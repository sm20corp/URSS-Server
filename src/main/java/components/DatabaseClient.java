package urss.server.components;

import io.vertx.ext.mongo.MongoClient;

import urss.server.components.Gson;

public class DatabaseClient<CLIENT, CONF> {
  private static final CONF defaultConf = new CONF();
  private static DatabaseClient instance = null;
  private CLIENT dbClient = null;

  private DatabaseClient() {
    setClient(MongoClient.createShared(
                vertx,
                Gson.toJson(DatabaseClient.defaultConf)));
  }

  private DatabaseClient(CONF conf) {
    setClient(MongoClient.createShared(
                vertx,
                Gson.toJson(conf)));
  }

  public static DatabaseClient getInstance() {
    if (instance == null) {
      instance = new DatabaseClient();
    }
    return instance;
  }

  public static DatabaseClient getInstance(CONF conf) {
    if (instance == null) {
      instance = new DatabaseClient(conf);
    }
    return instance;
  }

  public void setClient(CLIENT client) {
    this.dbClient = client;
  }

  public CLIENT getClient() {
    return this.dbClient;
  }
}
