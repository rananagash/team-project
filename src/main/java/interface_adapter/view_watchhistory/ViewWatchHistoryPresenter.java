package interface_adapter.view_watchhistory;

import use_case.view_watchhistory.ViewWatchHistoryOutputBoundary;
import use_case.view_watchhistory.ViewWatchHistoryResponseModel;

public class ViewWatchHistoryPresenter implements ViewWatchHistoryOutputBoundary {

    @Override
    public void prepareSuccessView(ViewWatchHistoryResponseModel responseModel) {
        /*
         * TODO(Jiaqi Zhao): Render the watch history in the UI (list, timeline, etc.).
         */
    }

    @Override
    public void prepareFailView(String errorMessage) {
        /*
         * TODO(Jiaqi Zhao): Define how errors should be presented to the user.
         */
    }
}

