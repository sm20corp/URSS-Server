package urss.server.components;

import io.vertx.ext.mongo.MongoClient;

import urss.server.components.MongoConfig;
import urss.server.Server;

public class MongoDB extends ADatabaseClient<MongoClient> {
  private static final MongoConfig defaultConf = new MongoConfig();
  private static MongoDB instance = null;

  private MongoDB() {
    setClient(MongoClient.createShared(
                Server.getInstance().getVertx(),
                this.defaultConf.toJSON()));
  }

  private MongoDB(MongoConfig conf) {
    setClient(MongoClient.createShared(
                Server.getInstance().getVertx(),
                conf.toJSON()));
  }

  public static MongoDB getInstance() {
    if (instance == null) {
      instance = new MongoDB();
    }
    return instance;
  }

  public static MongoDB getInstance(MongoConfig conf) {
    if (instance == null) {
      instance = new MongoDB(conf);
    }
    return instance;
  }
}
