package interface_adapter.view_watchlists;

import interface_adapter.ViewManagerModel;
import use_case.view_watchlists.ViewWatchListsInputBoundary;
import use_case.view_watchlists.ViewWatchListsRequestModel;

/**
 * Controller for the View Watch List use case.
 *
 * <p>This class gathers required input data, constructs a {@link ViewWatchListsRequestModel},
 * and passes it to the interactor via the input boundary.
 */
public class ViewWatchListsController {

    private final ViewWatchListsInputBoundary interactor;
    private final ViewManagerModel viewManagerModel;

    /**
     * Creates a controller for the View Watch List use case.
     *
     * @param interactor the input boundary
     * @param viewManagerModel the viewManagerModel
     */
    public ViewWatchListsController(ViewWatchListsInputBoundary interactor,
                                    ViewManagerModel viewManagerModel) {
        this.interactor = interactor;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Requests to view the authenticated user's watch lists, and specifies an index of which
     * list to display first.
     *
     * @param username the username of the user to retrieve watch lists
     * @param selectedIndex the index of which watch list to display first
     */
    public void execute(String username, int selectedIndex) {
        interactor.execute(new ViewWatchListsRequestModel(username, selectedIndex));
        viewManagerModel.setState("view watchlists");
        viewManagerModel.firePropertyChange();
    }

    /**
     * Request to view the authenticated user's watch lists, and defaults to displaying the
     * first watch list in their account.
     *
     * @param username the username of the user to retrieve watch lists
     */
    public void execute(String username) {
        interactor.execute(new ViewWatchListsRequestModel(username, 0));
        viewManagerModel.setState("view watchlists");
        viewManagerModel.firePropertyChange();
    }
}
