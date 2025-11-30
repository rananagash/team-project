package use_case.add_to_watchlist;

import entity.Movie;
import entity.User;
import entity.WatchList;
import use_case.common.MovieGateway;
import use_case.common.UserDataAccessInterface;

/**
 * Interactor for the Add to Watch List use case.
 *
 * <p>This class implements the use case application logic:
 * <ul>
 *     <li>Attempts to add a movie to a Watch List</li>
 *     <li>Detect if movie was already on Watch List</li>
 *     <li>Create a response model describing the result</li>
 *     <li>Persists state changes via {@link data_access.CachedUserDataAccessObject}</li>
 *     <li>Call the appropriate presenter method</li>
 * </ul>
 */
public class AddWatchListInteractor implements AddWatchListInputBoundary {

    private final AddWatchListOutputBoundary presenter;
    private final UserDataAccessInterface userDataAccessObject;
    private final MovieGateway movieDataAccessObject;

    /**
     * Constructs an interactor with a presenter that will translate the results.
     * @param presenter the output boundary used to display outcomes
     * @param userDataAccessObject persistence gateway used to save changes
     * @param movieDataAccessObject the movie gateway
     */
    public AddWatchListInteractor(AddWatchListOutputBoundary presenter,
                                  UserDataAccessInterface userDataAccessObject,
                                  MovieGateway movieDataAccessObject) {
        this.presenter = presenter;
        this.userDataAccessObject = userDataAccessObject;
        this.movieDataAccessObject = movieDataAccessObject;
    }

    /**
     * Executes the Add to Watch logic.
     *
     * <p>This method:
     * <ul>
     *     <li>Retrieves the entities from the request model</li>
     *     <li>Attempts to add the movie to the watch list</li>
     *     <li>Saves the user if the addition succeeded</li>
     *     <li>Builds a response model</li>
     *     <li>Routes the response through the presenter</li>
     * </ul>
     * @param requestModel contains the user, watch list, and movie to be added
     */
    @Override
    public void execute(AddWatchListRequestModel requestModel) {
        // Retrieve user
        final String username = requestModel.getUsername();
        final User user = userDataAccessObject.getUser(username);

        if (user == null) {
            presenter.prepareFailView("User not found: " + username);
            return;
        }

        // Retrieve user's watchlist
        final String watchListId = requestModel.getWatchListId();
        final WatchList watchList = user.getWatchListById(watchListId);

        if (watchList == null) {
            presenter.prepareFailView("WatchList not found: " + watchListId);
            return;
        }

        // Retrieve the movie
        final String movieId = requestModel.getMovieId();
        final Movie movie = movieDataAccessObject.findById(movieId).orElse(null);

        if (movie == null) {
            presenter.prepareFailView("Movie not found: " + movieId);
            return;
        }

        // Attempt adding to list
        final boolean added = watchList.addMovie(movie);

        final boolean success;
        final String message;

        if (added) {
            // Persist updated user
            userDataAccessObject.save(user);

            success = true;
            message = movie.getTitle() + " successfully added to " + watchList.getName() + "!";
        }
        else {
            success = false;
            message = movie.getTitle() + " is already in " + watchList.getName() + ".";
        }

        final AddWatchListResponseModel responseModel = new AddWatchListResponseModel(success, message);
        if (success) {
            presenter.prepareSuccessView(responseModel);
        }
        else {
            presenter.prepareFailView(message);
        }
    }
}
