package interface_adapter.filter_movies;

import use_case.filter_movies.FilterMoviesOutputBoundary;
import use_case.filter_movies.FilterMoviesResponseModel;

public class FilterMoviesPresenter implements FilterMoviesOutputBoundary {

    @Override
    public void prepareSuccessView(FilterMoviesResponseModel responseModel) {
        /*
         * TODO(Inba Thiyagarajan): Display the filtered movies in the UI; consider chips or dropdown interactions.
         */
    }

    @Override
    public void prepareFailView(String errorMessage) {
        /*
         * TODO(Inba Thiyagarajan): Surface messaging when filtering fails or yields no results.
         */
    }
}

