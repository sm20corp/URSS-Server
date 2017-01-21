package urss.server.components;

import io.vertx.core.json.JsonObject;
import com.google.gson.Gson;

public class JsonHandler {
  private static Gson instance = null;

  private JsonHandler() {

  }

  public static Gson getInstance() {
    if (instance == null) {
      instance = new Gson();
    }
    return instance;
  }

  public static Boolean verifyProperties(JsonObject obj, String[] properties) {
    for (String property : properties) {
      if (!obj.containsKey(property)) {
        return false;
      }
    }
    return true;
  }
}
