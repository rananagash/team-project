package use_case.usecases;

import com.moviesearch.domain.entities.Movie;
import java.util.List;
import java.util.stream.Collectors;

public class FilterMoviesUseCase {
    public List<Movie> execute(List<Movie> movies, String genreId) {
        if ("all".equals(genreId)) {
            return movies;
        }

        try {
            int genre = Integer.parseInt(genreId);
            return movies.stream()
                    .filter(movie -> movie.getGenreIds().contains(genre))
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            return movies;
        }
    }
}