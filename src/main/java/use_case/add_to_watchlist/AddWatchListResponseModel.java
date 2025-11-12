package use_case.add_to_watchlist;

import entity.Movie;

import java.util.List;

public class AddWatchListResponseModel {

    private final String watchListName;
    private final List<Movie> movies;

    public AddWatchListResponseModel(String watchListName, List<Movie> movies) {
        this.watchListName = watchListName;
        this.movies = movies;
    }

    public String getWatchListName() {
        return watchListName;
    }

    public List<Movie> getMovies() {
        return movies;
    }
}

