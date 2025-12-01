package use_case.filter_movies;

import java.util.List;

/**
 * Interface for converting genre IDs to human-readable genre names.
 * <p>
 * This interface follows the Dependency Inversion Principle by allowing
 * the use case layer to depend on an abstraction rather than a concrete
 * implementation (e.g., GenreUtils).
 */
public interface GenreConverter {

    /**
     * Converts a list of genre IDs to their corresponding genre names.
     *
     * @param genreIds the list of genre IDs to convert
     * @return a list of genre names corresponding to the provided genre IDs
     */
    List<String> convertGenreIdsToNames(List<Integer> genreIds);
}

