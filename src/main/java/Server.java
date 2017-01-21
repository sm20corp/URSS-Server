package urss.server;

import io.vertx.core.Vertx;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.ext.web.handler.CorsHandler;

import urss.server.components.MongoDB;
import urss.server.auth.AuthService;
import urss.server.api.credential.CredentialRoute;
import urss.server.api.article.ArticleRoute;
import urss.server.api.feed.FeedRoute;

public class Server extends AbstractVerticle {
  private static Server instance = null;
  private Router router;
  private MongoDB db;

  @Override
  public void start(Future<Void> fut) {
    instance = this;

    this.db = MongoDB.getInstance();// default instance points to localhost:27017 to "urss" db
    this.router = Router.router(vertx);

    configure();
    setupAuthentication();
    setupRoutes();

    vertx.createHttpServer().requestHandler(router::accept).listen(4242, result -> {
      if (result.succeeded()) {
        fut.complete();
      }
      else {
        fut.fail(result.cause());
      }
    });
  }

  private void configure() {
    this.router.route().handler(CorsHandler.create("*")
    .allowedMethod(HttpMethod.GET)
    .allowedMethod(HttpMethod.POST)
    .allowedMethod(HttpMethod.OPTIONS)
    .allowedMethod(HttpMethod.PUT)
    .allowedMethod(HttpMethod.PATCH)
    .allowedMethod(HttpMethod.DELETE)
    .allowedHeader("Access-Control-Request-Method")
    .allowedHeader("Access-Control-Allow-Credentials")
    .allowedHeader("Access-Control-Allow-Origin")
    .allowedHeader("Access-Control-Allow-Headers")
    .allowedHeader("Content-Type"));
    this.router.route().handler(BodyHandler.create());
  }

  private void setupAuthentication() {
    JWTAuthHandler authRedirect = AuthService.getInstance().generateHandler("/auth/local");

    this.router.route("/api/users/*").handler(authRedirect);
    this.router.route("/api/histories/*").handler(authRedirect);
    this.router.route("/api/credentials/:id").handler(authRedirect);
    this.router.route(HttpMethod.GET, "/api/credentials/").handler(authRedirect);
  }

  private void setupRoutes() {
    this.router.post("/auth/local").handler(AuthService.getInstance()::authLocal);

    CredentialRoute.attachRoutes(this.router);
    ArticleRoute.attachRoutes(this.router);
    FeedRoute.attachRoutes(this.router);
    //HistoryRoute.attachRoutes(this.router);
    //UserRoute.attachRoutes(this.router);
  }

  public static Server getInstance() {
    return instance;
  }
}
