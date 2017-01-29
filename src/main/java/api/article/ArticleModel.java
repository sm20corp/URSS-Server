package urss.server.api.article;

import urss.server.components.IModel;
import urss.server.components.AModel;

/*
attributes not implemented
  {
      category: [ {
          name: String,
          domain: String
      } ],
      guid: {
          id: String,
          isPermaLink: Boolean
      }
      source: {
          name: String,
          url: String
      }
  }
  */

public class ArticleModel extends AModel<ArticleModel> implements IModel<ArticleModel> {
  private static final String defaultFeedId = "";
  private static final String defaultTitle = "";
  private static final String defaultLink = "";
  private static final String defaultDescription = "";
  private static final String defaultPubDate = "";
  private static final String defaultAuthor = "";
  private static final String defaultComments = "";
  private static final String defaultEnclosureUrl = "";
  private static final long defaultEnclosureLength = 0;
  private static final String defaultEnclosureType = "";
  public static final String[] requiredFields = { "feedId", "title", "link",
                                                  "description", "pubDate" };
  public static final String[] optionalFields = { "author", "comments", "enclosureUrl",
                                                  "enclosureLength", "enclosureType"};
  private String feedId;
  private String title;
  private String link;
  private String description;
  private String pubDate;
  private String author;
  private String comments;
  private String enclosureUrl;
  private long enclosureLength;
  private String enclosureType;

  public ArticleModel() {
    this(defaultFeedId, defaultTitle, defaultLink, defaultDescription,
         defaultPubDate, defaultAuthor, defaultComments,
         defaultEnclosureUrl, defaultEnclosureLength, defaultEnclosureType);
  }

  public ArticleModel(String feedId, String title, String link,
                      String description, String pubDate) {
    this(feedId, title, link, description, pubDate, defaultAuthor,
         defaultComments, defaultEnclosureUrl, defaultEnclosureLength,
         defaultEnclosureType);
  }

  public ArticleModel(String feedId, String title, String link,
                      String description, String pubDate, String author) {
    this(feedId, title, link, description, pubDate, author,
         defaultComments, defaultEnclosureUrl, defaultEnclosureLength,
         defaultEnclosureType);
  }

  public ArticleModel(String feedId, String title, String link,
                      String description, String pubDate, String author,
                      String comments, String enclosureUrl, long enclosureLength,
                      String enclosureType) {
    setFeedId(feedId);
    setTitle(title);
    setLink(link);
    setDescription(description);
    setPubDate(pubDate);
    setAuthor(author);
    setComments(comments);
    setEnclosureUrl(enclosureUrl);
    setEnclosureLength(enclosureLength);
    setEnclosureType(enclosureType);
  }

  public void setFeedId(String feedId) {
    this.feedId = feedId;
  }

  public String getFeedId() {
    return this.feedId;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return this.title;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getLink() {
    return this.link;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return this.description;
  }

  public void setPubDate(String pubDate) {
    this.pubDate = pubDate;
  }

  public String getPubDate() {
    return this.pubDate;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getAuthor() {
    return this.author;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public String getComments() {
    return this.comments;
  }

  public void setEnclosureUrl(String url) {
    this.enclosureUrl = url;
  }

  public String getEnclosureUrl() {
    return this.enclosureUrl;
  }

  public void setEnclosureLength(long length) {
    this.enclosureLength = length;
  }

  public long getEnclosureLength() {
    return this.enclosureLength;
  }

  public void setEnclosureType(String type) {
    this.enclosureType = type;
  }

  public String getEnclosureType() {
    return this.enclosureType;
  }

  @Override
  public String toString() {
    return ("feedId: " + getFeedId() +
            " - title: " + getTitle() +
            " - link: " + getLink() +
            " - description: " + getDescription() +
            " - pubDate: " + getPubDate() +
            " - author: " + getAuthor() +
            " - comments: " + getComments() +
            " - enclosureUrl: " + getEnclosureUrl() +
            " - enclosureLength: " + getEnclosureLength() +
            " - enclosureType: " + getEnclosureType());
  }

  @Override
  public Boolean validate() {
    return true;
  }
}
