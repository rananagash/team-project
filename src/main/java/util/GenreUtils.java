
package util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GenreUtils {
    private static final Map<Integer, String> genreMap = Map.of(
    );

    public static String getGenreNames(List<Integer> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) {
            return "N/A";
        }

        return genreIds.stream()
                .map(genreMap::get)
                .filter(name -> name != null)
                .collect(Collectors.joining(", "));
    }

    public static List<String> getUniqueGenres(List<Integer> genreIds) {
        if (genreIds == null) {
            return List.of();
        }

        return genreIds.stream()
                .distinct()
                .map(genreMap::get)
                .filter(name -> name != null)
                .sorted()
                .collect(Collectors.toList());
    }
}