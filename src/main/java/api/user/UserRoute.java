package urss.server.api.user;

import io.vertx.ext.web.Router;

import urss.server.auth.AuthService;
import urss.server.api.user.UserController;

public class UserRoute {
  private static final String suffix = "/api/users";

  public static void attachRoutes(Router router) {
    router.post(UserRoute.suffix + "/").handler(UserController::verifyProperties);
    router.post(UserRoute.suffix + "/").handler(UserController::create);

    router.get(UserRoute.suffix + "/:id").handler(AuthService.getInstance()::hasAuthority);
//    router.get(UserRoute.suffix + "/:id").handler(UserController::isMine);
    router.get(UserRoute.suffix + "/:id").handler(UserController::show);

    router.put(UserRoute.suffix + "/:id").handler(AuthService.getInstance()::hasAuthority);
//    router.put(UserRoute.suffix + "/:id").handler(UserController::isMine);
    router.put(UserRoute.suffix + "/:id").handler(UserController::update);

    router.patch(UserRoute.suffix + "/:id").handler(AuthService.getInstance()::hasAuthority);
//    router.patch(UserRoute.suffix + "/:id").handler(UserController::isMine);
    router.patch(UserRoute.suffix + "/:id").handler(UserController::update);

    router.delete(UserRoute.suffix + "/:id").handler(AuthService.getInstance()::hasAuthority);
    router.delete(UserRoute.suffix + "/:id").handler(UserController::isAdmin);
    router.delete(UserRoute.suffix + "/:id").handler(UserController::delete);

    router.get(UserRoute.suffix + "/").handler(AuthService.getInstance()::hasAuthority);
    router.get(UserRoute.suffix + "/").handler(UserController::isAdmin);
    router.get(UserRoute.suffix + "/").handler(UserController::list);
  }
}
