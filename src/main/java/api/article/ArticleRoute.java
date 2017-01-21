package urss.server.api.article;

import io.vertx.ext.web.Router;

import urss.server.api.article.ArticleController;

public class ArticleRoute {
  private static final String suffix = "/api/articles";

  public static void attachRoutes(Router router) {
    router.post(ArticleRoute.suffix + "/").handler(ArticleController::verifyProperties);
    router.post(ArticleRoute.suffix + "/").handler(ArticleController::create);

    router.get(ArticleRoute.suffix + "/:id").handler(ArticleController::show);

    router.put(ArticleRoute.suffix + "/:id").handler(AuthService.getInstance()::hasAuthority);
    router.put(ArticleRoute.suffix + "/:id").handler(ArticleController::isAdmin);
    router.put(ArticleRoute.suffix + "/:id").handler(ArticleController::update);

    router.patch(ArticleRoute.suffix + "/:id").handler(AuthService.getInstance()::hasAuthority);
    router.patch(ArticleRoute.suffix + "/:id").handler(ArticleController::isAdmin);
    router.patch(ArticleRoute.suffix + "/:id").handler(ArticleController::update);

    router.delete(ArticleRoute.suffix + "/:id").handler(AuthService.getInstance()::hasAuthority);
    router.delete(ArticleRoute.suffix + "/:id").handler(ArticleController::isAdmin);
    router.delete(ArticleRoute.suffix + "/:id").handler(ArticleController::delete);

    router.get(ArticleRoute.suffix + "/").handler(ArticleController::list);
  }
}
