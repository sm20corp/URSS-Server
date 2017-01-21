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
  public static void create(RoutingContext rc) {
    JsonObject body = rc.getBodyAsJson();
    String url = body.getString("url");
    System.out.println("url of the feed is: " + url);
    if (url != null && !url.isEmpty()) {
      try {
        URL feedUrl = new URL(url);

        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(feedUrl));

        //System.out.println(feed);
        rc.put("feed", feed);
        rc.reroute(HttpMethod.POST, "/api/feeds/");
      }
      catch (Exception ex) {
        ex.printStackTrace();
        System.out.println("ERROR: " + ex.getMessage());
      }
    }
    else {
    System.out.println("feedModel validation failed");

    rc.response()
    .setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST)
    .putHeader("content-type", "application/json; charset=utf-8")
    .end(new JsonObject().put("message", "feed validation failed").encodePrettily());
    return ;
    //rc.response().end("feed/create");
  }
  }

  public static void createModel(RoutingContext rc) {
    SyndFeed feed = rc.remove("feed");

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

      rc.response()
      .setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(new JsonObject().put("message", "feed validation failed").encodePrettily());
      return ;
    }
    MongoDB.getInstance().getClient().insert("feeds", model.toJSON(), res -> {
      if (res.succeeded()) {
        System.out.println("res: " + res.result());
        rc.response()
        .setStatusCode(HttpURLConnection.HTTP_OK)
        .putHeader("content-type", "application/json; charset=utf-8")
        .end(new JsonObject().put("id", res.result()).encodePrettily());
        return ;
      }
      else {
        System.out.println("FAIL: " + res.cause().getMessage());
        rc.fail(HttpURLConnection.HTTP_INTERNAL_ERROR);
        return ;
      }
    });
  }

  public static void show(RoutingContext rc) {
    String id = rc.request().getParam("id");

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
            rc.response().setStatusCode(HttpURLConnection.HTTP_NOT_FOUND);
          }
          else {
            rc.response().setStatusCode(HttpURLConnection.HTTP_OK);
          }

          rc.response()
          .putHeader("content-type", "application/json; charset=utf-8")
          .end(feed.encodePrettily());
          return ;
        }
        else {
          System.out.println("FAIL: " + res.cause().getMessage());
          rc.fail(HttpURLConnection.HTTP_INTERNAL_ERROR);
          return ;
        }
      }
    );
  }

  public static void update(RoutingContext rc) {
    rc.response().end("feed/update");
  }

  public static void delete(RoutingContext rc) {
    rc.response().end("feed/delete");
  }

  public static void list(RoutingContext rc) {
    rc.response().end("feed/list");
  }
}
