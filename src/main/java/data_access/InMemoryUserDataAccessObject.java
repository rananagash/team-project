package data_access;

import java.util.HashMap;
import java.util.Map;

import entity.User;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.common.UserDataAccessInterface;

public class InMemoryUserDataAccessObject implements UserDataAccessInterface,
                                                     ChangePasswordUserDataAccessInterface {

    private final Map<String, User> accounts = new HashMap<>();
    private String currentUsername;

    // ===============================================================================================================
    // =========================================== PUBLIC INTERFACE METHODS ==========================================
    // ===============================================================================================================

    /**
     * Saves the full state of a user to the in-memory store.
     *
     * @param user the user to persist (must not be {@code null})
     */
    @Override
    public void save(User user) {
        accounts.put(user.getUserName(), user);
    }

    /**
     * Retrieves a user by username.
     *
     * @param userName the username to look up (must not be {@code null})
     * @return the {@link User} if found or {@code null} otherwise
     */
    @Override
    public User getUser(String userName) {
        return accounts.get(userName);
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
}

