package urss.server.api.article;

import urss.server.components.IModel;
import urss.server.components.AModel;

/*
attributes not implemented
  {
      author: String,
      category: [ {
          name: String,
          domain: String
      } ],
      comments: String,
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
  private String feedId;
  private String title;
  private String link;
  private String description;
  private String pubDate;

  public ArticleModel() {
    setFeedId(ArticleModel.defaultFeedId);
    setTitle(ArticleModel.defaultTitle);
    setLink(ArticleModel.defaultLink);
    setDescription(ArticleModel.defaultDescription);
    setPubDate(ArticleModel.defaultPubDate);
  }

  public ArticleModel(String feedId, String title, String link,
                      String description, String pubDate) {
    setFeedId(feedId);
    setTitle(title);
    setLink(link);
    setDescription(description);
    setPubDate(pubDate);
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

  @Override
  public String toString() {
    return ("feedId: " + getFeedId() +
            " - title: " + getTitle() +
            " - link: " + getLink() +
            " - description: " + getDescription() +
            " - pubDate: " + getPubDate());
  }

  @Override
  public Boolean validate() {
    return true;
  }
}
