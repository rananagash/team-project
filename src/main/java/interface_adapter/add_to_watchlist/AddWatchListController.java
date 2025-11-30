package interface_adapter.add_to_watchlist;

import use_case.add_to_watchlist.AddWatchListInputBoundary;
import use_case.add_to_watchlist.AddWatchListRequestModel;

/**
 * Controller for the Add to Watch List use case.
 *
 * <p>This class gathers required input data, constructs a {@link AddWatchListRequestModel},
 * and passes it to the interactor via the input boundary.</p>
 */
public class AddWatchListController {

    private final AddWatchListInputBoundary interactor;
    private final AddWatchListPresenter presenter;

    /**
     * Creates a controller for the Add to Watch List use case.
     *
     * @param interactor the input boundary
     * @param presenter the presenter used for output
     */
    public AddWatchListController(AddWatchListInputBoundary interactor,
                                  AddWatchListPresenter presenter) {
        this.interactor = interactor;
        this.presenter = presenter;
    }

    /**
     * Requests to add the specified movie to the given user's watch list.
     *
     * @param username the user who owns the watch list
     * @param movieId the movie to add
     * @param watchListId the target watch list
     */
    public void addMovieToWatchList(String username, String movieId, String watchListId) {
        final AddWatchListRequestModel requestModel = new AddWatchListRequestModel(username, movieId, watchListId);
        interactor.execute(requestModel);
    }

    /**
     * Exposes the presenter so a view can register itself.
     * @return presenter
     */
    public AddWatchListPresenter getPresenter() {
        return presenter;
    }
}
