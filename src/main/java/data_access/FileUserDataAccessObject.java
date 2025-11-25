package data_access;

import entity.User;
import entity.factories.UserFactory;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.view_profile.ViewProfileUserDataAccessInterface;
import use_case.view_profile.ProfileStats; // Add this import

import java.io.*;
import java.time.LocalDateTime; // Add this import
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DAO for user data implemented using a File to persist the data.
 */
public class FileUserDataAccessObject implements SignupUserDataAccessInterface,
        ViewProfileUserDataAccessInterface,
        use_case.login.LoginUserDataAccessInterface,
        use_case.common.UserGateway,
        use_case.change_password.ChangePasswordUserDataAccessInterface,
        use_case.logout.LogoutUserDataAccessInterface {

    private static final String HEADER = "username,password";

    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, User> accounts = new HashMap<>();
    private String currentUsername;

    /**
     * Construct this DAO for saving to and reading from a local file.
     *
     * @param csvPath     the path of the file to save to
     * @param userFactory factory for creating user objects
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public FileUserDataAccessObject(String csvPath, UserFactory userFactory) {

        csvFile = new File(csvPath);
        headers.put("username", 0);
        headers.put("password", 1);

        if (csvFile.length() == 0) {
            save();
        } else {

            try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
                final String header = reader.readLine();

                if (!header.equals(HEADER)) {
                    throw new RuntimeException(String.format("header should be%n: %s%n but was:%n%s", HEADER, header));
                }

                String row;
                while ((row = reader.readLine()) != null) {
                    final String[] col = row.split(",");
                    final String username = String.valueOf(col[headers.get("username")]);
                    final String password = String.valueOf(col[headers.get("password")]);
                    final User user = userFactory.create(username, password);
                    accounts.put(username, user);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void save() {
        final BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(csvFile));
            writer.write(String.join(",", headers.keySet()));
            writer.newLine();

            for (User user : accounts.values()) {
                final String line = String.format("%s,%s",
                        user.getUserName(), user.getPassword());
                writer.write(line);
                writer.newLine();
            }

            writer.close();

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void save(User user) {
        accounts.put(user.getUserName(), user);
        this.save();
    }

    @Override
    public boolean existsByName(String identifier) {
        return accounts.containsKey(identifier);
    }

    @Override
    public User getUser(String username) {
        return accounts.get(username);
    }

    @Override
    public User get(String username) {
        return getUser(username);
    }

    @Override
    public void setCurrentUsername(String name) {
        this.currentUsername = name;
    }

    @Override
    public String getCurrentUsername() {
        return this.currentUsername;
    }

    @Override
    public java.util.Optional<User> findByUserName(String userName) {
        return java.util.Optional.ofNullable(accounts.get(userName));
    }

    @Override
    public ProfileStats getUserStats(String username) {
        User user = accounts.get(username);
        if (user == null) {
            return null;
        }

        int watchlistCount = user.getWatchLists() != null ? user.getWatchLists().size() : 0;
        int reviewCount = user.getReviewsByMovieId().size();
        int watchedMoviesCount = user.getWatchHistory() != null ? user.getWatchHistory().getMovies().size() : 0;

        return new ProfileStats(watchlistCount, reviewCount, watchedMoviesCount);
    }

    @Override
    public void changePassword(User user) {
        save(user);
    }
}