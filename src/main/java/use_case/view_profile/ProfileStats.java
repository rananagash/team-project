package use_case.view_profile;

/**
 * Contains statistics for a user's profile.
 */
public class ProfileStats {
    private final int watchlistCount;
    private final int reviewCount;
    private final int watchedMoviesCount;

    public ProfileStats(int watchlistCount, int reviewCount, int watchedMoviesCount) {
        this.watchlistCount = watchlistCount;
        this.reviewCount = reviewCount;
        this.watchedMoviesCount = watchedMoviesCount;
    }

    public int getWatchlistCount() { return watchlistCount; }
    public int getReviewCount() { return reviewCount; }
    public int getWatchedMoviesCount() { return watchedMoviesCount; }
}