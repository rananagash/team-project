package view;

import entity.Movie;
import java.util.List;

public interface MovieSearchView {
    void showLoading();
    void hideLoading();
    void showSearchResults(List<Movie> movies);
    void showError(String message);
    void showSearchScreen();
    void updateWatchlistButtons();
    String getSearchQuery();
    void clearSearchQuery();
    void showProfile();
}
