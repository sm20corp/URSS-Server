package urss.server;

import io.vertx.core.Vertx;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class Server extends AbstractVerticle {
  @Override
  public void start(Future<Void> fut) {

    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());

    /*
       router.route().handler(BodyHandler.create());
       router.get("/products/:productID").handler(this::handleGetProduct);
       router.put("/products/:productID").handler(this::handleAddProduct);
       router.get("/products").handler(this::handleListProducts);

       vertx.createHttpServer().requestHandler(router::accept).listen(8080);
     */

    vertx
    .createHttpServer()
    .requestHandler(r -> {
      JsonObject config = Vertx.currentContext().config();

      String uri = config.getString("mongo_uri");
      if (uri == null) {
        uri = "mongodb://localhost:27017";
      }
      String db = config.getString("mongo_db");
      if (db == null) {
        db = "urss";
      }
      JsonObject mongoconfig = new JsonObject()
                               .put("connection_string", uri)
                               .put("db_name", db);

      System.out.println("going to create a mongo client ...");

      MongoClient mongoClient = MongoClient.createShared(vertx, mongoconfig);

      System.out.println("... mongo client");

      JsonObject product1 = new JsonObject().put("itemId", "12345").put("name", "Cooler").put("price", "100.0");

      mongoClient.save("products", product1, id -> {
        System.out.println("Inserted id: " + id.result());

        mongoClient.find("products", new JsonObject().put("itemId", "12345"), res -> {
          System.out.println("Name is " + res.result().get(0).getString("name"));
          /*

             mongoClient.remove("products", new JsonObject().put("itemId", "12345"), rs -> {
             if (rs.succeeded()) {
              System.out.println("Product removed ");
             }
             });
           */

        });

      });
      r.response().end("<h1>Cotisez vous pour acheter un micro à Corentin</h1>");
    })
    .listen(4242, result -> {
      if (result.succeeded()) {
        fut.complete();
      } else {
        fut.fail(result.cause());
      }
    });
  }
}
