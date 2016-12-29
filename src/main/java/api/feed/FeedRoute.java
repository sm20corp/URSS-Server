package urss.server.api.feed;

import io.vertx.ext.web.Router;

import urss.server.api.feed.FeedController;

public class FeedRoute {
  private static final String suffix = "/api/feeds";

  public static void attachRoutes(Router router) {
    router.post(FeedRoute.suffix + "/").handler(FeedController::create);
    router.get(FeedRoute.suffix + "/:id").handler(FeedController::show);
    router.put(FeedRoute.suffix + "/:id").handler(FeedController::update);
    router.patch(FeedRoute.suffix + "/:id").handler(FeedController::update);
    router.delete(FeedRoute.suffix + "/:id").handler(FeedController::delete);
    router.get(FeedRoute.suffix + "/").handler(FeedController::list);
  }
}
