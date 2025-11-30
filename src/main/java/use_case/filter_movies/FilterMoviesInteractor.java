package use_case.filter_movies;

import entity.Movie;
import use_case.common.MovieGateway;
import common.GenreUtils;

import java.util.List;

/**
 * Interactor for the Filter Movies use case.
 * <p>
 * This class handles filtering movies by genre IDs. It retrieves movies from
 * the {@link MovieGateway} and prepares the response via
 * {@link FilterMoviesOutputBoundary}.
 */
public class FilterMoviesInteractor implements FilterMoviesInputBoundary {

    private final MovieGateway movieGateway;
    private final FilterMoviesOutputBoundary presenter;

    /**
     * Constructs a new {@code FilterMoviesInteractor}.
     *
     * @param movieGateway the gateway used to fetch movies
     * @param presenter    the presenter to prepare the response for the view
     */
    public FilterMoviesInteractor(MovieGateway movieGateway,
                                  FilterMoviesOutputBoundary presenter) {
        this.movieGateway = movieGateway;
        this.presenter = presenter;
    }

    /**
     * Executes the filter movies use case.
     * <p>
     * Validates the request, fetches movies matching the genre IDs, converts
     * genre IDs to names, and forwards the response to the presenter.
     *
     * @param requestModel the request model containing genre IDs
     */
    @Override
    public void execute(FilterMoviesRequestModel requestModel) {
        if (requestModel.getGenreIds() == null || requestModel.getGenreIds().isEmpty()) {
            presenter.prepareFailView("Select at least one genre.");
            return;
        }

        // Retrieve movies matching the requested genres
        List<Movie> movies = movieGateway.filterByGenres(requestModel.getGenreIds());

        // Convert genre IDs to human-readable genre names using GenreUtils
        // Note: getGenreNames (plural) takes List<Integer>, not getGenreName (singular) which takes Integer
        List<String> genreNames = GenreUtils.getGenreNames(requestModel.getGenreIds());

        // Prepare the response for the presenter
        presenter.prepareSuccessView(new FilterMoviesResponseModel(
                requestModel.getGenreIds(),
                genreNames,
                movies));
    }
}
