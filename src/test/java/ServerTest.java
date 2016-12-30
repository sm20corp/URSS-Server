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
  public void testMyApplication(TestContext context) {
    final Async async = context.async();

    vertx.createHttpClient().post(4242, "localhost", "/auth/local/",
                                    response -> {
      response.handler(body -> {
        System.out.println("response: " + body);
//        context.assertTrue(body.toString().contains("credential"));
        async.complete();
      });
    })
    .end(Buffer.buffer("{" +
                        "\"email\":\"ouloulou@yopmail.com\"," +
                        "\"password\":\"abc123\"," +
                        "\"role\":\"admin\"" +
                       "}"));
  }
}
