package interface_adapter.search_movie;

import use_case.search_movie.SearchMovieInputBoundary;
import use_case.search_movie.SearchMovieRequestModel;

public class SearchMovieController {

    private final SearchMovieInputBoundary interactor;

    public SearchMovieController(SearchMovieInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void search(String query) {
        SearchMovieRequestModel requestModel = new SearchMovieRequestModel(query);
        interactor.execute(requestModel);
    }
}
