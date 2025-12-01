package use_case.filter_movies;

import common.GenreUtils;

import java.util.List;

/**
 * Adapter that adapts the GenreUtils utility class to the GenreConverter interface.
 * <p>
 * This adapter allows the use case layer to depend on the GenreConverter abstraction
 * while using the existing GenreUtils implementation. This follows the Adapter pattern
 * and the Dependency Inversion Principle.
 */
public class GenreUtilsAdapter implements GenreConverter {

    /**
     * Converts a list of genre IDs to their corresponding genre names
     * using the GenreUtils utility class.
     *
     * @param genreIds the list of genre IDs to convert
     * @return a list of genre names corresponding to the provided genre IDs
     */
    @Override
    public List<String> convertGenreIdsToNames(List<Integer> genreIds) {
        return GenreUtils.getGenreNames(genreIds);
    }
}

