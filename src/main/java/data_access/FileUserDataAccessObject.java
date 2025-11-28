package data_access;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.Movie;
import entity.Review;
import entity.User;
import entity.WatchHistory;
import entity.WatchList;
import entity.WatchedMovie;
import entity.factories.UserFactory;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.common.UserDataAccessInterface;
import use_case.view_profile.ProfileStats;
import use_case.view_profile.ViewProfileUserDataAccessInterface;

/**
 * File-backed implementation of the {@link UserDataAccessInterface}.
 *
 * <p>This DAO persists complete {@link User} entities, including nested watch lists,
 * reviews, and watch history, to a JSON file.
 */
public class FileUserDataAccessObject implements UserDataAccessInterface,
                                                 ChangePasswordUserDataAccessInterface,
                                                 ViewProfileUserDataAccessInterface {

    private final Path jsonPath;
    private final UserFactory userFactory;
    private final Map<String, User> accounts = new HashMap<>();
    private String currentUsername;

    /**
     * Constructs this DAO, loading any existing data or creating an empty file if needed.
     *
     * @param filePath the path of the JSON file
     * @param userFactory factory for creating {@link User} objects
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public FileUserDataAccessObject(String filePath, UserFactory userFactory) {
        this.jsonPath = Path.of(filePath);
        this.userFactory = userFactory;

        if (!Files.exists(jsonPath)) {
            writeJSON();
        }
        else {
            load();
        }
    }

    // ===============================================================================================================
    // =========================================== PUBLIC INTERFACE METHODS ==========================================
    // ===============================================================================================================

    /**
     * Saves the full state of a user, including nested entities, into the JSON file.
     * @param user the user to persist (must not be {@code null})
     */
    @Override
    public void save(User user) {
        accounts.put(user.getUserName(), user);
        writeJSON();
    }

    /**
     * Retrieves a user by username.
     *
     * @param username the username to look up (must not be {@code null})
     * @return the {@link User} if found or {@code null} otherwise
     */
    @Override
    public User getUser(String username) {
        return accounts.get(username);
    }

    /**
     * Returns {@code true} iff a user with this username exists in storage.
     *
     * @param username the username to check
     * @return {@code true} if the user exists,
     *         {@code false} otherwise
     */
    @Override
    public boolean existsByName(String username) {
        return accounts.containsKey(username);
    }

    /**
     * Records the username of the currently authenticated user, or clears it if {@code null}.
     *
     * @param username the username to record, or {@code null} to clear
     */
    @Override
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
        writeJSON();
    }

    /**
     * Returns the username of the user currently recorded as logged in.
     *
     * @return the username of the logged-in user, or {@code null} if none is set
     */
    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    /**
     * Saves the updated password for an existing user.
     *
     * @param user the user whose password is to be updated
     */
    @Override
    public void changePassword(User user) {
        save(user);
    }

    /**
     * Computes and returns profile statistics for the given user.
     *
     * @param username the username to get stats for
     * @return a {@link ProfileStats} instance
     */
    @Override
    public ProfileStats getUserStats(String username) {
        final User user = accounts.get(username);
        if (user == null) {
            return null;
        }

        // Calculate stats - TODO need to adjust based on User class methods
        int watchlistCount = user.getWatchLists() != null ? user.getWatchLists().size() : 0;
        int reviewCount = 0; // TODO need to implement getReviews() in User class
        int watchedMoviesCount = 0; // TODO need to implement getWatchHistory() in User class

        return new ProfileStats(watchlistCount, reviewCount, watchedMoviesCount);
    }

    // ===============================================================================================================
    // ================================================== I/O LOGIC ==================================================
    // ===============================================================================================================

    private void writeJSON() {
        try {
            final JSONObject root = new JSONObject();
            root.put(JsonKeys.CURRENT_USER, currentUsername);

            final JSONObject usersJson = new JSONObject();
            for (User user : accounts.values()) {
                usersJson.put(user.getUserName(), userToJson(user));
            }

            root.put(JsonKeys.USERS, usersJson);

            Files.writeString(jsonPath, root.toString(4));

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void load() {
        try {
            final String raw = Files.readString(jsonPath);

            if (raw.isBlank()) {
                return;
            }

            final JSONObject root = new JSONObject(raw);

            this.currentUsername = root.optString(JsonKeys.CURRENT_USER, null);

            final JSONObject usersJson = root.getJSONObject(JsonKeys.USERS);
            if (usersJson != null) {
                for (String username : usersJson.keySet()) {
                    final JSONObject u = usersJson.getJSONObject(username);
                    final User user = jsonToUser(u);
                    accounts.put(username, user);
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ===============================================================================================================
    // ================================================= JSON HELPERS ================================================
    // ===============================================================================================================

    private JSONObject userToJson(User user) {
        final JSONObject json = new JSONObject();
        json.put(JsonKeys.USERNAME, user.getUserName());
        json.put(JsonKeys.PASSWORD, user.getPassword());

        // WatchLists
        final JSONArray wlsJson = new JSONArray();
        for (WatchList wl : user.getWatchLists()) {
            wlsJson.put(watchListToJson(wl));
        }
        json.put(JsonKeys.WATCHLISTS, wlsJson);

        // Reviews
        final JSONObject reviewsJson = new JSONObject();
        for (Review r : user.getReviewsByMovieId().values()) {
            reviewsJson.put(r.getReviewId(), reviewToJson(r));
        }
        json.put(JsonKeys.REVIEWS, reviewsJson);

        // WatchHistory
        if (user.getWatchHistory() != null) {
            json.put(JsonKeys.WATCH_HISTORY, watchHistoryToJSON(user.getWatchHistory()));
        }

        return json;
    }

    private User jsonToUser(JSONObject json) {
        final User user = userFactory.create(
                json.getString(JsonKeys.USERNAME),
                json.getString(JsonKeys.PASSWORD)
        );

        user.getWatchLists().clear();

        // WatchLists
        final JSONArray wlsJson = json.optJSONArray(JsonKeys.WATCHLISTS);
        if (wlsJson != null) {
            for (int i = 0; i < wlsJson.length(); i++) {
                final WatchList wl = jsonToWatchList(wlsJson.getJSONObject(i), user);
                user.addWatchList(wl);
            }
        }

        // Reviews
        final JSONObject reviewsJson = json.optJSONObject(JsonKeys.REVIEWS);
        if (reviewsJson != null) {
            for (String id : reviewsJson.keySet()) {
                final Review r = jsonToReview(reviewsJson.getJSONObject(id), user);
                user.addReview(r);
            }
        }

        // WatchHistory
        if (json.has(JsonKeys.WATCH_HISTORY)) {
            final WatchHistory wh = jsonToWatchHistory(json.getJSONObject(JsonKeys.WATCH_HISTORY), user);
            user.setWatchHistory(wh);
        }

        return user;
    }

    // ================================================== WatchList ==================================================

    private JSONObject watchListToJson(WatchList watchList) {
        final JSONObject json = new JSONObject();
        json.put(JsonKeys.WL_ID, watchList.getWatchListId());
        json.put(JsonKeys.WL_NAME, watchList.getName());
        json.put(JsonKeys.WL_DATE_CREATED, watchList.getDateCreated().toString());

        final JSONArray moviesJson = new JSONArray();
        for (Movie m : watchList.getMovies()) {
            moviesJson.put(movieToJSON(m));
        }
        json.put(JsonKeys.WL_MOVIES, moviesJson);

        return json;
    }

    private WatchList jsonToWatchList(JSONObject json, User owner) {
        final WatchList wl = new WatchList(owner, json.getString(JsonKeys.WL_NAME));
        final JSONArray moviesJson = json.optJSONArray(JsonKeys.WL_MOVIES);

        if (moviesJson != null) {
            for (int i = 0; i < moviesJson.length(); i++) {
                final Movie m = jsonToMovie(moviesJson.getJSONObject(i));
                wl.addMovie(m);
            }
        }

        return wl;
    }

    // ==================================================== Movie ====================================================

    private JSONObject movieToJSON(Movie movie) {
        final JSONObject json = new JSONObject();
        json.put(JsonKeys.MOVIE_ID, movie.getMovieId());
        json.put(JsonKeys.MOVIE_TITLE, movie.getTitle());
        json.put(JsonKeys.MOVIE_PLOT, movie.getPlot());
        json.put(JsonKeys.MOVIE_GENRE_IDS, new JSONArray(movie.getGenreIds()));
        json.put(JsonKeys.MOVIE_RELEASE_DATE, movie.getReleaseDate());
        json.put(JsonKeys.MOVIE_RATING, movie.getRating());
        json.put(JsonKeys.MOVIE_POSTER_URL, movie.getPosterUrl());
        json.put(JsonKeys.MOVIE_POPULARITY, movie.getPopularity());
        return json;
    }

    private Movie jsonToMovie(JSONObject json) {
        final List<Integer> genreIds = new ArrayList<>();
        final JSONArray genreIdsJson = json.getJSONArray(JsonKeys.MOVIE_GENRE_IDS);
        for (int i = 0; i < genreIdsJson.length(); i++) {
            genreIds.add(genreIdsJson.getInt(i));
        }

        return new Movie(
                json.getString(JsonKeys.MOVIE_ID),
                json.getString(JsonKeys.MOVIE_TITLE),
                json.optString(JsonKeys.MOVIE_PLOT, null),
                genreIds,
                json.optString(JsonKeys.MOVIE_RELEASE_DATE, null),
                json.optDouble(JsonKeys.MOVIE_RATING, 0),
                json.optDouble(JsonKeys.MOVIE_POPULARITY, 0),
                json.optString(JsonKeys.MOVIE_POSTER_URL, null)
        );
    }

    // =================================================== Review ====================================================

    private JSONObject reviewToJson(Review review) {
        final JSONObject json = new JSONObject();
        json.put(JsonKeys.REVIEW_ID, review.getReviewId());
        json.put(JsonKeys.REVIEW_MOVIE, movieToJSON(review.getMovie()));
        json.put(JsonKeys.REVIEW_RATING, review.getRating());
        json.put(JsonKeys.REVIEW_COMMENT, review.getComment());
        json.put(JsonKeys.REVIEW_CREATED_AT, review.getCreatedAt().toString());
        return json;
    }

    private Review jsonToReview(JSONObject json, User owner) {
        final Movie movie = jsonToMovie(json.getJSONObject(JsonKeys.REVIEW_MOVIE));
        final int rating = json.getInt(JsonKeys.REVIEW_RATING);
        final String comment = json.optString(JsonKeys.REVIEW_COMMENT, "");
        final LocalDateTime createdAt = LocalDateTime.parse(json.getString(JsonKeys.REVIEW_CREATED_AT));

        return new Review(
                json.getString(JsonKeys.REVIEW_ID),
                owner,
                movie,
                rating,
                comment,
                createdAt
        );
    }

    // ================================================= WatchHistory ================================================

    private JSONObject watchHistoryToJSON(WatchHistory watchHistory) {
        final JSONObject json = new JSONObject();
        json.put(JsonKeys.WH_ID, watchHistory.getWatchHistoryId());

        final JSONArray moviesJson = new JSONArray();
        for (WatchedMovie wm : watchHistory.getMovies()) {
            final JSONObject obj = movieToJSON(wm);
            obj.put(JsonKeys.WH_WATCHED_AT, wm.getWatchedDate().toString());
            moviesJson.put(obj);
        }
        json.put(JsonKeys.WH_MOVIES, moviesJson);

        return json;
    }

    private WatchHistory jsonToWatchHistory(JSONObject json, User owner) {
        final WatchHistory wh = new WatchHistory(
                json.getString(JsonKeys.WH_ID),
                owner
        );

        final JSONArray moviesJson = json.optJSONArray(JsonKeys.WH_MOVIES);
        if (moviesJson != null) {
            for (int i = 0; i < moviesJson.length(); i++) {
                final JSONObject obj = moviesJson.getJSONObject(i);
                final Movie m = jsonToMovie(obj);
                final LocalDateTime watchedAt = LocalDateTime.parse(obj.getString(JsonKeys.WH_WATCHED_AT));
                wh.addWatchedMovie(new WatchedMovie(m, watchedAt));
            }
        }

        return wh;
    }

    /**
     * JSON key constants used for persistence.
     */
    private static final class JsonKeys {

        // Root-level
        static final String CURRENT_USER = "currentUser";
        static final String USERS = "users";

        // User
        static final String USERNAME = "username";
        static final String PASSWORD = "password";
        static final String WATCHLISTS = "watchlists";
        static final String REVIEWS = "reviews";
        static final String WATCH_HISTORY = "watchHistory";

        // WatchList
        static final String WL_ID = "id";
        static final String WL_NAME = "name";
        static final String WL_DATE_CREATED = "dateCreated";
        static final String WL_MOVIES = "movies";

        // Movie
        static final String MOVIE_ID = "id";
        static final String MOVIE_TITLE = "title";
        static final String MOVIE_PLOT = "plot";
        static final String MOVIE_GENRE_IDS = "genreIds";
        static final String MOVIE_RELEASE_DATE = "releaseDate";
        static final String MOVIE_RATING = "rating";
        static final String MOVIE_POSTER_URL = "posterUrl";
        static final String MOVIE_POPULARITY = "popularity";

        // Review
        static final String REVIEW_ID = "id";
        static final String REVIEW_MOVIE = "movie";
        static final String REVIEW_RATING = "rating";
        static final String REVIEW_COMMENT = "comment";
        static final String REVIEW_CREATED_AT = "createdAt";

        // WatchHistory / WatchedMovie
        static final String WH_ID = "id";
        static final String WH_MOVIES = "movies";
        static final String WH_WATCHED_AT = "watchedAt";

        private JsonKeys() {
        }
    }
}
