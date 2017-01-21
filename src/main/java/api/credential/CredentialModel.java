package urss.server.api.credential;

import org.mindrot.jbcrypt.BCrypt;

import urss.server.components.IModel;
import urss.server.components.AModel;

public class CredentialModel extends AModel<CredentialModel> implements IModel<CredentialModel> {
  private static final String defaultEmail = "";
  private static final String defaultPassword = "";
  private static final String defaultRole = "user";
  private static final int bcryptLogRounds = 12;
  public static final String[] requiredFields = { "email", "password" };
  public static final String[] optionalFields = { "role" };
  private String email;
  private String password;
  private String role;

  public CredentialModel() {
    setEmail(CredentialModel.defaultEmail.toLowerCase());
    setPassword(CredentialModel.defaultPassword);
    setRole(CredentialModel.defaultRole);
  }

  public CredentialModel(String email, String password) {
    setEmail(email.toLowerCase());
    setPassword(password);
    setRole(CredentialModel.defaultRole);
  }

  public CredentialModel(String email, String password, String role) {
    setEmail(email.toLowerCase());
    setPassword(password);
    setRole(role);
  }

  public Boolean authenticate(String password) {
    return BCrypt.checkpw(password, getPassword());
  }

  public static String encryptPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt(CredentialModel.bcryptLogRounds));
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

  public void setRole(String role) {
    this.role = role;
  }

  public String getRole() {
    return this.role;
  }

  @Override
  public String toString() {
    return ("email: " + getEmail() +
            " - password: " + getPassword() +
            " - role: " + getRole());
  }

  @Override
  public Boolean validate() {
    setPassword(encryptPassword(getPassword()));
    return true;
  }
}
