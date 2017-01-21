package urss.server.api.credential;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import java.net.HttpURLConnection;

import urss.server.components.MongoDB;
import urss.server.components.JsonHandler;
import urss.server.api.credential.CredentialModel;

public class CredentialController {
  public static void isMine(RoutingContext ctx) {
    String id = ctx.request().getParam("id");

    System.out.println("user principal: " + ctx.user().principal());
    System.out.println("you want this id: " + id);
    System.out.println("isAdmin: " + ctx.get("isAdmin"));

    if ((boolean) ctx.get("isAdmin") == true || ctx.user().principal().getString("userId").equals(id)) {
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

    if (!JsonHandler.verifyProperties(body, CredentialModel.requiredFields)) {
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

    CredentialModel model = JsonHandler.getInstance().fromJson(ctx.getBodyAsJson().toString(), CredentialModel.class);

    if (!model.validate()) {
      System.out.println("credentialModel validation failed");

      ctx.response()
      .setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(new JsonObject().put("message", "model validation failed").encodePrettily());
      return ;
    }

    System.out.println("model toString: " + model);

    MongoDB.getInstance().getClient().insert("credentials", model.toJSON(), res -> {
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
      "credentials",
      new JsonObject().put("_id", id),
      new JsonObject()
      .put("_id", false)
      .put("password", false),
      res -> {
        if (res.succeeded()) {
          JsonObject credential = res.result();

          System.out.println("credential found: " + credential);

          if (credential == null) {
            credential = new JsonObject().put("message", "id not found in db");
            ctx.response().setStatusCode(HttpURLConnection.HTTP_NOT_FOUND);
          }
          else {
            ctx.response().setStatusCode(HttpURLConnection.HTTP_OK);
          }

          ctx.response()
          .putHeader("content-type", "application/json; charset=utf-8")
          .end(credential.encodePrettily());
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

    for (String rField : CredentialModel.requiredFields) {
      if (body.containsKey(rField)) {
        update.put(rField, body.getString(rField));
      }
    }
    for (String oField : CredentialModel.optionalFields) {
      if (body.containsKey(oField)) {
        update.put(oField, body.getString(oField));
      }
    }

    if (update.containsKey("password")) {
      update.put("password", CredentialModel.encryptPassword(update.getString("password")));
    }

    update = new JsonObject().put("$set", update);

    System.out.println("updated value :" + update);

    MongoDB.getInstance().getClient().updateCollection(
      "credentials",
      query,
      update,
      res -> {
        if (res.succeeded()) {
          JsonObject result = res.result().toJson();

          System.out.println("result: " + result);

          // revock access token ?

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
      "credentials",
      new JsonObject().put("_id", id),
      res -> {
        if (res.succeeded()) {
          JsonObject result = res.result().toJson();

          System.out.println("result: " + result);

          // revock access token ?

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
    System.out.println("user: " + ctx.user().principal());
    System.out.println("isAdmin: " + ctx.get("isAdmin"));

    if ((boolean) ctx.get("isAdmin") == false) {
      ctx.response()
      .setStatusCode(HttpURLConnection.HTTP_FORBIDDEN)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(new JsonObject().put("message", "you must be admin").encodePrettily());
      return ;
    }

    MongoDB.getInstance().getClient().runCommand(
      "find",
      new JsonObject()
      .put("find", "credentials")
      .put("projection", new JsonObject()
        .put("password", false)),
      res -> {
        if (res.succeeded()) {
          JsonObject result = res.result();
          JsonArray credentials = result.getJsonObject("cursor").getJsonArray("firstBatch");

          System.out.println("all credentials: " + credentials);

          if (credentials == null || credentials.size() <= 0) {
            ctx.response()
            .setStatusCode(HttpURLConnection.HTTP_NOT_FOUND)
            .putHeader("content-type", "application/json; charset=utf-8")
            .end(new JsonObject().put("message", "no credential found").encodePrettily());
            return ;
          }
          else {
            ctx.response().setStatusCode(HttpURLConnection.HTTP_OK);
          }

          ctx.response()
          .putHeader("content-type", "application/json; charset=utf-8")
          .end(credentials.encodePrettily());
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
