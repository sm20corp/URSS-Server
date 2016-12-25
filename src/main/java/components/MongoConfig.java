package urss.server.components;

import io.vertx.core.json.JsonObject;

import urss.server.components.IDatabaseConfig;
import urss.server.components.JsonHandler;

public class MongoConfig implements IDatabaseConfig<MongoConfig> {
  private static final String defaultURI = "mongodb://localhost:27017";
  private static final String defaultName = "urss";
  private String connection_string;
  private String db_name;

  MongoConfig() {
    setURI(MongoConfig.defaultURI);
    setName(MongoConfig.defaultName);
  }

  MongoConfig(String uri, String dbName) {
    setURI(uri);
    setName(dbName);
  }

  public void setURI(String uri) {
    this.connection_string = uri;
  }

  public String getURI() {
    return this.connection_string;
  }

  public void setName(String dbName) {
    this.db_name = dbName;
  }

  public String getName() {
    return this.db_name;
  }

  @Override
  public String toString() {
    return "uri: " + getURI() + " - name: " + getName();
  }

/*
   generic abstract class for these two methods ?
 */

  public JsonObject toJSON() {
    return new JsonObject(JsonHandler.getInstance().toJson(this));
  }

  public MongoConfig fromJSON(JsonObject json) {
    return JsonHandler.getInstance().fromJson(json.toString(), MongoConfig.class);
  }
}
