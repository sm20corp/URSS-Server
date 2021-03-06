package urss.server.components;

abstract public class ADatabaseClient<CLIENT> {
  protected CLIENT dbClient = null;

  public void setClient(CLIENT client) {
    this.dbClient = client;
  }

  public CLIENT getClient() {
    return this.dbClient;
  }
}
