package urss.server.api.feed;

public class TextInput {
  private String title;
  private String description;
  private String name;
  private String link;

  public TextInput(String title, String description, String name, String link) {
    setTitle(title);
    setDescription(description);
    setName(name);
    setLink(link);
  }

  public String getTitle() {
    return (this.title);
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getDescription() {
    return (this.description);
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getName() {
    return (this.name);
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getLink() {
    return (this.link);
  }
  public void setLink(String link) {
    this.link = link;
  }
  @Override
  public String toString() {
    return "title: " + getTitle() + " -  description: " + getDescription() + " -  name: " + getName()
     + " -  link: " + getLink();
  }
}
