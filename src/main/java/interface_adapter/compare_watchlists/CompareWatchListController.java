package interface_adapter.compare_watchlists;

import use_case.compare_watchlists.CompareWatchListInputBoundary;
import use_case.compare_watchlists.CompareWatchListRequestModel;

public class CompareWatchListController {

    private final CompareWatchListInputBoundary interactor;

    public CompareWatchListController(CompareWatchListInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void compare(String baseUserName, String targetUserName) {
        CompareWatchListRequestModel requestModel =
                new CompareWatchListRequestModel(baseUserName, targetUserName);
        interactor.execute(requestModel);
    }
}
