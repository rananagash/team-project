package use_case.filter_movies;

import entity.Movie;
import use_case.common.MovieGateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interactor for the Filter Movies use case.
 * <p>
 * This class handles filtering movies by genre IDs. It retrieves movies from
 * the {@link MovieGateway} and prepares the response via
 * {@link FilterMoviesOutputBoundary}.
 * It also maintains a mapping of TMDb genre IDs to human-readable genre names.
 */
public class FilterMoviesInteractor implements FilterMoviesInputBoundary {

    private final MovieGateway movieGateway;
    private final FilterMoviesOutputBoundary presenter;

    /** Map of TMDb genre IDs to genre names. */
    private static final Map<Integer, String> GENRE_ID_TO_NAME = new HashMap<>();

    static {
        GENRE_ID_TO_NAME.put(28, "Action");
        GENRE_ID_TO_NAME.put(12, "Adventure");
        GENRE_ID_TO_NAME.put(16, "Animation");
        GENRE_ID_TO_NAME.put(35, "Comedy");
        GENRE_ID_TO_NAME.put(80, "Crime");
        GENRE_ID_TO_NAME.put(99, "Documentary");
        GENRE_ID_TO_NAME.put(18, "Drama");
        GENRE_ID_TO_NAME.put(10751, "Family");
        GENRE_ID_TO_NAME.put(14, "Fantasy");
        GENRE_ID_TO_NAME.put(36, "History");
        GENRE_ID_TO_NAME.put(27, "Horror");
        GENRE_ID_TO_NAME.put(10402, "Music");
        GENRE_ID_TO_NAME.put(9648, "Mystery");
        GENRE_ID_TO_NAME.put(10749, "Romance");
        GENRE_ID_TO_NAME.put(878, "Science Fiction");
        GENRE_ID_TO_NAME.put(10770, "TV Movie");
        GENRE_ID_TO_NAME.put(53, "Thriller");
        GENRE_ID_TO_NAME.put(10752, "War");
        GENRE_ID_TO_NAME.put(37, "Western");
    }

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

        // Convert genre IDs to human-readable genre names
        List<String> genreNames = requestModel.getGenreIds().stream()
                .map(genreId -> {
                    String genreName = GENRE_ID_TO_NAME.get(genreId);
                    return genreName != null ? genreName : "Unknown Genre (" + genreId + ")";
                })
                .toList();

        // Prepare the response for the presenter
        presenter.prepareSuccessView(new FilterMoviesResponseModel(
                requestModel.getGenreIds(),
                genreNames,
                movies));
    }

    /**
     * Returns the human-readable name for a given TMDb genre ID.
     *
     * @param genreId the genre ID
     * @return the genre name, or null if not found
     */
    public static String getGenreName(Integer genreId) {
        return GENRE_ID_TO_NAME.get(genreId);
    }

    /**
     * Returns a map of all available genre IDs and their corresponding names.
     *
     * @return a new map containing all genre IDs and names
     */
    public static Map<Integer, String> getAllGenres() {
        return new HashMap<>(GENRE_ID_TO_NAME);
    }
}
