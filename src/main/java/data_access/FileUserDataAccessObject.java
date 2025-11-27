package data_access;

import entity.User;
import entity.factories.UserFactory;
import use_case.add_to_watchlist.AddWatchListUserDataAccessInterface;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.review_movie.ReviewMovieUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.view_profile.ViewProfileUserDataAccessInterface;
import use_case.view_profile.ProfileStats; // Add this import

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * DAO for user data implemented using a File to persist the data.
 */
public class FileUserDataAccessObject implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChangePasswordUserDataAccessInterface,
        LogoutUserDataAccessInterface,
        ViewProfileUserDataAccessInterface,
        AddWatchListUserDataAccessInterface,
        ReviewMovieUserDataAccessInterface {

    private final Path jsonPath;
    private final UserFactory userFactory;
    private final Map<String, User> accounts = new HashMap<>();

    private String currentUsername;

    /**
     * Construct this DAO for saving to and reading from a local file.
     * @param filePath the path of the file to save to
     * @param userFactory factory for creating user objects
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public FileUserDataAccessObject(String filePath, UserFactory userFactory) {
        this.jsonPath = Path.of(filePath);
        this.userFactory = userFactory;

        if (!Files.exists(jsonPath)) {
            save(); //writes empty JSON
        }
        else {
            load(); // reads JSON
        }
    }

    private void save() {
        writeJSON();
    }

    private void load() {
        try {
            String raw = Files.readString(jsonPath);

            if  (raw.isBlank()) {
                return;
            }

            JSONObject root = new JSONObject(raw);

            // restore current user
            this.currentUsername = root.optString("currentUser", null);

            JSONObject usersJson = root.getJSONObject("users");
            if (usersJson != null) {
                for (String username : usersJson.keySet()) {
                    JSONObject u = usersJson.getJSONObject(username);
                    User user = userFactory.create(
                            u.getString("username"),
                            u.getString("password")
                    );
                    accounts.put(username, user);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeJSON() {
        try {
            JSONObject root = new JSONObject();

            // current logged-in user
            root.put("currentUser", currentUsername);

            // users object
            JSONObject usersJson = new JSONObject();
            for (User user : accounts.values()) {
                JSONObject u = new JSONObject();
                u.put("username", user.getUserName());
                u.put("password", user.getPassword());
                usersJson.put(user.getUserName(), u);
            }

            root.put("users", usersJson);

            Files.writeString(jsonPath, root.toString(4));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(User user) {
        accounts.put(user.getUserName(), user);
        writeJSON();
    }

    //TODO: error handling? What if not found?
    @Override
    public User getUser(String username) {
        return accounts.get(username);
    }

    @Override
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
        writeJSON();
    }

    @Override
    public String getCurrentUsername() {
        return "";
    }

    @Override
    public boolean existsByName(String username) {
        return accounts.containsKey(username);
    }

    @Override
    public ProfileStats getUserStats(String username) {
        User user = accounts.get(username);
        if (user == null) {
            return null;
        }

        // Calculate stats - TODO need to adjust based on User class methods
        int watchlistCount = user.getWatchLists() != null ? user.getWatchLists().size() : 0;
        int reviewCount = 0; // TODO need to implement getReviews() in User class
        int watchedMoviesCount = 0; // TODO need to implement getWatchHistory() in User class

        return new ProfileStats(watchlistCount, reviewCount, watchedMoviesCount);
    }

    @Override
    public void changePassword(User user) {
        //TODO: implement
    }
}