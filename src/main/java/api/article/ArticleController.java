package urss.server.api.article;

import io.vertx.ext.web.RoutingContext;

import urss.server.api.article.ArticleModel;

public class ArticleController {
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
