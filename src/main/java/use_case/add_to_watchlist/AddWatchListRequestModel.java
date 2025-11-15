package use_case.add_to_watchlist;

import entity.Movie;
import entity.User;
import entity.WatchList;

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

    private final User user;
    private final Movie movie;
    private final WatchList watchList;

    /**
     * constructs a request model with required entities
     *
     * @param user the user performing the operation
     * @param movie the movie to add
     * @param watchList the target watch list
     */
    public AddWatchListRequestModel(User user,  Movie movie, WatchList watchList) {
        this.user = user;
        this.movie = movie;
        this.watchList = watchList;
    }

    public User getUser() {
        return user;
    }

    public Movie getMovie() {
        return movie;
    }

    public WatchList getWatchList() {
        return watchList;
    }
}

