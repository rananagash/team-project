package use_case.view_watchlists;

import java.util.ArrayList;
import java.util.List;

import entity.Movie;
import entity.User;
import entity.WatchList;
import use_case.common.UserDataAccessInterface;

/**
 * Interactor for the View Watch List use case.
 * Retrieves the user's watchlists, validates input, and prepares
 * the response model to be sent to the presenter.
 */
public class ViewWatchListsInteractor implements ViewWatchListsInputBoundary {

    private final UserDataAccessInterface userDataAccess;
    private final ViewWatchListsOutputBoundary presenter;

    /**
     * Creates a new Interactor for viewing watchlists.
     *
     * @param userDataAccess the user data access object
     * @param presenter the presenter
     */
    public ViewWatchListsInteractor(UserDataAccessInterface userDataAccess,
                                    ViewWatchListsOutputBoundary presenter) {
        this.userDataAccess = userDataAccess;
        this.presenter = presenter;
    }

    /**
     * Executes the View Watch Lists use case: loads the user's watchlists,
     * validates the selected index, extracts movies, and sends the results to
     * the presenter.
     *
     * <p>On failure (e.g. user not found, no watchlists), the presenter's
     * failure method is invoked.
     *
     * @param requestModel the request model with the data to be added.
     */
    @Override
    public void execute(ViewWatchListsRequestModel requestModel) {
        final String username = requestModel.getUsername();
        final User user = userDataAccess.getUser(username);
        if (user == null) {
            presenter.prepareFailureView("User not found.");
            return;
        }

        final List<WatchList> lists = user.getWatchLists();
        if (lists.isEmpty()) {
            presenter.prepareFailureView("User has no watchlists.");
            return;
        }

        final List<ViewWatchListsResponseModel.WatchListInfo> listInfos = new ArrayList<>();
        for (WatchList watchList : lists) {
            listInfos.add(new ViewWatchListsResponseModel.WatchListInfo(
                    watchList.getWatchListId(),
                    watchList.getName()
            ));
        }

        int index = requestModel.getSelectedIndex();
        if (index < 0 || index >= listInfos.size()) {
            index = 0;
        }
        final WatchList selected = lists.get(index);

        final List<ViewWatchListsResponseModel.MovieInfo> movies = new ArrayList<>();
        for (Movie movie : selected.getMovies()) {
            movies.add(new ViewWatchListsResponseModel.MovieInfo(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getPlot(),
                    movie.getGenreIds(),
                    movie.getReleaseDate(),
                    movie.getRating(),
                    movie.getPosterUrl()
            ));
        }

        presenter.prepareSuccessView(new ViewWatchListsResponseModel(
                listInfos,
                index,
                movies,
                username
        ));
    }
}
