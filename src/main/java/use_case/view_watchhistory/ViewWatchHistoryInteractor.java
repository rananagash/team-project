package use_case.view_watchhistory;

import entity.User;
import use_case.common.UserDataAccessInterface;

public class ViewWatchHistoryInteractor implements ViewWatchHistoryInputBoundary {

    private final UserDataAccessInterface userDataAccessInterface;
    private final ViewWatchHistoryOutputBoundary presenter;

    public ViewWatchHistoryInteractor(UserDataAccessInterface userDataAccessInterface,
                                      ViewWatchHistoryOutputBoundary presenter) {
        this.userDataAccessInterface = userDataAccessInterface;
        this.presenter = presenter;
    }

    @Override
    public void execute(ViewWatchHistoryRequestModel requestModel) {
        User user = userDataAccessInterface.getUser(requestModel.getUserName());

        if (user == null) {
            presenter.prepareFailView("User not found: " + requestModel.getUserName());
            return;
        }

        if (user.getWatchHistory() == null) {
            presenter.prepareFailView("This user has no watch history yet.");
        }

        presenter.prepareSuccessView(
                new ViewWatchHistoryResponseModel(
                user.getUserName(),
                user.getWatchHistory().getMovies()
                )
        );
    }
}

