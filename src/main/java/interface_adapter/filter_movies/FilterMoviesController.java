package interface_adapter.filter_movies;

import use_case.filter_movies.FilterMoviesInputBoundary;
import use_case.filter_movies.FilterMoviesRequestModel;

import java.util.List;

public class FilterMoviesController {

    private final FilterMoviesInputBoundary interactor;

    public FilterMoviesController(FilterMoviesInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void filterByGenres(List<Integer> genreIds) {
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds);
        interactor.execute(requestModel);
    }
}
