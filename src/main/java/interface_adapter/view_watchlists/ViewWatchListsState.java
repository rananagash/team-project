package interface_adapter.view_watchlists;

import java.util.List;

import use_case.view_watchlists.ViewWatchListsResponseModel;

/**
 * The state object for the View Watch Lists view.
 * Stores data needed by teh UI, including available watchlists,
 * the currently selected watchlist index, the movies within that watchlist,
 * and any error messages.
 */
public class ViewWatchListsState {

    private List<ViewWatchListsResponseModel.WatchListInfo> watchlists;
    private int selectedIndex;
    private List<ViewWatchListsResponseModel.MovieInfo> movies;
    private String error;
    private String username;

    // getters & setters

    /**
     * Returns the list of watchlists available to the user.
     * @return the list of watchlists
     */
    public List<ViewWatchListsResponseModel.WatchListInfo> getWatchlists() {
        return watchlists;
    }

    /**
     * Sets the list of available watchlists.
     *
     * @param watchlists the list of watchlists to display
     */
    public void setWatchlists(List<ViewWatchListsResponseModel.WatchListInfo> watchlists) {
        this.watchlists = watchlists;
    }

    /**
     * Returns the index of the currently selected watchlist.
     *
     * @return the selected watchlist index
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Sets the index of the selected watchlist.
     *
     * @param selectedIndex the watchlist index selected by the user
     */
    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    /**
     * Returns the list of movies for the selected watchlist.
     *
     * @return the list of movies
     */
    public List<ViewWatchListsResponseModel.MovieInfo> getMovies() {
        return movies;
    }

    /**
     * Sets the list of movies belonging to the selected watchlist.
     *
     * @param movies the movies to display
     */
    public void setMovies(List<ViewWatchListsResponseModel.MovieInfo> movies) {
        this.movies = movies;
    }

    /**
     * Returns any error message stored in this state.
     * @return the error message, or {@code null} if none
     */
    public String getError() {
        return error;
    }

    /**
     * Sets an error message for the view to display.
     *
     * @param error the human-readable error-message
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Sets the username associated with these watchlists.
     *
     * @param username the user whose watchlists are being displayed
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the username associated with these watchlists.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }
}
