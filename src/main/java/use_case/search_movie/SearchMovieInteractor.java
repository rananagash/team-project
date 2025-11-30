package use_case.search_movie;

import entity.Movie;
import use_case.common.MovieGateway;
import use_case.common.MovieDataAccessException;
import use_case.common.PagedMovieResult;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

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
        String query = requestModel.getQuery();

        if (query == null || query.isBlank()) {
            presenter.prepareFailView("Search query cannot be empty.");
            return;
        }
        if (query.length() < 2) {
            presenter.prepareFailView("Search query must be at least 2 characters long.");
            return;
        }

        if (query.matches("[^a-zA-Z0-9]+")) {
            presenter.prepareFailView("Please enter a meaningful movie title.");
            return;
        }

        int requestedPage = requestModel.getPage() <= 0 ? 1 : requestModel.getPage();

        try {
            PagedMovieResult pagedResult = movieGateway.searchByTitle(query, requestedPage);

            if (pagedResult.getMovies().isEmpty()) {
                presenter.prepareFailView("No movies found for: \"" + query + "\". Please try a different search term.");
                return;
            }

            List<Movie> scoredAndSorted = scoreAndSortMovies(pagedResult.getMovies(), query);

            SearchMovieResponseModel responseModel = new SearchMovieResponseModel(
                    query,
                    scoredAndSorted,
                    pagedResult.getPage(),
                    pagedResult.getTotalPages()
            );

            presenter.prepareSuccessView(responseModel);

        } catch (MovieDataAccessException e) {
            String errorMessage;
            switch (e.getType()) {
                case NETWORK:
                    errorMessage = "Network error while searching. Please check your internet connection.";
                    break;
                case TMDB_ERROR:
                    errorMessage = "Movie service is temporarily unavailable. Please try again later.";
                    break;
                default:
                    errorMessage = "Search failed due to an unexpected error.";
            }
            presenter.prepareFailView(errorMessage);
        }
    }

    private List<Movie> scoreAndSortMovies(List<Movie> movies, String query) {
        List<Movie> copy = new ArrayList<>(movies);
        final String loweredQuery = query.toLowerCase();

        copy.sort(Comparator.comparingDouble(
                (Movie m) -> scoreMovie(m, loweredQuery)
        ).reversed());

        return copy;
    }

    private double scoreMovie(Movie movie, String loweredQuery) {
        double score = 0.0;

        String title = movie.getTitle() != null
                ? movie.getTitle().toLowerCase()
                : "";

        // search result priority
        if (title.equals(loweredQuery)) {
            score += 50;
        } else if (title.startsWith(loweredQuery)) {
            score += 30;
        } else if (title.contains(loweredQuery)) {
            score += 10;
        }

        // using getter from entity definition
        Double voteAverage = movie.getRating();
        if (voteAverage != null) {
            score += voteAverage * 3;
        }

        Double popularity = movie.getPopularity();
        if (popularity != null) {
            score += popularity * 0.1;
        }

        Integer releaseYear = movie.getReleaseYear();
        if (releaseYear != null) {
            int currentYear = java.time.Year.now().getValue();
            int age = currentYear - releaseYear;
            if (age <= 1) score += 10;
            else if (age <= 5) score += 5;
        }

        return score;
    }
}

