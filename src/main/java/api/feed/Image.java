package urss.server.api.feed;

public class Image {
  private String url;
  private String title;
  private Integer width;
  private Integer height;
  private String description;

  public Image(String url, String title, Integer width, Integer height, String description) {
    setUrl(url);
    setTitle(title);
    setWidth(width);
    setHeight(height);
    setDescription(description);
  }

  public String getUrl() {
    return (this.url);
  }
  public void setUrl(String url) {
    this.url = url;
  }
  public String getTitle() {
    return (this.title);
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public Integer getWidth() {
    return (this.width);
  }
  public void setWidth(Integer width) {
    this.width = width;
  }
  public Integer getHeight() {
    return (this.height);
  }
  public void setHeight(Integer height) {
    this.height = height;
  }
  public String getDescription() {
    return (this.description);
  }
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "url: " + getUrl() + " -  title: " + getTitle() + " -  width: " + getWidth()
     + " -  height: " + getHeight()  + " -  description: " + getDescription();
  }
}
