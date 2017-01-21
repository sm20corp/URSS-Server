package urss.server.api.feed;

import urss.server.components.IModel;
import urss.server.components.AModel;
import urss.server.api.feed.Category;
import urss.server.api.feed.Cloud;
import urss.server.api.feed.Image;
import urss.server.api.feed.TextInput;

public class FeedModel extends AModel<FeedModel> implements IModel<FeedModel> {
  private String title;
  private String link;
  private String description;
  private String language;
  private String copyright;
  private String managingEditor;
  private String webMaster;
  private String pubDate;
  private String lastBuildDate;
  private Category[] categoryArray;
  private String generator;
  private String docs;
  private Cloud cloud;
  private Integer ttl;
  private Image image;
  private TextInput textInput;
  private Integer[] skipHours;
  private String[] skipDays;
  //Add private member array of article


  public FeedModel(String title, String link, String description) {
    this.title = title;
    this.link = link;
    this.description = description;
    this.language = "";
    this.copyright = "";
    this.managingEditor = "";
    this.webMaster = "";
    this.pubDate =  "";
    this.lastBuildDate = "";
    this.categoryArray = null;
    this.generator = "";
    this.docs = "";
    this.cloud = null;
    this.ttl = 0;
    this.image = null;
    this.textInput = null;
    this.skipHours = null;
    this.skipDays = null;
  }

  public String getTitle() {
    return (this.title);
  }
  public String getLink() {
    return (this.link);
  }
  public String getDescription() {
    return (this.description);
  }
  public String getLanguage() {
    return (this.language);
  }
  public String getCopyright() {
    return (this.copyright);
  }
  public String getManagingEditor() {
    return (this.title);
  }
  public String getWebMaster() {
    return (this.webMaster);
  }
  public String getPubDate() {
    return (this.pubDate);
  }
  public String getLastBuildDate() {
    return (this.lastBuildDate);
  }
  public Category[] getCategoryArray() {
    return (this.categoryArray);
  }
  public String getGenerator() {
    return (this.generator);
  }
  public String getDocs() {
    return (this.docs);
  }
  public Integer getTtl() {
    return (this.ttl);
  }
  public Image getImage() {
    return (this.image);
  }
  public TextInput getTextInput() {
    return (this.textInput);
  }
  public Integer[] getSkipHours() {
    return (this).skipHours;
  }
  public String[] getSkipDays() {
    return (this.skipDays);
  }

  @Override
  public Boolean validate() {

    if (!this.getTitle().isEmpty() && !this.getDescription().isEmpty() && !this.getLink().isEmpty())
      return (false);
    return true;
  }

}
