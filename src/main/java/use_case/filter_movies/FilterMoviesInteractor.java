package use_case.filter_movies;

import entity.Movie;
import use_case.common.MovieGateway;

import java.util.List;

public class FilterMoviesInteractor implements FilterMoviesInputBoundary {

    private final MovieGateway movieGateway;
    private final FilterMoviesOutputBoundary presenter;

    public FilterMoviesInteractor(MovieGateway movieGateway,
                                  FilterMoviesOutputBoundary presenter) {
        this.movieGateway = movieGateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(FilterMoviesRequestModel requestModel) {
        if (requestModel.getGenreIds() == null || requestModel.getGenreIds().isEmpty()) {
            presenter.prepareFailView("Select at least one genre.");
            return;
        }

        /*
         * TODO(Inba Thiyagarajan): Map TMDb genre IDs to names and support richer filtering (sorting, multiple criteria, etc.).
         */
        List<Movie> movies = movieGateway.filterByGenres(requestModel.getGenreIds());
        presenter.prepareSuccessView(new FilterMoviesResponseModel(requestModel.getGenreIds(), movies));
    }
}

