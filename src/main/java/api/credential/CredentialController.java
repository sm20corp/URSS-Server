package urss.server.api.credential;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.net.HttpURLConnection;

import urss.server.components.MongoDB;
import urss.server.components.JsonHandler;
import urss.server.api.credential.CredentialModel;

public class CredentialController {
  public static void create(RoutingContext ctx) {
    System.out.println("params: " + ctx.request().params());
    System.out.println("body: " + ctx.getBodyAsJson());
    // check rights if needed

    // transform body to data model
    CredentialModel model = JsonHandler.getInstance().fromJson(ctx.getBodyAsJson().toString(), CredentialModel.class);

    // validate data and stuff
    // handle and throw http error, misformatted request
    if (!model.validate()) {
      System.out.println("credentialModel validation failed");
      ctx.fail(HttpURLConnection.HTTP_INTERNAL_ERROR);
      return ;
    }

    System.out.println("model toString: " + model);

    // create document
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
    ctx.response().end("credential/show");
  }

  public static void update(RoutingContext ctx) {
    System.out.println("params: " + ctx.request().params());
    System.out.println("body: " + ctx.getBodyAsJson());

    ctx.response().end("credential/update");
  }

  public static void delete(RoutingContext ctx) {
    ctx.response().end("credential/delete");
  }

  public static void list(RoutingContext ctx) {
    System.out.println("user: " + ctx.user().principal());
    ctx.response().end("credential/list");
  }
}
