package use_case.usecases;

import entity.Movie;
import entity.SearchResult;
import data_access.repositories.MovieRepository;
import java.util.List;

public class SearchMoviesUseCase {
    private final MovieRepository movieRepository;

    public SearchMoviesUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public SearchResult execute(String query) {
        if (query == null || query.trim().isEmpty()) {
            return SearchResult.error("please search by title");
        }

        try {
            List<Movie> movies = movieRepository.searchMovies(query.trim());
            if (movies.isEmpty()) {
                return SearchResult.error("none found, try again");
            }
            return SearchResult.success(movies);
        } catch (Exception e) {
            return SearchResult.error("search failed: " + e.getMessage());
        }
    }
}