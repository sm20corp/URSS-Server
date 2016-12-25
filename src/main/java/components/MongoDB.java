package urss.server.components;

import io.vertx.core.Vertx;
import io.vertx.ext.mongo.MongoClient;

import urss.server.components.MongoConfig;

public class MongoDB extends ADatabaseClient<MongoClient> {
  private static final MongoConfig defaultConf = new MongoConfig();
  private static MongoDB instance = null;

  private MongoDB(Vertx vertx) {
    setClient(MongoClient.createShared(
                vertx,
                this.defaultConf.toJSON()));
  }

  private MongoDB(Vertx vertx, MongoConfig conf) {
    setClient(MongoClient.createShared(
                vertx,
                conf.toJSON()));
  }

  public static MongoDB getInstance() {
    return instance;
  }

  public static MongoDB getInstance(Vertx vertx) {
    if (instance == null) {
      instance = new MongoDB(vertx);
    }
    return instance;
  }

  public static MongoDB getInstance(Vertx vertx, MongoConfig conf) {
    if (instance == null) {
      instance = new MongoDB(vertx, conf);
    }
    return instance;
  }

  /*
     implement functions, maybe use an interface
   */

  // create
  // getOne
  // getAll
  // update
  // delete
}
