package use_case.common;

import entity.User;

/**
 * Provides the core operations for accessing and saving {@link User} entities.
 *
 * <p>This interface defines the shared behaviour required by the majority of user-related use cases.
 * All modifications to a user's watch lists, reviews, or watch history must occur through saving the
 * user object provided to this interface.
 */
public interface UserDataAccessInterface {

    /**
     * Retrieves a user by their unique username.
     *
     * @param userName the username to look up (must not be {@code null})
     * @return the corresponding {@link User}, or {@code null} if no such user exists
     */
    User getUser(String userName);

    /**
     * Persists the full state of the given user, including nested entities such as watch lists, reviews,
     * and watch history.
     *
     * @param user the user to persist (must not be {@code null})
     */
    void save(User user);

    /**
     * Returns whether a user with the given username exists.
     *
     * @param username the username to check
     * @return {@code true} if a user with this username already exists;
     *         {@code false} otherwise
     */
    boolean existsByName(String username);

    /**
     * Records the username of the currently authenticated user.
     *
     * <p>Setting this value to {@code null} clears the current user record.
     *
     * @param username the username to record, or {@code null} to clear
     */
    void setCurrentUsername(String username);

    /**
     * Returns the username of the user currently recorded as logged in.
     *
     * @return the username of the logged-in user, or {@code null} if none is set
     */
    String getCurrentUsername();
}

