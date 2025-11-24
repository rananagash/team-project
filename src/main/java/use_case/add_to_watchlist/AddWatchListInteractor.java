package use_case.add_to_watchlist;

import entity.Movie;
import entity.User;
import entity.WatchList;
import use_case.common.UserGateway;

/**
 * Interactor for the Add to Watch List use case.
 *
 * <p>This class implements the use case application logic:
 * <ul>
 *     <li>Attempts to add a movie to a Watch List</li>
 *     <li>Detect if movie was already on Watch List</li>
 *     <li>Create a response model describing the result</li>
 *     <li>Persists state changes via {@link UserGateway}</li>
 *     <li>Call the appropriate presenter method</li>
 * </ul>
 */
public class AddWatchListInteractor implements AddWatchListInputBoundary {

    private final AddWatchListOutputBoundary presenter;
    private final UserGateway userGateway;

    /**
     * Constructs an interactor with a presenter that will translate the results.
     * @param presenter the output boundary used to display outcomes
     * @param userGateway persistence gateway used to save changes
     */
    public AddWatchListInteractor(AddWatchListOutputBoundary presenter,
                                  UserGateway userGateway) {
        this.presenter = presenter;
        this.userGateway = userGateway;
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
        final User user = requestModel.getUser();
        final Movie movie = requestModel.getMovie();
        final WatchList watchList = requestModel.getWatchList();

        final boolean success;
        final String message;

        final boolean added = watchList.addMovie(movie);

        if (added) {
            userGateway.save(user);
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

