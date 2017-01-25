package urss.server.api.feed;

import io.vertx.ext.web.Router;

import urss.server.auth.AuthService;
import urss.server.api.feed.FeedController;

public class FeedRoute {
  private static final String suffix = "/api/feeds";

  public static void attachRoutes(Router router) {
    router.post(FeedRoute.suffix + "/fromURL").handler(FeedController::create);

    router.post(FeedRoute.suffix + "/").handler(FeedController::verifyProperties);
    router.post(FeedRoute.suffix + "/").handler(FeedController::createModel);

    router.get(FeedRoute.suffix + "/:id").handler(FeedController::show);

    router.put(FeedRoute.suffix + "/:id").handler(AuthService.getInstance()::hasAuthority);
    router.put(FeedRoute.suffix + "/:id").handler(FeedController::isAdmin);
    router.put(FeedRoute.suffix + "/:id").handler(FeedController::update);

    router.patch(FeedRoute.suffix + "/:id").handler(AuthService.getInstance()::hasAuthority);
    router.patch(FeedRoute.suffix + "/:id").handler(FeedController::isAdmin);
    router.patch(FeedRoute.suffix + "/:id").handler(FeedController::update);

    router.delete(FeedRoute.suffix + "/:id").handler(AuthService.getInstance()::hasAuthority);
    router.delete(FeedRoute.suffix + "/:id").handler(FeedController::isAdmin);
    router.delete(FeedRoute.suffix + "/:id").handler(FeedController::delete);

    router.get(FeedRoute.suffix + "/").handler(FeedController::list);
  }
}
