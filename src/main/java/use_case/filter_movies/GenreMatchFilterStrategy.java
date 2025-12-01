package use_case.filter_movies;

import entity.Movie;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Default filtering strategy that matches movies containing at least one
 * of the specified genre IDs.
 * <p>
 * This is the standard "OR" filtering strategy where a movie matches if
 * it has any of the selected genres.
 */
public class GenreMatchFilterStrategy implements MovieFilterStrategy {

    /**
     * Filters movies to include only those that have at least one
     * of the specified genre IDs.
     *
     * @param movies the list of movies to filter
     * @param genreIds the list of genre IDs to match against
     * @return a filtered list of movies
     */
    @Override
    public List<Movie> filter(List<Movie> movies, List<Integer> genreIds) {
        if (movies == null || genreIds == null) {
            return List.of();
        }

        return movies.stream()
                .filter(movie -> {
                    List<Integer> movieGenres = movie.getGenreIds();
                    if (movieGenres == null || movieGenres.isEmpty()) {
                        return false;
                    }
                    // Check if movie has at least one of the selected genres
                    return movieGenres.stream().anyMatch(genreIds::contains);
                })
                .collect(Collectors.toList());
    }
}

