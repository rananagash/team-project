package use_case.filter_movies;

import java.util.List;
import java.util.stream.Collectors;

import common.GenreUtils;
import entity.Movie;
import use_case.common.MovieGateway;

/**
 * Interactor for the Filter Movies use case.
 * <p>
 * This class handles filtering movies by genre IDs. It filters the provided
 * movies and prepares the response via {@link FilterMoviesOutputBoundary}.
 */
public class FilterMoviesInteractor implements FilterMoviesInputBoundary {

    private final MovieGateway movieGateway;
    private final FilterMoviesOutputBoundary presenter;

    /**
     * Constructs a new {@code FilterMoviesInteractor}.
     *
     * @param movieGateway the gateway (used for potential future data access)
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
     * Validates the request, filters movies matching the genre IDs, converts
     * genre IDs to names, and forwards the response to the presenter.
     *
     * @param requestModel the request model containing genre IDs and movies to filter
     */
    @Override
    public void execute(FilterMoviesRequestModel requestModel) {
        if (requestModel.getGenreIds() == null || requestModel.getGenreIds().isEmpty()) {
            presenter.prepareFailView("Select at least one genre.");
            return;
        }

        if (requestModel.getMoviesToFilter() == null || requestModel.getMoviesToFilter().isEmpty()) {
            presenter.prepareFailView("No movies to filter.");
            return;
        }

        // Filter movies by selected genres
        List<Movie> filteredMovies = requestModel.getMoviesToFilter().stream()
                .filter(movie -> {
                    List<Integer> movieGenres = movie.getGenreIds();
                    if (movieGenres == null || movieGenres.isEmpty()) {
                        return false;
                    }
                    // Check if movie has at least one of the selected genres
                    return movieGenres.stream().anyMatch(requestModel.getGenreIds()::contains);
                })
                .collect(Collectors.toList());

        // Convert genre IDs to human-readable genre names using GenreUtils
        List<String> genreNames = GenreUtils.getGenreNames(requestModel.getGenreIds());

        // Prepare the response for the presenter
        presenter.prepareSuccessView(new FilterMoviesResponseModel(
                requestModel.getGenreIds(),
                genreNames,
                filteredMovies));
    }
}
