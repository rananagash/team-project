package use_case.view_watchlists;

import java.util.List;

/**
 * Response model for the View Watch Lists use case.
 * Contains lightweight data for watchlists and movies for
 * use by the presenter and UI.
 */
public class ViewWatchListsResponseModel {

    private final List<WatchListInfo> watchLists;
    private final int selectedIndex;
    private final List<MovieInfo> movies;
    private final String username;

    /**
     * Creates a full response model containing watchlists, the selected index of watchlist,
     * its movies, and the username.
     *
     * @param watchLists the list of watchlists
     * @param selectedIndex the currently selected watchlist index
     * @param movies the movies belonging to the selected watchlist
     * @param username the user whose data this represents
     */
    public ViewWatchListsResponseModel(List<WatchListInfo> watchLists,
                                       int selectedIndex,
                                       List<MovieInfo> movies,
                                       String username) {
        this.watchLists = watchLists;
        this.selectedIndex = selectedIndex;
        this.movies = movies;
        this.username = username;
    }

    /**
     * Returns the list of watchlists.
     *
     * @return the watchlists
     */
    public List<WatchListInfo> getWatchLists() {
        return watchLists;
    }

    /**
     * Returns the selected index.
     * @return the selected index
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Returns the movies belonging to the selected watchlist.
     *
     * @return the list of movies
     */
    public List<MovieInfo> getMovies() {
        return movies;
    }

    /**
     * Returns the username associated with this response model.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * A Data Transfer Object representing a single watchlist.
     */
    public static class WatchListInfo {
        private final String id;
        private final String name;

        /**
         * Constructs watchlist summary data.
         *
         * @param id the unique ID of the watchlist
         * @param name the display name of the watchlist
         */
        public WatchListInfo(String id, String name) {
            this.id = id;
            this.name = name;
        }

        /**
         * Returns the unique ID of this watchlist.
         *
         * @return the watchlist ID
         */
        public String getId() {
            return id;
        }

        /**
         * Returns the display name of this watchlist.
         *
         * @return the watchlist name
         */
        public String getName() {
            return name;
        }
    }

    /**
     * A Data Transfer Object representing a single movie entry.
     */
    public static class MovieInfo {
        private final String id;
        private final String title;
        private final String plot;
        private final List<Integer> genreIds;
        private final String releaseDate;
        private final double rating;
        private final String posterUrl;

        /**
         * Constructs movie summary data.
         *
         * @param id the movie ID
         * @param title the movie title
         * @param plot a short plot summary
         * @param genreIds list of TMDb genre IDs
         * @param releaseDate release date
         * @param rating the movie's average rating
         * @param posterUrl the URL for the movie poster
         */
        public MovieInfo(String id,
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
         * Returns the movie title.
         *
         * @return the title
         */
        public String getTitle() {
            return title;
        }

        /**
         * Returns the plot summary.
         *
         * @return the movie plot
         */
        public String getPlot() {
            return plot;
        }

        /**
         * Returns the list of TMDb genre IDs associated with the movie.
         *
         * @return the list of genre IDs
         */
        public List<Integer> getGenreIds() {
            return genreIds;
        }

        /**
         * Returns the movie's release date.
         *
         * @return the release date
         */
        public String getReleaseDate() {
            return releaseDate;
        }

        /**
         * Returns the movie's average rating.
         *
         * @return the rating
         */
        public double getRating() {
            return rating;
        }

        /**
         * Returns the URL for the movie poster image.
         *
         * @return the poster image URL
         */
        public String getPosterUrl() {
            return posterUrl;
        }
    }
}
