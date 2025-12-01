package use_case.filter_movies;

import entity.Movie;

import java.util.List;

/**
 * Strategy interface for filtering movies.
 * <p>
 * This interface follows the Open/Closed Principle by allowing the filtering
 * logic to be extended with new strategies (e.g., AND vs OR filtering,
 * date range filtering, rating filtering) without modifying existing code.
 * <p>
 * It also follows the Single Responsibility Principle by separating filtering
 * logic from the interactor.
 */
public interface MovieFilterStrategy {

    /**
     * Filters a list of movies based on the specified criteria.
     *
     * @param movies the list of movies to filter
     * @param genreIds the list of genre IDs to filter by
     * @return a filtered list of movies that match the criteria
     */
    List<Movie> filter(List<Movie> movies, List<Integer> genreIds);
}

