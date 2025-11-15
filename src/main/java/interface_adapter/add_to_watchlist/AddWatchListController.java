package interface_adapter.add_to_watchlist;

import entity.Movie;
import entity.User;
import entity.WatchList;
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

    /**
     * Creates a controller for the Add to Watch List use case.
     *
     * @param interactor the input boundary
     */
    public AddWatchListController(AddWatchListInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Requests to add the specified movie to the given user's watch list.
     *
     * @param user the user who owns the watch list
     * @param movie the movie to add
     * @param watchList the target watch list
     */
    public void addMovieToWatchList(User user, Movie movie, WatchList watchList) {
        AddWatchListRequestModel requestModel = new AddWatchListRequestModel(user, movie, watchList);
        interactor.execute(requestModel);
    }
}
