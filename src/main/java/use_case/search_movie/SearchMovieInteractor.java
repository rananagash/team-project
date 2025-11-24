package use_case.search_movie;

import entity.Movie;
import use_case.common.MovieGateway;

import java.util.List;

public class SearchMovieInteractor implements SearchMovieInputBoundary {

    private final MovieGateway movieGateway;
    private final SearchMovieOutputBoundary presenter;

    public SearchMovieInteractor(MovieGateway movieGateway,
                                 SearchMovieOutputBoundary presenter) {
        this.movieGateway = movieGateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(SearchMovieRequestModel requestModel) {
        if (requestModel.getQuery() == null || requestModel.getQuery().isBlank()) {
            presenter.prepareFailView("Search query cannot be empty.");
            return;
        }

        /*
         * TODO(Chester Zhao): Extend search strategy with TMDb pagination, scoring, and network failure handling.
         */
        List<Movie> results = movieGateway.searchByTitle(requestModel.getQuery());
        presenter.prepareSuccessView(new SearchMovieResponseModel(requestModel.getQuery(), results));
    }
}

