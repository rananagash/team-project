package interface_adapter.view_watchhistory;

import use_case.view_watchhistory.ViewWatchHistoryInputBoundary;
import use_case.view_watchhistory.ViewWatchHistoryRequestModel;

public class ViewWatchHistoryController {

    private final ViewWatchHistoryInputBoundary interactor;

    public ViewWatchHistoryController(ViewWatchHistoryInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void loadHistory(String userName) {
        ViewWatchHistoryRequestModel requestModel = new ViewWatchHistoryRequestModel(userName);
        interactor.execute(requestModel);
    }
}
