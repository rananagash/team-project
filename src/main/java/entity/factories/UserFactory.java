package entity.factories;

import entity.User;

/**
 * Factory class for creating {@link User} entities.
 *
 * <p>This factory provides a centralized way to create User objects, which is useful
 * for dependency injection and allows for potential future extensions (e.g., adding
 * validation, default values, or different user types).
 *
 * <p>The factory is used throughout the application, particularly in:
 * <ul>
 *     <li>Data access objects for deserializing users from storage</li>
 *     <li>Use case interactors for creating new users during signup</li>
 *     <li>Password change operations that may need to recreate user instances</li>
 * </ul>
 *
 * @author Team Project 9
 */
public class UserFactory {

    /**
     * Creates a new {@link User} with the specified username and password.
     *
     * <p>This method delegates to the {@link User} constructor. The created user will
     * automatically have a default watch list initialized with the name "[username]'s Watch List".
     *
     * @param name the username (must not be empty, as per {@link User} constructor requirements)
     * @param password the password (must not be empty, as per {@link User} constructor requirements)
     * @return a new {@link User} instance
     * @throws IllegalArgumentException if username or password is empty (as thrown by {@link User} constructor)
     */
    public User create(String name, String password) {
        return new User(name, password);
    }
}
