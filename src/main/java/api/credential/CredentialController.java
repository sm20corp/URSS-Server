package urss.server.api.credential;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.net.HttpURLConnection;

import urss.server.components.MongoDB;
import urss.server.components.JsonHandler;
import urss.server.api.credential.CredentialModel;

public class CredentialController {
  public static void create(RoutingContext ctx) {
    JsonObject body = ctx.getBodyAsJson();
    System.out.println("params: " + ctx.request().params());
    System.out.println("body: " + body);

    if (!JsonHandler.verifyProperties(body, CredentialModel.requiredFields)) {
      System.out.println("required fields not present in request's body");

      ctx.response()
      .setStatusCode(HttpURLConnection.HTTP_BAD_REQUEST)
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(new JsonObject().put("message", "required fields not present in request's body").encodePrettily());
      return ;
    }

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

    // TODO
    // do the current user has the rights to see the requested id infos ? should be its id or admin

    System.out.println("you want this id: " + id);

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
    // TODO
    // do the current user has the rights to update the requested id infos ? should be its id or admin
    System.out.println("params: " + ctx.request().params());
    System.out.println("body: " + ctx.getBodyAsJson());

    ctx.response().end("credential/update");
  }

  public static void delete(RoutingContext ctx) {
    // TODO
    // do the current user has the rights to delete the requested id infos ? should be its id or admin
    ctx.response().end("credential/delete");
  }

  public static void list(RoutingContext ctx) {
    // TODO
    // do the current user has the rights to list all credentials ? should be admin
    System.out.println("user: " + ctx.user().principal());
    ctx.response().end("credential/list");
  }
}
