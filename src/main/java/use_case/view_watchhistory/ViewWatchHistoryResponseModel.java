package use_case.view_watchhistory;

import entity.WatchedMovie;

import java.util.List;

public class ViewWatchHistoryResponseModel {

    private final String userName;
    private final List<WatchedMovie> watchedMovies;

    public ViewWatchHistoryResponseModel(String userName, List<WatchedMovie> watchedMovies) {
        this.userName = userName;
        this.watchedMovies = watchedMovies;
    }

    public String getUserName() {
        return userName;
    }

    public List<WatchedMovie> getWatchedMovies() {
        return watchedMovies;
    }
}

