package use_case.add_to_watchlist;

/**
 * Request model for the Add to Watch List use case.
 *
 * <p>Contains all input data needed by the interactor:
 * <ul>
 *     <li>The user performing the action</li>
 *     <li>The movie to add</li>
 *     <li>The target to watch</li>
 * </ul>
 */
public class AddWatchListRequestModel {

    private final String username;
    private final String movieId;
    private final String watchListId;

    /**
     * Constructs a request model with required entities.
     *
     * @param username the user performing the operation
     * @param movieId the movie to add
     * @param watchListId the target watch list
     */
    public AddWatchListRequestModel(String username, String movieId, String watchListId) {
        this.username = username;
        this.movieId = movieId;
        this.watchListId = watchListId;
    }

    public String getUsername() {
        return username;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getWatchListId() {
        return watchListId;
    }
}

