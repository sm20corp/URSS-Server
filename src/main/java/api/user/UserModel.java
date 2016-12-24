package urss.server;

public class UserModel {
  private static final String defaultEmail = "";
  private static final String defaultPassword = "";
  private String email;
  private String password;

  public UserModel() {
    setEmail(UserModel.defaultEmail);
    setPassword(UserModel.defaultPassword);
  }

  public UserModel(String email, String password) {
    setEmail(email);
    setPassword(password);
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return this.email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return this.password;
  }

  @Override
  public String toString() {
    return "email: " + getEmail() + " -  password: " + getPassword();
  }
}
