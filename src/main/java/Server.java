package urss.server;

import io.vertx.core.Vertx;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import urss.server.components.MongoDB;
import urss.server.api.user.UserRoute;

public class Server extends AbstractVerticle {
  private static Server instance = null;
  private Router router;
  private MongoDB db;

  @Override
  public void start(Future<Void> fut) {

    instance = this;

    this.db = MongoDB.getInstance();// default instance points to localhost:27017 to "urss" db

    this.router = Router.router(vertx);

    this.router.route().handler(BodyHandler.create());

    this.router.get("/").handler(this::home);

    UserRoute.attachRoutes(this.router);
//    FeedRoute.attachRoutes(this.router);

    vertx.createHttpServer().requestHandler(router::accept).listen(4242, result -> {
      if (result.succeeded()) {
        fut.complete();
      }
      else {
        fut.fail(result.cause());
      }
    });
  }

  public void home(RoutingContext rc) {
    System.out.println("homepage");
    JsonObject product1 = new JsonObject().put("itemId", "12345").put("name", "Cooler").put("price", "100.0");

    db.getClient().save("products", product1, id -> {
      System.out.println("Inserted id: " + id.result());

      db.getClient().find("products", new JsonObject().put("itemId", "12345"), res -> {
        System.out.println("Name is " + res.result().get(0).getString("name"));
        /*

           MongoDB.getClient().remove("products", new JsonObject().put("itemId", "12345"), rs -> {
           if (rs.succeeded()) {
            System.out.println("Product removed ");
           }
           });
         */

      });

    });
    rc.response().end("<h1>Cotisez vous pour acheter un micro à Corentin</h1>");
  }

  public static Server getInstance() {
    return instance;
  }
}
