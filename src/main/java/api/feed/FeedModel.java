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


  private FeedModel(Builder builder) {
    this.title = builder.title;
    this.link = builder.link;
    this.description = builder.description;
    this.language = builder.language;
    this.copyright = builder.copyright;
    this.managingEditor = builder.managingEditor;
    this.webMaster = builder.webMaster;
    this.pubDate =  builder.pubDate;
    this.lastBuildDate = builder.lastBuildDate;
    this.categoryArray =  builder.categoryArray;
    this.generator = builder.generator;
    this.docs = builder.docs;
    this.cloud = builder.cloud;
    this.ttl = builder.ttl;
    this.image = builder.image;
    this.textInput = builder.textInput;
    this.skipHours = builder.skipHours;
    this.skipDays = builder.skipDays;
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
    return true;
  }

  public static class Builder {
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


    public Builder(String title, String link, String description, String language,
                    String copyright, String managingEditor, String webMaster, String pubDate,
                    String lastBuildDate, Category[] categoryArray, String generator, String docs,
                    Cloud cloud, Integer ttl, Image image, TextInput textInput, Integer[] skipHours,
                    String[] skipDays) {
      this.title = title;
      this.link = link;
      this.description = description;
      this.language = language;
      this.copyright = copyright;
      this.managingEditor = managingEditor;
      this.webMaster = webMaster;
      this.pubDate =  pubDate;
      this.lastBuildDate = lastBuildDate;
      this.categoryArray =  categoryArray;
      this.generator = generator;
      this.docs = docs;
      this.cloud = cloud;
      this.ttl = ttl;
      this.image = image;
      this.textInput = textInput;
      this.skipHours = skipHours;
      this.skipDays = skipDays;
    }

    public Builder title(String title) {
      this.title = title;
      return (this);
    }
    public Builder link(String link) {
      this.link = link;
      return (this);
    }
    public Builder description(String description) {
      this.description = description;
      return (this);
    }
    public Builder language(String language) {
      this.language = language;
      return (this);
    }
    public Builder copyright(String copyright) {
      this.copyright = copyright;
      return (this);
    }
    public Builder managingEditor(String managingEditor) {
      this.title = managingEditor;
      return (this);
    }
    public Builder webMaster(String webMaster) {
      this.webMaster = webMaster;
      return (this);
    }
    public Builder pubDate(String pubDate) {
      this.pubDate = pubDate;
      return (this);
    }
    public Builder lastBuildDate(String lastBuildDate) {
      this.lastBuildDate = lastBuildDate;
      return (this);
    }
    public Builder categoryArray(Category[] categoryArray) {
      this.categoryArray = categoryArray;
      return (this);
    }
    public Builder generator(String generator) {
      this.generator = generator;
      return (this);
    }
    public Builder docs(String docs) {
      this.docs = docs;
      return (this);
    }
    public Builder ttl(Integer ttl) {
      this.ttl = ttl;
      return (this);
    }
    public Builder image(Image image) {
      this.image = image;
      return (this);
    }
    public Builder textInput(TextInput textInput) {
      this.textInput = textInput;
      return (this);
    }
    public Builder skipHours(Integer[] skipHours) {
      this.skipHours = skipHours;
      return (this);
    }
    public Builder skipDays(String[] skipDays) {
      this.skipDays = skipDays;
      return (this);
    }
    public FeedModel build() {
      return (new FeedModel(this));
    }
  }
}
