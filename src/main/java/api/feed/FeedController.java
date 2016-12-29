package urss.server.api.feed;

import io.vertx.ext.web.RoutingContext;

import urss.server.api.feed.FeedModel;

public class FeedController {
  public static void create(RoutingContext rc) {
    rc.response().end("feed/create");
  }

  public static void show(RoutingContext rc) {
    rc.response().end("feed/show");
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
