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

        /*
         * TODO(Chester Zhao): Extend search strategy with TMDb pagination, scoring, and network failure handling.
         */
        int requestedPage = requestModel.getPage() <= 0 ? 1 : requestModel.getPage();

        try {
            // Gateway: get assigned page from TMDB
            PagedMovieResult pagedResult =
                    movieGateway.searchByTitle(query, requestedPage);

            // scoring
            List<Movie> scoredAndSorted =
                    scoreAndSortMovies(pagedResult.getMovies(), query);

            // Pagination
            SearchMovieResponseModel responseModel =
                    new SearchMovieResponseModel(
                            query,
                            scoredAndSorted,
                            pagedResult.getPage(),
                            pagedResult.getTotalPages()
                    );

            presenter.prepareSuccessView(responseModel);

        } catch (MovieDataAccessException e) {
            // 4. server errorS
            switch (e.getType()) {
                case NETWORK -> presenter.prepareFailView(
                        "Network error while searching movies. Please check your connection and try again."
                );
                case TMDB_ERROR -> presenter.prepareFailView(
                        "The movie service is currently unavailable. Please try again later."
                );
                default -> presenter.prepareFailView(
                        "Unexpected error while searching movies."
                );
            }
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

