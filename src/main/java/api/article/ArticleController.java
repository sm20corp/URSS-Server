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

  public static void create(RoutingContext rc) {
    rc.response().end("article/create");
  }

  public static void show(RoutingContext rc) {
    rc.response().end("article/show");
  }

  public static void update(RoutingContext rc) {
    rc.response().end("article/update");
  }

  public static void delete(RoutingContext rc) {
    rc.response().end("article/delete");
  }

  public static void list(RoutingContext rc) {
    rc.response().end("article/list");
  }
}
