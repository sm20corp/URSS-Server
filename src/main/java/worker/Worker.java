package urss.server.worker;

import java.util.List;

import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;
import io.vertx.core.buffer.Buffer;

import urss.server.Server;
import urss.server.components.MongoDB;

public class Worker {
  private static final long defaultDelay = 10000;//10 seconds
  private long delay;
  private HttpClient httpClient = null;

  public Worker() {
    this(Worker.defaultDelay);
  }

  public Worker(long delay) {
    setDelay(delay);
  }

  public void initFeeds() {
    if (httpClient == null) {
      this.httpClient = Server.getInstance().getVertx().createHttpClient();
    }

    MongoDB.getInstance().getClient().find(
      "urls",
      new JsonObject(),
      res -> {
        if (res.succeeded()) {
          List<JsonObject> results = res.result();

          for (JsonObject feedUrl : results) {
            System.out.println("meh: " + feedUrl);
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

    this.httpClient.get(4242, "localhost", "/api/feeds",
      response -> {
        response.handler(body -> {
          System.out.println("response body: " + body);
        });
      })
    .end();
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
