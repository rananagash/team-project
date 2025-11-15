package interface_adapter.add_to_watchlist;

import use_case.add_to_watchlist.AddWatchListOutputBoundary;
import use_case.add_to_watchlist.AddWatchListResponseModel;

/**
 * Presenter for the Add to Watch List use case.
 *
 * This class receives data from the interactor and delegates UI updates to a {@link AddWatchListView}.
 */
public class AddWatchListPresenter implements AddWatchListOutputBoundary {

    private final AddWatchListView view;

    /**
     * Construct a presenter given a view interface that can display the results of the Add to Watch List operation.
     * @param view view the UI interface used to display the results
     */
    public AddWatchListPresenter(AddWatchListView view) {
        this.view = view;
    }

    @Override
    public void prepareSuccessView(AddWatchListResponseModel responseModel) {
        view.showResult(responseModel.getMessage());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        view.showResult(errorMessage);
    }
}

