package common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for converting TMDb genre IDs to human-readable genre names.
 *
 * <p>This class provides static methods to convert between TMDb genre IDs (integers)
 * and their corresponding genre names (strings). The mapping is based on the official
 * TMDb genre list.
 *
 * <p>This utility is designed to be used across all layers of the application,
 * including the view layer for displaying genres and the use case layer for
 * processing genre-related operations.
 *
 * @author Team Project 9
 */
public final class GenreUtils {

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

    // Private constructor to prevent instantiation
    private GenreUtils() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Returns the human-readable name for a given TMDb genre ID.
     *
     * @param genreId the genre ID
     * @return the genre name, or null if the genre ID is not recognized
     */
    public static String getGenreName(Integer genreId) {
        if (genreId == null) {
            return null;
        }
        return GENRE_ID_TO_NAME.get(genreId);
    }

    /**
     * Returns the human-readable name for a given TMDb genre ID, with a fallback
     * for unknown genres.
     *
     * @param genreId the genre ID
     * @return the genre name, or "Unknown Genre (ID)" if not found
     */
    public static String getGenreNameOrUnknown(Integer genreId) {
        if (genreId == null) {
            return "Unknown Genre";
        }
        String name = GENRE_ID_TO_NAME.get(genreId);
        return name != null ? name : "Unknown Genre (" + genreId + ")";
    }

    /**
     * Converts a list of genre IDs to a list of genre names.
     *
     * @param genreIds the list of genre IDs to convert
     * @return a list of genre names (unknown genres will show as "Unknown Genre (ID)")
     */
    public static List<String> getGenreNames(List<Integer> genreIds) {
        if (genreIds == null) {
            return List.of();
        }
        return genreIds.stream()
                .map(GenreUtils::getGenreNameOrUnknown)
                .collect(Collectors.toList());
    }

    /**
     * Converts a list of genre IDs to a comma-separated string of genre names.
     *
     * @param genreIds the list of genre IDs to convert
     * @return a comma-separated string of genre names
     */
    public static String getGenreNamesAsString(List<Integer> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) {
            return "";
        }
        return genreIds.stream()
                .map(GenreUtils::getGenreNameOrUnknown)
                .collect(Collectors.joining(", "));
    }

    /**
     * Returns an immutable copy of all available genre IDs and their corresponding names.
     *
     * @return a new map containing all genre IDs and names
     */
    public static Map<Integer, String> getAllGenres() {
        return Map.copyOf(GENRE_ID_TO_NAME);
    }

    /**
     * Checks if a genre ID is valid (exists in the mapping).
     *
     * @param genreId the genre ID to check
     * @return true if the genre ID is recognized, false otherwise
     */
    public static boolean isValidGenreId(Integer genreId) {
        if (genreId == null) {
            return false;
        }
        return GENRE_ID_TO_NAME.containsKey(genreId);
    }
}

