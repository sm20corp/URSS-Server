package urss.server.api.history;

import java.util.List;
import java.util.ArrayList;

import urss.server.components.IModel;
import urss.server.components.AModel;

public class HistoryModel extends AModel<HistoryModel> implements IModel<HistoryModel> {
  public static final String[] requiredFields = {  };
  public static final String[] optionalFields = { "bookmarks", "viewedArticles", "starredArticles" };
  private List<String> bookmarks;
  private List<String> viewedArticles;
  private List<String> starredArticles;

  public HistoryModel() {
    this(new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
  }

  public HistoryModel(List<String> bookmarks, List<String> viewedArticles, List<String> starredArticles) {
    setBookmarks(bookmarks);
    setViewedArticles(viewedArticles);
    setStarredArticles(starredArticles);
  }

  public void setBookmarks(List<String> bookmarks) {
    this.bookmarks = bookmarks;
  }

  public List<String> getBookmarks() {
    return this.bookmarks;
  }

  public void setViewedArticles(List<String> viewedArticles) {
    this.viewedArticles = viewedArticles;
  }

  public List<String> getViewedArticles() {
    return this.viewedArticles;
  }

  public void setStarredArticles(List<String> starredArticles) {
    this.starredArticles = starredArticles;
  }

  public List<String> getStarredArticles() {
    return this.starredArticles;
  }

  @Override
  public String toString() {
    return ("bookmaks: " + getBookmarks() +
            " - viewedArticles: " + getViewedArticles() +
            " - starredArticles: " + getStarredArticles());
  }

  @Override
  public Boolean validate() {
    return true;
  }
}
