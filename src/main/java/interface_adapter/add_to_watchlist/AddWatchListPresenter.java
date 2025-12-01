package interface_adapter.add_to_watchlist;

import java.util.List;
import java.util.stream.Collectors;

import entity.WatchList;
import use_case.add_to_watchlist.AddWatchListOutputBoundary;
import use_case.add_to_watchlist.AddWatchListResponseModel;

/**
 * Presenter for the Add to Watch List use case.
 *
 * <p>This class formats interactor output into a form suitable for the UI layer.
 * It updates the {@link AddWatchListViewModel} to reflect the results of the
 * use case and optionally notifies a registered {@link AddWatchListView}.
 */
public class AddWatchListPresenter implements AddWatchListOutputBoundary {

    private AddWatchListView view;
    private final AddWatchListViewModel viewModel;

    /**
     * Construct a presenter given a view interface that can display the results of the Add to Watch List operation.
     *
     * @param viewModel the view model
     */
    public AddWatchListPresenter(AddWatchListViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Registers a UI view that should be notified when results are ready.
     * @param view a UI component
     */
    public void setView(AddWatchListView view) {
        this.view = view;
    }

    /**
     * Returns the view model associated with the presenter.
     * @return the viewmodel
     */
    public AddWatchListViewModel getViewModel() {
        return viewModel;
    }

    /**
     * Converts a list of watchlist entities into UI-ready watchlistoption objects.
     * @param watchLists watchlists belonging to the user
     */
    public void loadWatchListsForUser(List<WatchList> watchLists) {
        final List<WatchListOption> options =
                watchLists.stream()
                        .map(wl -> new WatchListOption(wl.getWatchListId(), wl.getName()))
                        .collect(Collectors.toList());

        viewModel.setWatchListOptions(options);
    }

    @Override
    public void prepareSuccessView(AddWatchListResponseModel responseModel) {
        final String message = responseModel.getMessage();
        viewModel.setResultMessage(message);
        if (view != null) {
            view.showResult(message);
        }
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setResultMessage(errorMessage);
        if (view != null) {
            view.showResult(errorMessage);
        }
    }
}

