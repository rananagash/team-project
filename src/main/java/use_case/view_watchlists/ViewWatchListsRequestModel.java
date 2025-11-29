package use_case.view_watchlists;

/**
 * Request model for the View Watch List use case.
 * Encapsulates the username and (optionally) the selected watchlist index.
 */
public class ViewWatchListsRequestModel {
    private final String username;
    private final int selectedIndex;

    /**
     * Creates a request with a username and selected watchlist index.
     *
     * @param username the user whose watchlists should be viewed
     * @param selectedIndex the index of the watchlist to display
     */
    public ViewWatchListsRequestModel(String username, int selectedIndex) {
        this.username = username;
        this.selectedIndex = selectedIndex;
    }

    /**
     * Creates a request with a username, and defaults the watchlist index to 0.
     *
     * @param username the user whose watchlists should be viewed
     */
    public ViewWatchListsRequestModel(String username) {
        this(username, 0);
    }

    /**
     * Returns the username associated with the request.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the selected watchlist index.
     *
     * @return the index, default to 0 if unspecified
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }
}
