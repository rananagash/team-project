package entity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a movie that has been watched by a user, extending {@link Movie}
 * with a timestamp indicating when it was watched.
 *
 * <p>WatchedMovie inherits all properties from Movie and adds:
 * <ul>
 *     <li>A watched date timestamp</li>
 * </ul>
 *
 * <p>This class is used in {@link WatchHistory} to track when movies were viewed.
 * If a null watched date is provided, it defaults to the current time.
 *
 * @author Team Project 9
 */
public class WatchedMovie extends Movie {

    private final LocalDateTime watchedDate;

    /**
     * Creates a WatchedMovie from an existing Movie and watched timestamp.
     *
     * @param movie the movie that was watched (must not be null)
     * @param watchedDate the date and time when the movie was watched (null defaults to current time)
     * @throws NullPointerException if {@code movie} is null
     */
    public WatchedMovie(Movie movie, LocalDateTime watchedDate) {
        super(movie.getMovieId(),
                movie.getTitle(),
                movie.getPlot(),
                movie.getGenreIds(),
                movie.getReleaseDate(),
                movie.getRating(),
                movie.getPopularity(),
                movie.getPosterUrl());
        this.watchedDate = watchedDate == null ? LocalDateTime.now() : watchedDate;
    }

    /**
     * Creates a WatchedMovie with all properties specified directly.
     *
     * @param movieId the unique movie identifier (must not be null)
     * @param title the movie title (must not be null)
     * @param plot the plot summary (may be null)
     * @param genreIds list of genre IDs (null is treated as empty list)
     * @param releaseDate the release date (may be null)
     * @param rating the rating
     * @param popularity the popularity score
     * @param posterUrl the poster URL (may be null)
     * @param watchedDate the date and time when the movie was watched (null defaults to current time)
     * @throws NullPointerException if {@code movieId} or {@code title} is null
     */
    public WatchedMovie(String movieId,
                        String title,
                        String plot,
                        List<Integer> genreIds,
                        String releaseDate,
                        double rating,
                        double popularity,
                        String posterUrl,
                        LocalDateTime watchedDate) {
        super(movieId, title, plot, genreIds, releaseDate, rating, popularity, posterUrl);
        this.watchedDate = watchedDate == null ? LocalDateTime.now() : watchedDate;
    }

    /**
     * Returns the date and time when this movie was watched.
     *
     * @return the watched date timestamp
     */
    public LocalDateTime getWatchedDate() {
        return watchedDate;
    }
}
