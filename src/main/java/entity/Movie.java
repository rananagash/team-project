package entity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Movie {

    /*
     * TODO(team): Adjust fields based on the actual TMDb payload, e.g., add language or runtime if needed.
     */
    private final String movieId;
    private final String title;
    private final String plot;
    private final List<Integer> genreIds;
    private final String releaseDate;
    private double rating;
    private final String posterUrl;
    private final double popularity;

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
        this.popularity = 0.0;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getPlot() {
        return plot;
    }

    public List<Integer> getGenreIds() {
        return Collections.unmodifiableList(genreIds);
    }

    public double getRating() {
        return rating;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Integer getReleaseYear(){
        if (releaseDate == null || releaseDate.length() < 4) {
            return null;
        }
        try {
            return Integer.parseInt(releaseDate.substring(0, 4));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void updateRating(double rating) {
        this.rating = rating;
    }

    public String getPosterUrl() {
        return posterUrl;
    }
}
