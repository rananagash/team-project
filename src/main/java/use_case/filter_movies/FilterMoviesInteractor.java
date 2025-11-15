package use_case.filter_movies;

import entity.Movie;
import use_case.common.MovieGateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterMoviesInteractor implements FilterMoviesInputBoundary {

    private final MovieGateway movieGateway;
    private final FilterMoviesOutputBoundary presenter;

    // Map TMDb genre IDs to genre names
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

        // Map TMDb genre IDs to names and support richer filtering (sorting, multiple criteria, etc.).
        List<Movie> movies = movieGateway.filterByGenres(requestModel.getGenreIds());
        presenter.prepareSuccessView(new FilterMoviesResponseModel(requestModel.getGenreIds(), movies));
    }


     // Get the genre name for a given genre ID.
     // Return the genre name, or null if the ID is not found

    public static String getGenreName(Integer genreId) {
        return GENRE_ID_TO_NAME.get(genreId);
    }


     // Get all available genre IDs and their names.
     // Return A map of genre IDs to genre names

    public static Map<Integer, String> getAllGenres() {
        return new HashMap<>(GENRE_ID_TO_NAME);
    }
}

