package entity;

import java.time.LocalDateTime;
import java.util.List;

public class WatchedMovie extends Movie {

    private final LocalDateTime watchedDate;

    public WatchedMovie(Movie movie, LocalDateTime watchedDate) {
        super(movie.getMovieId(),
                movie.getTitle(),
                movie.getPlot(),
                movie.getGenreIds(),
                movie.getReleaseDate(),
                movie.getRating(),
                movie.getPosterUrl());
        this.watchedDate = watchedDate == null ? LocalDateTime.now() : watchedDate;
    }

    public WatchedMovie(String movieId,
                        String title,
                        String plot,
                        List<Integer> genreIds,
                        String releaseDate,
                        double rating,
                        String posterUrl,
                        LocalDateTime watchedDate) {
        super(movieId, title, plot, genreIds, releaseDate, rating, posterUrl);
        this.watchedDate = watchedDate == null ? LocalDateTime.now() : watchedDate;
    }

    public LocalDateTime getWatchedDate() {
        return watchedDate;
    }
}
