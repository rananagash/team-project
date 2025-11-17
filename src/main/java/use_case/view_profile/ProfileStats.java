package use_case.view_profile;

import java.time.LocalDateTime;

/**
 * Contains statistics for a user's profile.
 */
public class ProfileStats {
    private final int watchlistCount;
    private final int reviewCount;
    private final int watchedMoviesCount;
    private final LocalDateTime accountCreationDate;

    public ProfileStats(int watchlistCount, int reviewCount, int watchedMoviesCount) {
        this.watchlistCount = watchlistCount;
        this.reviewCount = reviewCount;
        this.watchedMoviesCount = watchedMoviesCount;
        this.accountCreationDate = null; // TODO: Add account creation date support when User class is updated
    }

    public int getWatchlistCount() { return watchlistCount; }
    public int getReviewCount() { return reviewCount; }
    public int getWatchedMoviesCount() { return watchedMoviesCount; }
    public LocalDateTime getAccountCreationDate() { return accountCreationDate; }
}