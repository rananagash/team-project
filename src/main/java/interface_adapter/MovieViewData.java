package interface_adapter;

import java.util.List;

/**
 * A simple data-transfer object used by the interface adapter layer to pass movie information from persenters to views.
 *
 * <p>This class contains only the fields that the UI needs to render a movie card.
 *
 * <p>Instances of {@code MovieViewData} are immutable.
 */
public class MovieViewData {

    private final String id;
    private final String title;
    private final String plot;
    private final List<Integer> genreIds;
    private final String releaseDate;
    private final double rating;
    private final String posterUrl;

    /**
     * Constructs a view-friendly representation of a movie.
     *
     * @param id the unique movie id
     * @param title the movie title
     * @param plot a short plot summary or description
     * @param genreIds the numeric genre IDs associated with this movie
     * @param releaseDate the release date
     * @param rating the movie's rating on TMDb
     * @param posterUrl the poster URL
     */
    public MovieViewData(String id,
                         String title,
                         String plot,
                         List<Integer> genreIds,
                         String releaseDate,
                         double rating,
                         String posterUrl) {
        this.id = id;
        this.title = title;
        this.plot = plot;
        this.genreIds = genreIds;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.posterUrl = posterUrl;
    }

    /**
     * Returns the movie's unique ID.
     *
     * @return the movie ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the movie's title.
     *
     * @return the movie title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the movie's plot summary.
     *
     * @return the plot summary
     */
    public String getPlot() {
        return plot;
    }

    /**
     * Returns the numeric genre identifiers from this movie.
     *
     * @return list of integer genre IDs
     */
    public List<Integer> getGenreIds() {
        return genreIds;
    }

    /**
     * Returns the movie's release date.
     *
     * @return the movie's release date
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * Returns the movie's rating.
     * @return the movie's rating
     */
    public double getRating() {
        return rating;
    }

    /**
     * Returns the URL of the movie's poster image.
     *
     * @return the poster URL
     */
    public String getPosterUrl() {
        return posterUrl;
    }
}
