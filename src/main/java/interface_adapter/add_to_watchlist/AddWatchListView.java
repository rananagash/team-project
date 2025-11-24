package interface_adapter.add_to_watchlist;

/**
 * An interface representing a UI element that can display the result of the "Add to Watch List" use case.
 */
public interface AddWatchListView {

    /**
     * Displays the final result of the add to watchlist operation.
     * @param message a readable message indicating success or failure
     */
    void showResult(String message);
}
