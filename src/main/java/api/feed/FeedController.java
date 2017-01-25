package urss.server.api.feed;

import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import urss.server.components.JsonHandler;

import java.util.*;

import java.net.HttpURLConnection;
import io.vertx.core.json.JsonObject;

import urss.server.components.MongoDB;

import java.net.URL;
import java.io.InputStreamReader;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import urss.server.api.feed.FeedModel;

public class FeedController {
  public static void isAdmin(RoutingContext ctx) {
    System.out.println("user principal: " + ctx.user().principal());
    System.out.println("isAdmin: " + ctx.get("isAdmin"));

    if ((boolean) ctx.get("isAdmin") == true) {
      System.out.println("Access granted");

      ctx.next();
    }
    else {
      System.out.println("Access denied");
      ctx.response()
      .setStatusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(new JsonObject().put("message", "You must be admin to modify or delete this resource").encodePrettily());
      return ;
    }
  }

  public static void verifyProperties(RoutingContext ctx) {
    System.out.println("verifyProperties");
    JsonObject body = ctx.getBodyAsJson();

    if (!JsonHandler.verifyProperties(body, FeedModel.requiredFields)) {
      System.out.println("required fields not present in request's body");

      ctx.response()
      .setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(new JsonObject().put("message", "required fields not present in request's body").encodePrettily());
      return ;
    }
    else {
      System.out.println("properties verified");
      ctx.next();
    }
  }

  public static void create(RoutingContext ctx) {
    JsonObject body = ctx.getBodyAsJson();
    String url = body.getString("url");
    System.out.println("url of the feed is: " + url);
    if (url != null && !url.isEmpty()) {
      try {
        URL feedUrl = new URL(url);

        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(feedUrl));

        System.out.println(feed);
        ctx.put("feed", feed);
        ctx.reroute(HttpMethod.POST, "/api/feeds/");
      }
      catch (Exception ex) {
        ex.printStackTrace();
        System.out.println("ERROR: " + ex.getMessage());
      }
    }
    else {
      System.out.println("feedModel validation failed");

      ctx.response()
      .setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(new JsonObject().put("message", "feed validation failed").encodePrettily());
      return ;
    }
  }

  public static void createModel(RoutingContext ctx) {
    SyndFeed feed = ctx.remove("feed");

    System.out.println("feed title " + feed.getTitle());
    System.out.println("feed description " + feed.getDescription());
    System.out.println("feed link " + feed.getLink());
    List entries = feed.getEntries();
		Iterator itEntries = entries.iterator();

		while (itEntries.hasNext()) {
			SyndEntry entry = (SyndEntry) itEntries.next();
      if (entry.getTitle() != null)
		    System.out.println("{\nArticle Title: " + entry.getTitle());
      if (entry.getLink() != null)
		    System.out.println("Article Link: " + entry.getLink());
			if (entry.getPublishedDate() != null)
        System.out.println("Article Publish Date: " + entry.getPublishedDate());
      if (entry.getEnclosures().size() > 0)
        System.out.println("Article Image: " + entry.getEnclosures().get(0).getUrl());
			if (entry.getDescription() != null)
        System.out.println("Article Description: " + entry.getDescription().getValue() + "\n}");
		}
    JsonObject jo = new JsonObject();
    jo.put("title", feed.getTitle());
    jo.put("description", feed.getDescription());
    jo.put("link", feed.getLink());
    FeedModel model = JsonHandler.getInstance().fromJson(jo.toString(), FeedModel.class);
    if (!model.validate()) {
      System.out.println("feedModel validation failed");

      ctx.response()
      .setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(new JsonObject().put("message", "feed validation failed").encodePrettily());
      return ;
    }
    MongoDB.getInstance().getClient().insert("feeds", model.toJSON(), res -> {
      if (res.succeeded()) {
        System.out.println("res: " + res.result());
        ctx.response()
        .setStatusCode(HttpURLConnection.HTTP_OK)
        .putHeader("content-type", "application/json; charset=utf-8")
        .end(new JsonObject().put("id", res.result()).encodePrettily());
        return ;
      }
      else {
        System.out.println("FAIL: " + res.cause().getMessage());
        ctx.fail(HttpURLConnection.HTTP_INTERNAL_ERROR);
        return ;
      }
    });
  }

  public static void show(RoutingContext ctx) {
    String id = ctx.request().getParam("id");

    MongoDB.getInstance().getClient().findOne(
      "feeds",
      new JsonObject().put("_id", id),
      new JsonObject()
      .put("_id", false)
      .put("title", false)
      .put("description", false)
      .put("link", false),
      res -> {
        if (res.succeeded()) {
          JsonObject feed = res.result();

          System.out.println("feed found: " + feed);

          if (feed == null) {
            feed = new JsonObject().put("message", "id not found in db");
            ctx.response().setStatusCode(HttpURLConnection.HTTP_NOT_FOUND);
          }
          else {
            ctx.response().setStatusCode(HttpURLConnection.HTTP_OK);
          }

          ctx.response()
          .putHeader("content-type", "application/json; charset=utf-8")
          .end(feed.encodePrettily());
          return ;
        }
        else {
          System.out.println("FAIL: " + res.cause().getMessage());
          ctx.fail(HttpURLConnection.HTTP_INTERNAL_ERROR);
          return ;
        }
      }
    );
  }

  public static void update(RoutingContext ctx) {
    JsonObject body = ctx.getBodyAsJson();
    System.out.println("params: " + ctx.request().params());
    System.out.println("body: " + body);
    String id = ctx.request().getParam("id");
    JsonObject query = new JsonObject();
    query.put("_id", id);
    JsonObject update = new JsonObject();

    for (String rField : ArticleModel.requiredFields) {
      if (body.containsKey(rField)) {
        update.put(rField, body.getString(rField));
      }
    }
    for (String oField : ArticleModel.optionalFields) {
      if (body.containsKey(oField)) {
        update.put(oField, body.getString(oField));
      }
    }

    update = new JsonObject().put("$set", update);

    System.out.println("updated value :" + update);

    MongoDB.getInstance().getClient().updateCollection(
      "feeds",
      query,
      update,
      res -> {
        if (res.succeeded()) {
          JsonObject result = res.result().toJson();

          System.out.println("result: " + result);

          ctx.response()
          .setStatusCode(HttpURLConnection.HTTP_NO_CONTENT)
          .putHeader("content-type", "application/json; charset=utf-8")
          .end();
          return ;
        }
        else {
          System.out.println("FAIL: " + res.cause().getMessage());
          ctx.fail(HttpURLConnection.HTTP_INTERNAL_ERROR);
          return ;
        }
      }
    );
  }

  public static void delete(RoutingContext ctx) {
    String id = ctx.request().getParam("id");

    MongoDB.getInstance().getClient().removeDocuments(
      "feeds",
      new JsonObject().put("_id", id),
      res -> {
        if (res.succeeded()) {
          JsonObject result = res.result().toJson();

          System.out.println("result: " + result);

          ctx.response()
          .setStatusCode(HttpURLConnection.HTTP_NO_CONTENT)
          .putHeader("content-type", "application/json; charset=utf-8")
          .end();
          return ;
        }
        else {
          System.out.println("FAIL: " + res.cause().getMessage());
          ctx.fail(HttpURLConnection.HTTP_INTERNAL_ERROR);
          return ;
        }
      }
    );
  }

  public static void list(RoutingContext ctx) {
    MongoDB.getInstance().getClient().runCommand(
      "find",
      new JsonObject()
      .put("find", "feeds"),
      res -> {
        if (res.succeeded()) {
          JsonObject result = res.result();
          JsonArray feeds = result.getJsonObject("cursor").getJsonArray("firstBatch");

          System.out.println("all feeds: " + feeds);

          if (feeds == null || feeds.size() <= 0) {
            ctx.response()
            .setStatusCode(HttpURLConnection.HTTP_NOT_FOUND)
            .putHeader("content-type", "application/json; charset=utf-8")
            .end(new JsonObject().put("message", "no article found").encodePrettily());
            return ;
          }
          else {
            ctx.response().setStatusCode(HttpURLConnection.HTTP_OK);
          }

          ctx.response()
          .putHeader("content-type", "application/json; charset=utf-8")
          .end(feeds.encodePrettily());
          return ;
        }
        else {
          System.out.println("FAIL: " + res.cause().getMessage());
          ctx.fail(HttpURLConnection.HTTP_INTERNAL_ERROR);
          return ;
        }
      }
    );
  }
}
