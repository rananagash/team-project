package use_case.add_to_watchlist;

import entity.Movie;
import entity.User;
import entity.WatchList;
import use_case.common.MovieGateway;
import use_case.common.UserGateway;

import java.time.LocalDateTime;
import java.util.UUID;

public class AddWatchListInteractor implements AddWatchListInputBoundary {

    private final UserGateway userGateway;
    private final MovieGateway movieGateway;
    private final AddWatchListOutputBoundary presenter;

    public AddWatchListInteractor(UserGateway userGateway,
                                  MovieGateway movieGateway,
                                  AddWatchListOutputBoundary presenter) {
        this.userGateway = userGateway;
        this.movieGateway = movieGateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddWatchListRequestModel requestModel) {
        userGateway.findByUserName(requestModel.getUserName()).ifPresentOrElse(user -> handleUser(user, requestModel),
                () -> presenter.prepareFailView("User not found: " + requestModel.getUserName()));
    }

    private void handleUser(User user, AddWatchListRequestModel requestModel) {
        WatchList watchList = user.getWatchListByName(requestModel.getWatchListName())
                .orElseGet(() -> createWatchList(user, requestModel.getWatchListName()));

        /*
         * TODO(Alana Watson): Handle duplicate movies, empty inputs, and update feedback messages per team decision.
         */
        movieGateway.findById(requestModel.getMovieId()).ifPresentOrElse(movie -> addMovie(user, watchList, movie),
                () -> presenter.prepareFailView("Movie not found: " + requestModel.getMovieId()));
    }

    private WatchList createWatchList(User user, String watchListName) {
        WatchList watchList = new WatchList(UUID.randomUUID().toString(), user, watchListName, LocalDateTime.now());
        user.addWatchList(watchList);
        return watchList;
    }

    private void addMovie(User user, WatchList watchList, Movie movie) {
        watchList.addMovie(movie);
        userGateway.save(user);
        presenter.prepareSuccessView(new AddWatchListResponseModel(watchList.getName(), watchList.getMovies()));
    }
}

