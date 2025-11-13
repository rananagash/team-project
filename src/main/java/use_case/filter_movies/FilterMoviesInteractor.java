package use_case.filter_movies;

import entity.Movie;
import use_case.common.MovieGateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
        Map<Integer, String> genreMap = new HashMap<>();
        genreMap.put(28, "Action");
        genreMap.put(12, "Adventure");
        genreMap.put(16, "Animation");
        genreMap.put(35, "Comedy");
        genreMap.put(80, "Crime");
        genreMap.put(99, "Documentary");
        genreMap.put(18, "Drama");
        genreMap.put(10751, "Family");
        genreMap.put(14, "Fantasy");
        genreMap.put(36, "History");
        genreMap.put(27, "Horror");
        genreMap.put(10402, "Music");
        genreMap.put(9648, "Mystery");
        genreMap.put(10749, "Romance");
        genreMap.put(878, "Science Fiction");
        genreMap.put(10770, "TV Movie");
        genreMap.put(53, "Thriller");
        genreMap.put(10752, "War");
        genreMap.put(37, "Western");

        List<String> selectedGenres = requestModel.getGenreIds().stream()
                .map(genreMap::get)
                .filter(Objects::nonNull);
                Collectors.toList();

        if (selectedGenres.isEmpty()) {
            presenter.prepareFailView("Invalid genre selection.");
            return;
        }

        List<Movie> movies = movieGateway.filterByGenres(requestModel.getGenreIds());
        presenter.prepareSuccessView(new FilterMoviesResponseModel(requestModel.getGenreIds(), movies));
    }
}

