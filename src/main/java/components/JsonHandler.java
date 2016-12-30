package urss.server.components;

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
}
