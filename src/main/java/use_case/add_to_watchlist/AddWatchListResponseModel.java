package use_case.add_to_watchlist;

/**
 * Response model for the Add to Watch List use case.
 *
 * <p>Contains:
 * <ul>
 *     <li>A boolean indicating success or failure</li>
 *     <li>a user-facing message for display</li>
 * </ul>
 */
public class AddWatchListResponseModel {

    private final boolean success;
    private final String message;

    /**
     * Constructs a response model with the outcome of the Add to Watch List use case.
     * @param success whether the movie was added, or failure the movie already existed on the Watch List
     * @param message human-readable message for display of result
     */
    public AddWatchListResponseModel(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getMessage() {return message;}
    public boolean isSuccess() {return success;}
}

