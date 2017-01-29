package urss.server;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class ServerTest {

  private Vertx vertx;

  @Before
  public void setUp(TestContext context) {
    vertx = Vertx.vertx();
    vertx.deployVerticle(Server.class.getName(),
                         context.asyncAssertSuccess());
  }

  @After
  public void tearDown(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void credentialCreate(TestContext context) {
    final Async async = context.async();

    vertx.createHttpClient().post(4242, "79.137.78.39", "/api/credentials/",
                                  response -> {
      context.assertEquals(response.statusCode(), 200);
      async.complete();
    })
    .end(Buffer.buffer("{" +
                        "\"email\":\"test@justicier.com\"," +
                        "\"password\":\"toto123\"" +
                       "}"));
  }

  @Test
  public void articleCreate(TestContext context) {
    final Async async = context.async();

    vertx.createHttpClient().post(4242, "79.137.78.39", "/api/articles/",
                                  response -> {
      context.assertEquals(response.statusCode(), 200);
      async.complete();
    })
    .end(Buffer.buffer("{" +
                        "\"feedId\":\"1234\"," +
                        "\"title\":\"bob\"," +
                        "\"link\":\"google.fr\"," +
                        "\"description\":\"lil info\"," +
                        "\"pubDate\":\"28122006\"," +
                        "\"author\":\"anthony\"," +
                        "\"comments\":\"c cool\"" +
                       "}"));
  }

  @Test
  public void feedCreate(TestContext context) {
    final Async async = context.async();

    vertx.createHttpClient().post(4242, "79.137.78.39", "/api/articles/",
                                  response -> {
      context.assertEquals(response.statusCode(), 200);
      async.complete();
    })
    .end(Buffer.buffer("{" +
                        "\"url\":\"http://feeds.bbci.co.uk/news/world/rss.xml\"," +
                        "\"title\":\"BBC News - World\"," +
                        "\"link\":\"http://www.bbc.co.uk/news/\"," +
                        "\"description\":\"BBC News - World\"," +
                        "\"articles\":[\"28122006\", \"1234\"]" +
                       "}"));
  }
}
