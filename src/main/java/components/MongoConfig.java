package urss.server.components;

import urss.server.components.IModel;
import urss.server.components.AModel;

public class MongoConfig extends AModel<MongoConfig> implements IModel<MongoConfig> {
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
}
