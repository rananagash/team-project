package use_case.add_to_watchlist;

/**
 * The Input boundary for the Add to Watch List use case.
 *
 * <p>Defines the method the controller may call.</p>
 */
public interface AddWatchListInputBoundary {

    /**
     * Executes the Add to Watch List use case with the given request model.
     * @param requestModel the request model with the data to be added.
     */
    void execute(AddWatchListRequestModel requestModel);
}
