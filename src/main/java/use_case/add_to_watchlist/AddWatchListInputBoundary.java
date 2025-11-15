package use_case.add_to_watchlist;

/**
 * The Input boundary for the Add to Watch List use case.
 *
 * <p>Defines the method the controller may call.</p>
 */
public interface AddWatchListInputBoundary {

    void execute(AddWatchListRequestModel requestModel);
}
