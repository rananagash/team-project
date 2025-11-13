package interface_adapter.add_to_watchlist;

import use_case.add_to_watchlist.AddWatchListInputBoundary;
import use_case.add_to_watchlist.AddWatchListRequestModel;

public class AddWatchListController {

    private final AddWatchListInputBoundary interactor;

    public AddWatchListController(AddWatchListInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void addMovieToWatchList(String userName, String watchListName, String movieId) {
        AddWatchListRequestModel requestModel = new AddWatchListRequestModel(userName, watchListName, movieId);
        interactor.execute(requestModel);
    }
}
