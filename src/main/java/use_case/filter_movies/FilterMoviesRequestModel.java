package use_case.filter_movies;

import java.util.List;

public class FilterMoviesRequestModel {

    private final List<Integer> genreIds;

    public FilterMoviesRequestModel(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }
}

