package urss.server.api.feed;

public class Cloud {
  private String domain;
  private Integer port;
  private String path;
  private String registerProcedure;
  private String protocol;

  public Cloud(String domain, Integer port, String path, String registerProcedure, String protocol) {
    setDomain(domain);
    setPort(port);
    setPath(path);
    setRegisterProcedure(registerProcedure);
    setProtocol(protocol);
  }

  public String getDomain() {
    return (this.domain);
  }
  public void setDomain(String domain) {
    this.domain = domain;
  }
  public Integer getPort() {
    return (this.port);
  }
  public void setPort(Integer port) {
    this.port = port;
  }
  public String getPath() {
    return (this.path);
  }
  public void setPath(String path) {
    this.path = path;
  }
  public String getRegisterProcedure() {
    return (this.registerProcedure);
  }
  public void setRegisterProcedure(String registerProcedure) {
    this.registerProcedure = registerProcedure;
  }
  public String getProtocol() {
    return (this.protocol);
  }
  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  @Override
  public String toString() {
    return "domain: " + getDomain() + " -  port: " + getPort() + " -  path: " + getPath()
     + " -  registerProcedure: " + getRegisterProcedure()  + " -  protocol: " + getProtocol();
  }
}
