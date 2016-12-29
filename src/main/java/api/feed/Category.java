package urss.server.api.feed;

public class Category {
  private String name;
  private String domain;

  public Category(String name, String domain) {
    setName(name);
    setDomain(domain);
  }

  public String getName() {
    return (this.name);
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getDomain() {
    return (this.domain);
  }
  public void setDomain(String domain) {
    this.domain = domain;
  }

  @Override
  public String toString() {
    return "name: " + getName() + " -  domain: " + getDomain();
  }
}
