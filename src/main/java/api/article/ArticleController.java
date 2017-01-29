package urss.server.api.article;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import java.net.HttpURLConnection;

import urss.server.components.MongoDB;
import urss.server.components.JsonHandler;
import urss.server.api.article.ArticleModel;

public class ArticleController {
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

    if (!JsonHandler.verifyProperties(body, ArticleModel.requiredFields)) {
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
    System.out.println("params: " + ctx.request().params());
    System.out.println("body: " + body);

    ArticleModel model = JsonHandler.getInstance().fromJson(ctx.getBodyAsJson().toString(), ArticleModel.class);

    if (!model.validate()) {
      System.out.println("articleModel validation failed");

      ctx.response()
      .setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(new JsonObject().put("message", "model validation failed").encodePrettily());
      return ;
    }

    System.out.println("model toString: " + model);

    MongoDB.getInstance().getClient().findOne(
      "articles",
      model.toJSON(),
      new JsonObject(),
      findResult -> {
        if (findResult.succeeded()) {
          JsonObject article = findResult.result();

          if (article == null) {
            MongoDB.getInstance().getClient().insert("articles", model.toJSON(), insertResult -> {
              if (insertResult.succeeded()) {
                System.out.println("res: " + insertResult.result());
                ctx.response()
                .setStatusCode(HttpURLConnection.HTTP_OK)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(new JsonObject().put("id", insertResult.result()).encodePrettily());
                return ;
              }
              else {
                System.out.println("FAIL: " + insertResult.cause().getMessage());
                ctx.fail(HttpURLConnection.HTTP_INTERNAL_ERROR);
                return ;
              }
            });
          }
          else {
            ctx.response()
            .setStatusCode(HttpURLConnection.HTTP_OK)
            .putHeader("content-type", "application/json; charset=utf-8")
            .end(article.encodePrettily());
            return ;
          }
        }
        else {
          System.out.println("FAIL: " + findResult.cause().getMessage());
          ctx.fail(HttpURLConnection.HTTP_INTERNAL_ERROR);
          return ;
        }
      }
    );
  }

  public static void show(RoutingContext ctx) {
    String id = ctx.request().getParam("id");

    MongoDB.getInstance().getClient().findOne(
      "articles",
      new JsonObject().put("_id", id),
      new JsonObject().put("_id", false),
      res -> {
        if (res.succeeded()) {
          JsonObject article = res.result();

          System.out.println("article found: " + article);

          if (article == null) {
            article = new JsonObject().put("message", "id not found in db");
            ctx.response().setStatusCode(HttpURLConnection.HTTP_NOT_FOUND);
          }
          else {
            ctx.response().setStatusCode(HttpURLConnection.HTTP_OK);
          }

          ctx.response()
          .putHeader("content-type", "application/json; charset=utf-8")
          .end(article.encodePrettily());
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
        update.put(rField, body.getValue(rField));
      }
    }
    for (String oField : ArticleModel.optionalFields) {
      if (body.containsKey(oField)) {
        update.put(oField, body.getValue(oField));
      }
    }

    update = new JsonObject().put("$set", update);

    System.out.println("updated value :" + update);

    MongoDB.getInstance().getClient().updateCollection(
      "articles",
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
      "articles",
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
      .put("find", "articles"),
      res -> {
        if (res.succeeded()) {
          JsonObject result = res.result();
          JsonArray articles = result.getJsonObject("cursor").getJsonArray("firstBatch");

          System.out.println("all articles: " + articles);

          if (articles == null || articles.size() <= 0) {
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
          .end(articles.encodePrettily());
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
