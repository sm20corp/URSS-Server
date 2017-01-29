package urss.server.api.user;

import urss.server.components.IModel;
import urss.server.components.AModel;

public class UserModel extends AModel<UserModel> implements IModel<UserModel> {
  private static final String defaultCredential = "";
  private static final String defaultHistory = "";
//  private static final String defaultLastConnected = "";
  public static final String[] requiredFields = { "credential", "history"/*, "lastConnected"*/ };
  public static final String[] optionalFields = {  };
  private String credential;
  private String history;
//  private String lastConnected;

  public UserModel(String credential, String history) {
    setCredential(credential);
    setHistory(history);
  }

  public void setCredential(String credential) {
    this.credential = credential;
  }

  public String getCredential() {
    return this.credential;
  }

  public void setHistory(String history) {
    this.history = history;
  }

  public String getHistory() {
    return this.history;
  }

  /*
  public void setLastConnected(String lastConnected) {
    this.lastConnected = lastConnected;
  }

  public String getLastConnected() {
    return this.lastConnected;
  }
  */

  @Override
  public String toString() {
    return ("credential: " + getCredential() +
            " - history: " + getHistory()/* +
            " - lastConnected: " + getLastConnected()*/);
  }

  @Override
  public Boolean validate() {
    return true;
  }
}
