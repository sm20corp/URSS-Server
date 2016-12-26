package urss.server.components;

import io.vertx.core.json.JsonObject;

import urss.server.components.JsonHandler;

abstract public class AModel<MODEL> {
  public JsonObject toJSON() {
    return new JsonObject(JsonHandler.getInstance().toJson(this));
  }

  public MongoConfig fromJSON(JsonObject json) {
    return JsonHandler.getInstance().fromJson(json.toString(), MongoConfig.class);
  }
}
