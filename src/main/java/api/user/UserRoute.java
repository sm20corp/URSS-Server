package urss.server.api.user;

import io.vertx.ext.web.Router;

import urss.server.api.user.UserController;

public class UserRoute {
  private static final String suffix = "/api/users";

  public static void attachRoutes(Router router) {
    router.post(UserRoute.suffix + "/").handler(UserController::create);
    router.get(UserRoute.suffix + "/:id").handler(UserController::show);
    router.put(UserRoute.suffix + "/:id").handler(UserController::update);
    router.patch(UserRoute.suffix + "/:id").handler(UserController::update);
    router.delete(UserRoute.suffix + "/:id").handler(UserController::delete);
    router.get(UserRoute.suffix + "/").handler(UserController::list);
  }
}
