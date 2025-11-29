package data_access.repositories;

import entity.Movie;

import java.util.ArrayList;
import java.util.List;

public class StorageRepositoryImpl implements StorageRepository {
    private List<Movie> watchlist = new ArrayList<>();
    private List<Movie> watchHistory = new ArrayList<>();

    @Override
    public List<Movie> getWatchlist() {
        return new ArrayList<>(watchlist);
    }

    @Override
    public void setWatchlist(List<Movie> movies) {
        this.watchlist = new ArrayList<>(movies);
    }

    @Override
    public List<Movie> getWatchHistory() {
        return new ArrayList<>(watchHistory);
    }

    @Override
    public void setWatchHistory(List<Movie> movies) {
        this.watchHistory = new ArrayList<>(movies);
    }
}