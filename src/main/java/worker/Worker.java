package urss.server.worker;

import java.util.List;

import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.buffer.Buffer;

import urss.server.Server;
import urss.server.components.MongoDB;

public class Worker {
  private static final long defaultDelay = 60000;//1 minute
  private long delay;
  private HttpClient httpClient = null;

  public Worker() {
    this(Worker.defaultDelay);
  }

  public Worker(long delay) {
    setDelay(delay);
  }

  public void initFeeds() {
    if (this.httpClient == null) {
      this.httpClient = Server.getInstance().getVertx().createHttpClient();
    }

    MongoDB.getInstance().getClient().find(
      "urls",
      new JsonObject(),
      res -> {
        if (res.succeeded()) {
          List<JsonObject> results = res.result();

          for (JsonObject feedUrl : results) {
            this.httpClient.post(4242, "localhost", "/api/feeds/fromURL",
              response -> {
                response.handler(body -> {
                  System.out.println("response body: " + body);
                });
              })
            .end(Buffer.buffer(
              "{" +
              "\"url\":\"" + feedUrl.getString("url") + "\"" +
              "}"));
          }
        }
      }
    );
  }

  public void refreshFeeds(Long id) {
    System.out.println("worker fired ! id: " + id);
    if (this.httpClient == null) {
      System.out.println("initializing ...");
      initFeeds();
      return ;
    }
    System.out.println("updating ...");

    MongoDB.getInstance().getClient().find(
      "feeds",
      new JsonObject(),
      res -> {
        if (res.succeeded()) {
          List<JsonObject> feeds = res.result();

          if (feeds != null) {
            for (JsonObject feed : feeds) {
              System.out.println("feed url: " + feed.getString("url"));
              this.httpClient.put(4242, "localhost", "/api/feeds/fromURL",
                updateResponse -> {
                  updateResponse.handler(updateBody -> {
                    System.out.println("updateResponse body: " + updateBody);
                  });
                })
              .putHeader("content-type", "application/json; charset=utf-8")
              .end(Buffer.buffer(
                "{" +
                "\"url\":\"" + feed.getString("url") + "\"" +
                "}"));
            }
          }
        }
        else {
          System.out.println("FAIL: " + res.cause().getMessage());
          return ;
        }
      }
    );
  }

  public void setDelay(long delay) {
    this.delay = delay;
  }

  public long getDelay() {
    return this.delay;
  }

  public String toString() {
    return ("delay: " + this.delay);
  }
}
