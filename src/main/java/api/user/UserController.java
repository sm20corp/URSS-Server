package urss.server.api.user;

import io.vertx.ext.web.RoutingContext;

import urss.server.api.user.UserModel;

public class UserController {
  public static void create(RoutingContext rc) {
    rc.response().end("user/create");
  }

  public static void show(RoutingContext rc) {
    rc.response().end("user/show");
  }

  public static void update(RoutingContext rc) {
    rc.response().end("user/update");
  }

  public static void delete(RoutingContext rc) {
    rc.response().end("user/delete");
  }

  public static void list(RoutingContext rc) {
    rc.response().end("user/list");
  }
}
