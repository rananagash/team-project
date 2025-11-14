package interface_adapter.add_to_watchlist;

import entity.Movie;
import entity.User;
import entity.WatchList;
import use_case.add_to_watchlist.AddWatchListInputBoundary;
import use_case.add_to_watchlist.AddWatchListRequestModel;

public class AddWatchListController {

    private final AddWatchListInputBoundary interactor;

    public AddWatchListController(AddWatchListInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void addMovieToWatchList(User user, Movie movie, WatchList watchList) {
        AddWatchListRequestModel requestModel = new AddWatchListRequestModel(user, movie, watchList);
        interactor.execute(requestModel);
    }
}
