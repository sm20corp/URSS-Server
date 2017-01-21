package urss.server.api.credential;

import io.vertx.ext.web.Router;

import urss.server.auth.AuthService;
import urss.server.api.credential.CredentialController;

public class CredentialRoute {
  private static final String suffix = "/api/credentials";

  public static void attachRoutes(Router router) {
    router.post(CredentialRoute.suffix + "/").handler(CredentialController::verifyProperties);
    router.post(CredentialRoute.suffix + "/").handler(CredentialController::create);

    router.get(CredentialRoute.suffix + "/:id").handler(AuthService.getInstance()::hasAuthority);
    router.get(CredentialRoute.suffix + "/:id").handler(CredentialController::isMine);
    router.get(CredentialRoute.suffix + "/:id").handler(CredentialController::show);

    router.put(CredentialRoute.suffix + "/:id").handler(AuthService.getInstance()::hasAuthority);
    router.put(CredentialRoute.suffix + "/:id").handler(CredentialController::isMine);
    router.put(CredentialRoute.suffix + "/:id").handler(CredentialController::update);

    router.patch(CredentialRoute.suffix + "/:id").handler(AuthService.getInstance()::hasAuthority);
    router.patch(CredentialRoute.suffix + "/:id").handler(CredentialController::isMine);
    router.patch(CredentialRoute.suffix + "/:id").handler(CredentialController::update);

    router.delete(CredentialRoute.suffix + "/:id").handler(AuthService.getInstance()::hasAuthority);
    router.delete(CredentialRoute.suffix + "/:id").handler(CredentialController::isMine);
    router.delete(CredentialRoute.suffix + "/:id").handler(CredentialController::delete);

    router.get(CredentialRoute.suffix + "/").handler(AuthService.getInstance()::hasAuthority);
    router.get(CredentialRoute.suffix + "/").handler(CredentialController::list);
  }
}
