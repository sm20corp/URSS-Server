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
      enclosure: {
          url: String,
          length: Number,
          type: String
      },
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
  public static final String[] requiredFields = { "feedId", "title", "link",
                                                  "description", "pubDate" };
  public static final String[] optionalFields = { "author", "comments" };
  private String feedId;
  private String title;
  private String link;
  private String description;
  private String pubDate;
  private String author;
  private String comments;

  public ArticleModel() {
    ArticleModel(defaultFeedId, defaultTitle, defaultLink, defaultDescription,
                 defaultPubDate, defaultAuthor, defaultComments);
  }

  public ArticleModel(String feedId, String title, String link,
                      String description, String pubDate) {
    ArticleModel(feedId, title, link, description, pubDate,
                 defaultAuthor, defaultComments);
  }

  public ArticleModel(String feedId, String title, String link,
                      String description, String pubDate, String author) {
    ArticleModel(feedId, title, link, description, pubDate, author,
                 defaultComments);
  }

  public ArticleModel(String feedId, String title, String link,
                      String description, String pubDate, String author,
                      String comments) {
    setFeedId(feedId);
    setTitle(title);
    setLink(link);
    setDescription(description);
    setPubDate(pubDate);
    setAuthor(author);
    setComments(comments);
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

  @Override
  public String toString() {
    return ("feedId: " + getFeedId() +
            " - title: " + getTitle() +
            " - link: " + getLink() +
            " - description: " + getDescription() +
            " - pubDate: " + getPubDate() +
            " - author: " + getAuthor() +
            " - comments: " + getComments());
  }

  @Override
  public Boolean validate() {
    return true;
  }
}
