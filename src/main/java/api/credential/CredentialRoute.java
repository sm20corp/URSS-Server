package urss.server.api.credential;

import io.vertx.ext.web.Router;

import urss.server.api.credential.CredentialController;

public class CredentialRoute {
  private static final String suffix = "/api/credentials";

  public static void attachRoutes(Router router) {
    router.post(CredentialRoute.suffix + "/").handler(CredentialController::create);
    router.get(CredentialRoute.suffix + "/:id").handler(CredentialController::show);
    router.put(CredentialRoute.suffix + "/:id").handler(CredentialController::update);
    router.patch(CredentialRoute.suffix + "/:id").handler(CredentialController::update);
    router.delete(CredentialRoute.suffix + "/:id").handler(CredentialController::delete);
    router.get(CredentialRoute.suffix + "/").handler(CredentialController::list);
  }
}
