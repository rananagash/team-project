package use_case.add_to_watchlist;

import entity.Movie;
import entity.User;
import entity.WatchList;
import use_case.common.UserGateway;


public class AddWatchListInteractor implements AddWatchListInputBoundary {

    private final AddWatchListOutputBoundary presenter;
    private final UserGateway userGateway;

    public AddWatchListInteractor(AddWatchListOutputBoundary presenter,
                                  UserGateway userGateway) {
        this.presenter = presenter;
        this.userGateway = userGateway;
    }

    @Override
    public void execute(AddWatchListRequestModel requestModel) {
        User user = requestModel.getUser();
        Movie movie = requestModel.getMovie();
        WatchList watchList = requestModel.getWatchList();

        boolean success;
        String message;

        if (!watchList.getMovies().contains(movie)) {
            success = false;
            message = "\"" + movie.getTitle() + "\" is already in " + watchList.getName() + "\"";
        } else {
            watchList.addMovie(movie);
            userGateway.save(user);
            success = true;
            message = "\"" + movie.getTitle() + "\" successfully added to " + watchList.getName() + "\"";
        }

        AddWatchListResponseModel responseModel = new AddWatchListResponseModel(success, message);
        if (success) {
            presenter.prepareSuccessView(responseModel);
        } else {
            presenter.prepareFailView(message);
        }
    }
}

