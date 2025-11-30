package interface_adapter.view_watchlists;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewModel;

/**
 * The ViewModel for the View Watch Lists view.
 *
 * <p>Stores UI-specific data such as the username and selected index,
 * and notifies observers when the state changes.
 */
public class ViewWatchListsViewModel extends ViewModel<ViewWatchListsState> {

    private final ViewManagerModel viewManagerModel;
    private String username;
    private int selectedIndex;

    /**
     * Creates the ViewWatchListViewModel with initial empty state.
     *
     * @param viewManagerModel the application-wide view manager
     */
    public ViewWatchListsViewModel(ViewManagerModel viewManagerModel) {
        super("view watchlists");
        this.viewManagerModel = viewManagerModel;
        setState(new ViewWatchListsState());
    }

    /**
     * Returns the applications ViewManagerModel, used to switch views.
     *
     * @return the {@link ViewManagerModel}
     */
    public ViewManagerModel getViewManagerModel() {
        return viewManagerModel;
    }

    /**
     * Returns the username whose watchlists are currently being displayed.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username associated with this view.
     *
     * @param username the username to store
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the currently selected index from the watchlist dropdown.
     *
     * @return the selected index
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Sets the selected index of the watchlist dropdown for this view.
     *
     * @param selectedIndex the index chosen by the user
     */
    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}
