package use_case.view_watchhistory;

import entity.User;
import use_case.common.UserGateway;

public class ViewWatchHistoryInteractor implements ViewWatchHistoryInputBoundary {

    private final UserGateway userGateway;
    private final ViewWatchHistoryOutputBoundary presenter;

    public ViewWatchHistoryInteractor(UserGateway userGateway,
                                      ViewWatchHistoryOutputBoundary presenter) {
        this.userGateway = userGateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(ViewWatchHistoryRequestModel requestModel) {
        userGateway.findByUserName(requestModel.getUserName()).ifPresentOrElse(user -> {
            if (user.getWatchHistory() == null) {
                presenter.prepareFailView("This user has no watch history yet.");
                return;
            }
            presenter.prepareSuccessView(new ViewWatchHistoryResponseModel(
                    user.getUserName(),
                    user.getWatchHistory().getMovies()));
        }, () -> presenter.prepareFailView("User not found: " + requestModel.getUserName()));

        /*
         * TODO(Jiaqi Zhao): Add entry points for writing watch history (e.g., after playback completes); the interactor currently supports read-only flow.
         */
    }
}

