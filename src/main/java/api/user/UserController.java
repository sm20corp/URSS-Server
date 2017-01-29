package urss.server.api.user;

import java.net.HttpURLConnection;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;

import urss.server.components.MongoDB;
import urss.server.components.JsonHandler;
import urss.server.api.user.UserModel;

public class UserController {
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

  public static void isMine(RoutingContext ctx) {
    String id = ctx.request().getParam("id");

    System.out.println("user principal: " + ctx.user().principal());
    System.out.println("you want this id: " + id);
    System.out.println("isAdmin: " + ctx.get("isAdmin"));

    // TODO
    /** code property verification **/
    if ((boolean) ctx.get("isAdmin") == true || ctx.user().principal().getString("credentialId").equals(id)) {
      System.out.println("Access granted");

      ctx.next();
    }
    else {
      System.out.println("Access denied");
      ctx.response()
      .setStatusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(new JsonObject().put("message", "You are not admin and you are requesting a resource that isn't yours").encodePrettily());
      return ;
    }
  }

  public static void verifyProperties(RoutingContext ctx) {
    System.out.println("verifyProperties");
    JsonObject body = ctx.getBodyAsJson();

    if (!JsonHandler.verifyProperties(body, UserModel.requiredFields)) {
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

  public static void ok(RoutingContext ctx) {
    ctx.response()
    .setStatusCode(HttpURLConnection.HTTP_OK)
    .putHeader("content-type", "application/json; charset=utf-8")
    .end(ctx.getBodyAsJson().encodePrettily());
  }

  public static void create(RoutingContext ctx) {
    JsonObject body = ctx.getBodyAsJson();
    System.out.println("params: " + ctx.request().params());
    System.out.println("body: " + body);

    UserModel model = JsonHandler.getInstance().fromJson(ctx.getBodyAsJson().toString(), UserModel.class);

    if (!model.validate()) {
      System.out.println("UserModel validation failed");

      ctx.response()
      .setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(new JsonObject().put("message", "model validation failed").encodePrettily());
      return ;
    }

    System.out.println("model toString: " + model);

    MongoDB.getInstance().getClient().insert("users", model.toJSON(), res -> {
      if (res.succeeded()) {
        System.out.println("res: " + res.result());

        ctx.setBody(Buffer.buffer(new JsonObject().put("id", res.result()).toString()));
        ctx.next();
      }
      else {
        System.out.println("FAIL: " + res.cause().getMessage());
        ctx.fail(HttpURLConnection.HTTP_INTERNAL_ERROR);
      }
    });
  }

  public static void show(RoutingContext ctx) {
    String id = ctx.request().getParam("id");

    MongoDB.getInstance().getClient().findOne(
      "users",
      new JsonObject().put("_id", id),
      new JsonObject().put("_id", false),
      res -> {
        if (res.succeeded()) {
          JsonObject user = res.result();

          System.out.println("user found: " + user);

          if (user == null) {
            user = new JsonObject().put("message", "id not found in db");
            ctx.response().setStatusCode(HttpURLConnection.HTTP_NOT_FOUND);
          }
          else {
            ctx.response().setStatusCode(HttpURLConnection.HTTP_OK);
          }

          ctx.response()
          .putHeader("content-type", "application/json; charset=utf-8")
          .end(user.encodePrettily());
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

    for (String rField : UserModel.requiredFields) {
      if (body.containsKey(rField)) {
        update.put(rField, body.getValue(rField));
      }
    }
    for (String oField : UserModel.optionalFields) {
      if (body.containsKey(oField)) {
        update.put(oField, body.getValue(oField));
      }
    }

    update = new JsonObject().put("$set", update);

    System.out.println("updated value :" + update);

    MongoDB.getInstance().getClient().updateCollection(
      "users",
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
      "users",
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
      .put("find", "users"),
      res -> {
        if (res.succeeded()) {
          JsonObject result = res.result();
          JsonArray users = result.getJsonObject("cursor").getJsonArray("firstBatch");

          System.out.println("all users: " + users);

          if (users == null || users.size() <= 0) {
            ctx.response()
            .setStatusCode(HttpURLConnection.HTTP_NOT_FOUND)
            .putHeader("content-type", "application/json; charset=utf-8")
            .end(new JsonObject().put("message", "no user found").encodePrettily());
            return ;
          }
          else {
            ctx.response().setStatusCode(HttpURLConnection.HTTP_OK);
          }

          ctx.response()
          .putHeader("content-type", "application/json; charset=utf-8")
          .end(users.encodePrettily());
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

  public static void credentialToHistory(RoutingContext ctx) {
    JsonObject body = ctx.getBodyAsJson();

    ctx.put("credentialId", body.getString("id"));
    ctx.setBody(Buffer.buffer(new JsonObject().toString()));
    ctx.next();
  }

  public static void historyToUser(RoutingContext ctx) {
    JsonObject body = ctx.getBodyAsJson();

    ctx.put("historyId", body.getString("id"));
    ctx.setBody(Buffer.buffer(
      new JsonObject()
      .put("credential", ((String) ctx.get("credentialId")))
      .put("history", ((String) ctx.get("historyId")))
      .toString()));
    ctx.next();
  }

  public static void viewArticle(RoutingContext ctx) {
    System.out.println("params: " + ctx.request().params());
    String id = ctx.request().getParam("id");

    // check if id exists in articles
    MongoDB.getInstance().getClient().findOne(
      "articles",
      new JsonObject().put("_id", id),
      new JsonObject(),
      articleResult -> {
        if (articleResult.succeeded()) {
          JsonObject article = articleResult.result();

          System.out.println("article found: " + article);

          if (article == null) {
            ctx.response()
            .setStatusCode(HttpURLConnection.HTTP_NOT_FOUND)
            .putHeader("content-type", "application/json; charset=utf-8")
            .end(new JsonObject().put("message", "article id doesn't exists").encodePrettily());
            return ;
          }

          // get current user
          MongoDB.getInstance().getClient().findOne(
            "users",
            new JsonObject().put("_id", ctx.user().principal().getString("userId")),
            new JsonObject(),
            userResult -> {
              if (userResult.succeeded()) {
                JsonObject user = userResult.result();

                System.out.println("user found: " + user);

                if (user == null) {
                  ctx.response()
                  .setStatusCode(HttpURLConnection.HTTP_NOT_FOUND)
                  .putHeader("content-type", "application/json; charset=utf-8")
                  .end(new JsonObject().put("message", "your user id doesn't exist").encodePrettily());
                  return ;
                }

                // set article as read in history
                MongoDB.getInstance().getClient().updateCollection(
                  "histories",
                  new JsonObject().put("_id", user.getString("history")),
                  new JsonObject().put("$push", new JsonObject().put("viewedArticles", id)),
                  historyResult -> {
                    if (historyResult.succeeded()) {
                      JsonObject result = historyResult.result().toJson();

                      System.out.println("result: " + result);

                      ctx.response()
                      .setStatusCode(HttpURLConnection.HTTP_NO_CONTENT)
                      .putHeader("content-type", "application/json; charset=utf-8")
                      .end();
                      return ;
                    }
                    else {
                      System.out.println("FAIL: " + historyResult.cause().getMessage());
                      ctx.fail(HttpURLConnection.HTTP_INTERNAL_ERROR);
                      return ;
                    }
                  }
                );
              }
              else {
                System.out.println("FAIL: " + userResult.cause().getMessage());
                ctx.fail(HttpURLConnection.HTTP_INTERNAL_ERROR);
                return ;
              }
            }
          );
        }
        else {
          System.out.println("FAIL: " + articleResult.cause().getMessage());
          ctx.fail(HttpURLConnection.HTTP_INTERNAL_ERROR);
          return ;
        }
      }
    );
  }

  public static void bookmarkFeed(RoutingContext ctx) {
    System.out.println("params: " + ctx.request().params());
    String id = ctx.request().getParam("id");

    // check if id exists in articles
    MongoDB.getInstance().getClient().findOne(
      "feeds",
      new JsonObject().put("_id", id),
      new JsonObject(),
      feedResult -> {
        if (feedResult.succeeded()) {
          JsonObject feed = feedResult.result();

          System.out.println("feed found: " + feed);

          if (feed == null) {
            ctx.response()
            .setStatusCode(HttpURLConnection.HTTP_NOT_FOUND)
            .putHeader("content-type", "application/json; charset=utf-8")
            .end(new JsonObject().put("message", "feed id doesn't exists").encodePrettily());
            return ;
          }

          // get current user
          MongoDB.getInstance().getClient().findOne(
            "users",
            new JsonObject().put("_id", ctx.user().principal().getString("userId")),
            new JsonObject(),
            userResult -> {
              if (userResult.succeeded()) {
                JsonObject user = userResult.result();

                System.out.println("user found: " + user);

                if (user == null) {
                  ctx.response()
                  .setStatusCode(HttpURLConnection.HTTP_NOT_FOUND)
                  .putHeader("content-type", "application/json; charset=utf-8")
                  .end(new JsonObject().put("message", "your user id doesn't exist").encodePrettily());
                  return ;
                }

                // set article as read in history
                MongoDB.getInstance().getClient().updateCollection(
                  "histories",
                  new JsonObject().put("_id", user.getString("history")),
                  new JsonObject().put("$push", new JsonObject().put("bookmarks", id)),
                  historyResult -> {
                    if (historyResult.succeeded()) {
                      JsonObject result = historyResult.result().toJson();

                      System.out.println("result: " + result);

                      ctx.response()
                      .setStatusCode(HttpURLConnection.HTTP_NO_CONTENT)
                      .putHeader("content-type", "application/json; charset=utf-8")
                      .end();
                      return ;
                    }
                    else {
                      System.out.println("FAIL: " + historyResult.cause().getMessage());
                      ctx.fail(HttpURLConnection.HTTP_INTERNAL_ERROR);
                      return ;
                    }
                  }
                );
              }
              else {
                System.out.println("FAIL: " + userResult.cause().getMessage());
                ctx.fail(HttpURLConnection.HTTP_INTERNAL_ERROR);
                return ;
              }
            }
          );
        }
        else {
          System.out.println("FAIL: " + feedResult.cause().getMessage());
          ctx.fail(HttpURLConnection.HTTP_INTERNAL_ERROR);
          return ;
        }
      }
    );
  }
}
