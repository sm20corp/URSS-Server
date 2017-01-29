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
      initFeeds();
    }
    else {
      System.out.println("updating ...");
      this.httpClient.get(4242, "localhost", "/api/feeds",
        response -> {
          response.bodyHandler(body -> {
            System.out.println("handler fired");
            JsonArray feeds = body.toJsonArray();
            for (int i = 0; i < feeds.size(); i++) {
              JsonObject feed = feeds.getJsonObject(i);

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
          });
        })
      .end();
    }
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
