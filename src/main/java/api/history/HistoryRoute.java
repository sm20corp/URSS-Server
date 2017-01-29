package urss.server.api.history;

import io.vertx.ext.web.Router;

import urss.server.auth.AuthService;
import urss.server.api.history.HistoryController;

public class HistoryRoute {
  private static final String suffix = "/api/histories";

  public static void attachRoutes(Router router) {
    router.post(HistoryRoute.suffix + "/").handler(HistoryController::verifyProperties);
    router.post(HistoryRoute.suffix + "/").handler(HistoryController::create);
    router.post(HistoryRoute.suffix + "/").handler(HistoryController::ok);

    router.get(HistoryRoute.suffix + "/:id").handler(AuthService.getInstance()::hasAuthority);
//    router.get(HistoryRoute.suffix + "/:id").handler(HistoryController::isMine);
    router.get(HistoryRoute.suffix + "/:id").handler(HistoryController::show);

    router.put(HistoryRoute.suffix + "/:id").handler(AuthService.getInstance()::hasAuthority);
//    router.put(HistoryRoute.suffix + "/:id").handler(HistoryController::isMine);
    router.put(HistoryRoute.suffix + "/:id").handler(HistoryController::update);

    router.patch(HistoryRoute.suffix + "/:id").handler(AuthService.getInstance()::hasAuthority);
//    router.patch(HistoryRoute.suffix + "/:id").handler(HistoryController::isMine);
    router.patch(HistoryRoute.suffix + "/:id").handler(HistoryController::update);

    router.delete(HistoryRoute.suffix + "/:id").handler(AuthService.getInstance()::hasAuthority);
    router.delete(HistoryRoute.suffix + "/:id").handler(HistoryController::isAdmin);
    router.delete(HistoryRoute.suffix + "/:id").handler(HistoryController::delete);

    router.get(HistoryRoute.suffix + "/").handler(AuthService.getInstance()::hasAuthority);
    router.get(HistoryRoute.suffix + "/").handler(HistoryController::isAdmin);
    router.get(HistoryRoute.suffix + "/").handler(HistoryController::list);
  }
}
