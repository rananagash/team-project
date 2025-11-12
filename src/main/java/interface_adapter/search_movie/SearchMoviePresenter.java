package interface_adapter.search_movie;

import use_case.search_movie.SearchMovieOutputBoundary;
import use_case.search_movie.SearchMovieResponseModel;

public class SearchMoviePresenter implements SearchMovieOutputBoundary {

    @Override
    public void prepareSuccessView(SearchMovieResponseModel responseModel) {
        /*
         * TODO(Chester Zhao): Push the search results to the UI and design pagination or lazy loading.
         */
    }

    @Override
    public void prepareFailView(String errorMessage) {
        /*
         * TODO(Chester Zhao): Show error or empty-state messaging in the UI.
         */
    }
}

