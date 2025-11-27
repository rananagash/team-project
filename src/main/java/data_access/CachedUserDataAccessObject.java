package data_access;

import entity.User;
import use_case.change_password.ChangePasswordUserDataAccessInterface;
import use_case.common.UserDataAccessInterface;
import use_case.view_profile.ProfileStats;
import use_case.view_profile.ViewProfileUserDataAccessInterface;

/**
 * A caching wrapper that combines a persistent user store
 * ({@link FileUserDataAccessObject}) with an in-memory cache
 * ({@link InMemoryUserDataAccessObject}).
 *
 * <p>Reads are served from the in-memory cache whenever possible,
 * while writes go to both the cache and the persistent store.
 *
 * <p>This class should be used by the application's dependency-injection layer.
 */
public class CachedUserDataAccessObject implements UserDataAccessInterface,
                                                   ChangePasswordUserDataAccessInterface,
                                                   ViewProfileUserDataAccessInterface {

    private final UserDataAccessInterface persistentStore;
    private final InMemoryUserDataAccessObject cache;

    /**
     * Constructs a new cached DAO using the provided persistent store.
     *
     * <p>The cache is initially synchronized only for the currently logged-in user.
     * Additional users are loaded lazily into the cache the first time they are accessed.
     *
     * @param persistentStore a persistent implementation such as {@link FileUserDataAccessObject}
     */
    public CachedUserDataAccessObject(UserDataAccessInterface persistentStore) {
        this.persistentStore = persistentStore;
        this.cache = new InMemoryUserDataAccessObject();

        preloadCache();
    }

    private void preloadCache() {
        // Users are lazy loaded into the cache as soon as they are accessed, not preloaded all at once.
        cache.setCurrentUsername(persistentStore.getCurrentUsername());
    }

    // ===============================================================================================================
    // ================================================ PUBLIC METHODS ===============================================
    // ===============================================================================================================

    /**
     * Saves the sate of a {@link User} into both the in-memory cache and the persistent store.
     *
     * @param user the user to persist (must not be {@code null})
     */
    @Override
    public void save(User user) {
        // Update both in-memory cache and file storage
        cache.save(user);
        persistentStore.save(user);
    }

    /**
     * Retrieves the {@link User} associated with the given username.
     *
     * <p>This method first attempts to retrieve the user from the in-memory cache.
     * If the user is not in the cache, it is loaded from the persistent store and
     * then cached for future lookups.
     *
     * @param userName the username to look up (must not be {@code null})
     * @return the corresponding user, or {@code null} if no such user exists
     */
    @Override
    public User getUser(String userName) {
        User user = cache.getUser(userName);
        if (user != null) {
            return user;
        }

        // if not found in cache, load from persistent store
        user = persistentStore.getUser(userName);

        // cache it for future use
        if (user != null) {
            cache.save(user);
        }
        return user;
    }

    /**
     * Returns whether a user with the specified username exists.
     *
     * <p>The cache is checked first for efficiency. If not found, the persistent store is checked.
     *
     * @param username the username to check
     * @return {@code true} if a user with this username exists,
     *         {@code false} otherwise
     */
    @Override
    public boolean existsByName(String username) {
        if (cache.existsByName(username)) {
            return true;
        }

        return persistentStore.existsByName(username);
    }

    /**
     * Sets the username of the currently authenticated user.
     *
     * @param username the username to record, or {@code null} to clear
     */
    @Override
    public void setCurrentUsername(String username) {
        cache.setCurrentUsername(username);
        persistentStore.setCurrentUsername(username);
    }

    /**
     * Returns the username of the currently authenticated user.
     *
     * @return the current username, or {@code null} if no user is logged in
     */
    @Override
    public String getCurrentUsername() {
        return cache.getCurrentUsername();
    }

    /**
     * Updates the password for the existing user.
     *
     * @param user the user whose password is to be updated
     */
    @Override
    public void changePassword(User user) {
        save(user);
    }

    @Override
    public ProfileStats getUserStats(String username) {
        return null;
        //TODO Rana
    }
}
