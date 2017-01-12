package urss.server.auth;


import java.util.ArrayList;
import java.util.List;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.JWTAuthHandler;
import java.net.HttpURLConnection;

import urss.server.Server;
import urss.server.components.MongoDB;
import urss.server.components.JsonHandler;
import urss.server.api.credential.CredentialModel;

public class AuthService {
  private static AuthService instance = null;
  private JWTAuth jwt = null;
  private Logger logger;

  private AuthService() {
    setJWT(JWTAuth.create(Server.getInstance().getVertx(), new JsonObject()
        .put("keyStore", new JsonObject()
          .put("type", "jceks")
          .put("path", "keystore.jceks")
          .put("password", System.getenv().get("SECRET")))));
    setLogger(LoggerFactory.getLogger(AuthService.class.getName()));
  }

  public static AuthService getInstance() {
    if (instance == null) {
      instance = new AuthService();
    }
    return instance;
  }

  public JWTAuthHandler generateHandler(String endpoint) {
    return JWTAuthHandler.create(getJWT(), endpoint);
  }

  private String generateToken(String id, String role) {
    List<String> authorities = new ArrayList<>();

    authorities.add("role:" + role);

    return getJWT().generateToken(new JsonObject().put("userId", id).put("role", role), new JWTOptions().setExpiresInSeconds(60L).setPermissions(authorities));
  }

  public void hasAuthority(RoutingContext ctx) {
    ctx.user().isAuthorised("role:user", res -> {
      if (res.succeeded()) {
        boolean hasAuthority = res.result();

        ctx.put("isAdmin", hasAuthority);
        ctx.next();
      }
      else {
        getLogger().info("FAIL: " + res.cause().getMessage());
        ctx.fail(HttpURLConnection.HTTP_INTERNAL_ERROR);
        return ;
      }
    });
  }

  public void authLocal(RoutingContext ctx) {
    getLogger().info("params: " + ctx.request().params());
    getLogger().info("body: " + ctx.getBodyAsJson());

    JsonObject body = ctx.getBodyAsJson();

    // check if the credential exists in database
    MongoDB.getInstance().getClient()
    .findOne(
      "credentials",
      new JsonObject()
      .put("email", body.getString("email")),
      new JsonObject(),
      credentialRes -> {
        if (credentialRes.succeeded()) {
          JsonObject credential = credentialRes.result();
          getLogger().info("credential: " + credential);

          if (credential == null) {
            ctx.response()
            .setStatusCode(HttpURLConnection.HTTP_NOT_FOUND)
            .putHeader("content-type", "application/json; charset=utf-8")
            .end(new JsonObject().put("message", "no credential matched your email").encodePrettily());
            return ;
          }

          // check if the credential is linked to a user
          /*
          MongoDB.getInstance().getClient()
          .findOne(
            "users",
            new JsonObject()
            .put("credential", credential.getString("_id")),
            new JsonObject()
            .put("_id", true),
            userRes -> {
              if (userRes.succeeded()) {
                getLogger().info("role: " + credential.getString("role"));
                JsonObject user = userRes.result();

                if (user == null) {
                  getLogger().info("credential is not linked to a user");
                  ctx.response()
                  .setStatusCode(HttpURLConnection.HTTP_NOT_FOUND)
                  .putHeader("content-type", "application/json; charset=utf-8")
                  .end(new JsonObject().put("message", "credential is not linked to a user").encodePrettily());
                  return ;
                }
                getLogger().info("user: " + user);
                */
                // check if the password is fine
                CredentialModel credentialModel = JsonHandler.getInstance().fromJson(credential.toString(), CredentialModel.class);
                if (!credentialModel.authenticate(body.getString("password"))) {
                  getLogger().info("password doesn't match");
                  ctx.fail(HttpURLConnection.HTTP_UNAUTHORIZED);
                  return ;
                }

                // update last connected field
                /*
                MongoDB.getInstance().getClient()
                .update(
                  "users",
                  new JsonObject().put("_id", user.getString("_id")),
                  new JsonObject().put("$currentDate", new JsonObject().put("lastConnected", true)),
                  res -> {
                    if (res.succeeded()) {
                      System.out.println("updated: " + res.result());
                    }
                    else {
                      getLogger().info("FAIL: " + res.cause().getMessage());
                    }
                  }
                );
                */

                // generate token with user's id and role
                ctx.response()
                .setStatusCode(HttpURLConnection.HTTP_OK)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(new JsonObject()
                  .put("token", generateToken(/*user*/credential.getString("_id"), credential.getString("role")))
                  .put("userId", /*user*/credential.getString("_id"))
                  .encodePrettily()
                );
                return ;
                /*
              }
              else {
                getLogger().info("FAIL: " + userRes.cause().getMessage());
                ctx.fail(HttpURLConnection.HTTP_INTERNAL_ERROR);
                return ;
              }
            }
          );
          */
        }
        else {
          getLogger().info("FAIL: " + credentialRes.cause().getMessage());
          ctx.fail(HttpURLConnection.HTTP_INTERNAL_ERROR);
          return ;
        }
      }
    );
  }

  private void setJWT(JWTAuth jwt) {
    this.jwt = jwt;
  }

  private JWTAuth getJWT() {
    return this.jwt;
  }

  private void setLogger(Logger logger) {
    this.logger = logger;
  }

  private Logger getLogger() {
    return this.logger;
  }
}
