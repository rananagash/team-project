package use_case.compare_watchlists;

import entity.Movie;
import entity.User;
import entity.WatchList;
import use_case.common.UserDataAccessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CompareWatchListInteractor implements CompareWatchListInputBoundary {

    private final UserDataAccessInterface userDataAccessInterface;
    private final CompareWatchListOutputBoundary presenter;

    public CompareWatchListInteractor(UserDataAccessInterface userDataAccessInterface,
                                      CompareWatchListOutputBoundary presenter) {
        this.userDataAccessInterface = userDataAccessInterface;
        this.presenter = presenter;
    }

    @Override
    public void execute(CompareWatchListRequestModel requestModel) {
        User baseUser = userDataAccessInterface.getUser(requestModel.getBaseUserName());
        User targetUser = userDataAccessInterface.getUser(requestModel.getTargetUserName());

        if (baseUser == null || targetUser == null) {
            presenter.prepareFailView("Both users must exist before comparing watchlists.");
            return;
        }

        /*
         * TODO(Rana Nagash): Decide which watchlists to compare (default vs. selected) and handle empty lists, deduping, etc.
         */
        List<Movie> baseMovies = aggregateMovies(baseUser);
        List<Movie> targetMovies = aggregateMovies(targetUser);

        Set<String> targetMovieIds = targetMovies.stream()
                .map(Movie::getMovieId)
                .collect(Collectors.toSet());
        Set<String> baseMovieIds = baseMovies.stream()
                .map(Movie::getMovieId)
                .collect(Collectors.toSet());

        List<Movie> commonMovies = baseMovies.stream()
                .filter(movie -> targetMovieIds.contains(movie.getMovieId()))
                .collect(Collectors.toList());

        List<Movie> baseOnlyMovies = baseMovies.stream()
                .filter(movie -> !targetMovieIds.contains(movie.getMovieId()))
                .collect(Collectors.toList());

        List<Movie> targetOnlyMovies = targetMovies.stream()
                .filter(movie -> !baseMovieIds.contains(movie.getMovieId()))
                .collect(Collectors.toList());

        presenter.prepareSuccessView(new CompareWatchListResponseModel(commonMovies, baseOnlyMovies, targetOnlyMovies));
    }

    private List<Movie> aggregateMovies(User user) {
        List<Movie> movies = new ArrayList<>();
        for (WatchList watchList : user.getWatchLists()) {
            movies.addAll(watchList.getMovies());
        }
        return movies;
    }
}

