package urss.server.components;

import io.vertx.core.json.JsonObject;

public interface IDatabaseConfig<T> {
  public String toString();
  public JsonObject toJSON();
  public T fromJSON(JsonObject json);
}
