package entity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a movie entity with information retrieved from The Movie Database (TMDb) API.
 *
 * <p>A Movie contains:
 * <ul>
 *     <li>A unique movie identifier (TMDb ID)</li>
 *     <li>Title and plot summary</li>
 *     <li>Genre IDs (as integers matching TMDb genre codes)</li>
 *     <li>Release date (typically in YYYY-MM-DD format)</li>
 *     <li>Rating (vote average from TMDb, typically 0-10)</li>
 *     <li>Popularity score from TMDb</li>
 *     <li>Poster URL for the movie poster image</li>
 * </ul>
 *
 * <p>The rating can be updated after construction, but all other fields are immutable.
 * The genre list is returned as an unmodifiable view to prevent external modification.
 *
 * @author Team Project 9
 */
public class Movie {

    private final String movieId;
    private final String title;
    private final String plot;
    private final List<Integer> genreIds;
    private final String releaseDate;
    private double rating;
    private final String posterUrl;
    private final double popularity;

    /**
     * Creates a new Movie with the specified properties.
     *
     * @param movieId the unique movie identifier (must not be null)
     * @param title the movie title (must not be null)
     * @param plot the plot summary or overview (may be null)
     * @param genreIds list of genre IDs from TMDb (null is treated as empty list)
     * @param releaseDate the release date, typically in YYYY-MM-DD format (may be null)
     * @param rating the TMDb vote average rating (typically 0-10)
     * @param popularity the TMDb popularity score
     * @param posterUrl the URL to the movie poster image (may be null)
     * @throws NullPointerException if {@code movieId} or {@code title} is null
     */
    public Movie(String movieId,
                 String title,
                 String plot,
                 List<Integer> genreIds,
                 String releaseDate,
                 double rating,
                 double popularity,
                 String posterUrl) {
        this.movieId = Objects.requireNonNull(movieId, "movieId");
        this.title = Objects.requireNonNull(title, "title");
        this.plot = plot;
        this.genreIds = genreIds == null ? List.of() : List.copyOf(genreIds);
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.posterUrl = posterUrl;
        this.popularity = popularity; // Fixed: was incorrectly set to 0.0
    }

    /**
     * Returns the unique movie identifier.
     *
     * @return the movie ID
     */
    public String getMovieId() {
        return movieId;
    }

    /**
     * Returns the movie title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the plot summary or overview.
     *
     * @return the plot, or null if not available
     */
    public String getPlot() {
        return plot;
    }

    /**
     * Returns an unmodifiable view of the genre IDs.
     *
     * @return an unmodifiable list of genre IDs
     */
    public List<Integer> getGenreIds() {
        return Collections.unmodifiableList(genreIds);
    }

    /**
     * Returns the TMDb vote average rating.
     *
     * @return the rating (typically 0-10)
     */
    public double getRating() {
        return rating;
    }

    /**
     * Returns the TMDb popularity score.
     *
     * @return the popularity score
     */
    public double getPopularity() {
        return popularity;
    }

    /**
     * Returns the release date.
     *
     * @return the release date, typically in YYYY-MM-DD format, or null if not available
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Extracts and returns the release year from the release date.
     *
     * <p>If the release date is null or has fewer than 4 characters, returns null.
     * Otherwise, attempts to parse the first 4 characters as the year.
     *
     * @return the release year as an Integer, or null if unavailable or unparseable
     */
    public Integer getReleaseYear() {
        if (releaseDate == null || releaseDate.length() < 4) {
            return null;
        }
        try {
            return Integer.parseInt(releaseDate.substring(0, 4));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Updates the rating for this movie.
     *
     * @param rating the new rating value
     */
    public void updateRating(double rating) {
        this.rating = rating;
    }

    /**
     * Returns the URL to the movie poster image.
     *
     * @return the poster URL, or null if not available
     */
    public String getPosterUrl() {
        return posterUrl;
    }
}
