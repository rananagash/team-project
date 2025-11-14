package use_case.add_to_watchlist;

import entity.Movie;
import entity.User;
import entity.WatchList;

public class AddWatchListRequestModel {

    private final User user;
    private final Movie movie;
    private final WatchList watchList;

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

