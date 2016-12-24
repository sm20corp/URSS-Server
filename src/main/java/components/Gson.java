package urss.server.components;

import com.google.gson.Gson;
import io.vertx.core.json.JsonObject;

public class Gson<T> {
  private static Gson instance = null;

  public static Gson getInstance() {
    if (this.instance == null) {
      this.instance = new Gson();
    }
    return this.instance;
  }

  public static JsonObject toJSON(T object) {
    return new JsonObject(getInstance().toJson(object));
  }

  public static T fromJSON(JsonObject json, Class<T> klass) {
    return getInstance().fromJson(json.toString(), klass);
  }
}
