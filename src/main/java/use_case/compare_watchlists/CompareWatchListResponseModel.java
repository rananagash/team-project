package use_case.compare_watchlists;

import entity.Movie;

import java.util.List;

public class CompareWatchListResponseModel {

    private final List<Movie> commonMovies;
    private final List<Movie> baseOnlyMovies;
    private final List<Movie> targetOnlyMovies;

    public CompareWatchListResponseModel(List<Movie> commonMovies,
                                         List<Movie> baseOnlyMovies,
                                         List<Movie> targetOnlyMovies) {
        this.commonMovies = commonMovies;
        this.baseOnlyMovies = baseOnlyMovies;
        this.targetOnlyMovies = targetOnlyMovies;
    }

    public List<Movie> getCommonMovies() {
        return commonMovies;
    }

    public List<Movie> getBaseOnlyMovies() {
        return baseOnlyMovies;
    }

    public List<Movie> getTargetOnlyMovies() {
        return targetOnlyMovies;
    }
}

