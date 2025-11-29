package interface_adapter.view_watchlists;

import interface_adapter.ViewManagerModel;
import use_case.view_watchlists.ViewWatchListsOutputBoundary;
import use_case.view_watchlists.ViewWatchListsResponseModel;

/**
 * Presenter for the View Watch List use case.
 */
public class ViewWatchListsPresenter implements ViewWatchListsOutputBoundary {

    private final ViewWatchListsViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    /**
     * Construct a presenter for the View Watch List use case.
     *
     * @param viewModel the View Watchlist View Model
     * @param viewManagerModel the general View Manager Model
     */
    public ViewWatchListsPresenter(ViewWatchListsViewModel viewModel,
                                   ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Set the view for the success view in the View Watch List use case.
     *
     * @param responseModel the response model
     */
    @Override
    public void prepareSuccessView(ViewWatchListsResponseModel responseModel) {
        final ViewWatchListsState state = new ViewWatchListsState();
        state.setWatchlists(responseModel.getWatchLists());
        state.setSelectedIndex(responseModel.getSelectedIndex());
        state.setMovies(responseModel.getMovies());
        state.setUsername(responseModel.getUsername());
        state.setError(null);

        viewModel.setSelectedIndex(responseModel.getSelectedIndex());
        viewModel.setUsername(responseModel.getUsername());
        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

    /**
     * Set the failure view for the View Watch List use case.
     *
     * @param message human-readable error message
     */
    @Override
    public void prepareFailureView(String message) {
        final ViewWatchListsState state = new ViewWatchListsState();
        state.setError(message);

        viewModel.setState(state);
        viewModel.firePropertyChange();
    }
}
