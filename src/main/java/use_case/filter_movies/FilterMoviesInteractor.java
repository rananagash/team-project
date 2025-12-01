package use_case.filter_movies;

import java.util.List;

import entity.Movie;
import use_case.common.MovieGateway;

/**
 * Interactor for the Filter Movies use case.
 * <p>
 * This class orchestrates the filter movies use case by delegating to
 * specialized components (validator, filter strategy, genre converter).
 * <p>
 * This implementation follows SOLID principles:
 * <ul>
 *   <li>Single Responsibility: Orchestrates the use case flow</li>
 *   <li>Open/Closed: Extensible through MovieFilterStrategy interface</li>
 *   <li>Dependency Inversion: Depends on abstractions (interfaces)</li>
 * </ul>
 */
public class FilterMoviesInteractor implements FilterMoviesInputBoundary {

    private final MovieGateway movieGateway;
    private final FilterMoviesOutputBoundary presenter;
    private final FilterMoviesValidator validator;
    private final MovieFilterStrategy filterStrategy;
    private final GenreConverter genreConverter;

    /**
     * Constructs a new {@code FilterMoviesInteractor}.
     *
     * @param movieGateway the gateway (used for potential future data access)
     * @param presenter    the presenter to prepare the response for the view
     * @param validator    the validator for request models
     * @param filterStrategy the strategy for filtering movies
     * @param genreConverter the converter for genre IDs to names
     */
    public FilterMoviesInteractor(MovieGateway movieGateway,
                                  FilterMoviesOutputBoundary presenter,
                                  FilterMoviesValidator validator,
                                  MovieFilterStrategy filterStrategy,
                                  GenreConverter genreConverter) {
        this.movieGateway = movieGateway;
        this.presenter = presenter;
        this.validator = validator;
        this.filterStrategy = filterStrategy;
        this.genreConverter = genreConverter;
    }

    /**
     * Executes the filter movies use case.
     * <p>
     * Validates the request, filters movies matching the genre IDs using the
     * configured strategy, converts genre IDs to names, and forwards the
     * response to the presenter.
     *
     * @param requestModel the request model containing genre IDs and movies to filter
     */
    @Override
    public void execute(FilterMoviesRequestModel requestModel) {
        // Validate request
        String validationError = validator.validate(requestModel);
        if (validationError != null) {
            presenter.prepareFailView(validationError);
            return;
        }

        // Filter movies using the configured strategy
        List<Movie> filteredMovies = filterStrategy.filter(
                requestModel.getMoviesToFilter(),
                requestModel.getGenreIds());

        // Convert genre IDs to human-readable genre names
        List<String> genreNames = genreConverter.convertGenreIdsToNames(
                requestModel.getGenreIds());

        // Prepare the response for the presenter
        presenter.prepareSuccessView(new FilterMoviesResponseModel(
                requestModel.getGenreIds(),
                genreNames,
                filteredMovies));
    }
}
