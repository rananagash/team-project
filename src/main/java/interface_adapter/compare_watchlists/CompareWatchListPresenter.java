package interface_adapter.compare_watchlists;

import use_case.compare_watchlists.CompareWatchListOutputBoundary;
import use_case.compare_watchlists.CompareWatchListResponseModel;

public class CompareWatchListPresenter implements CompareWatchListOutputBoundary {

    @Override
    public void prepareSuccessView(CompareWatchListResponseModel responseModel) {
        /*
         * TODO(Rana Nagash): Render the comparison in the UI, e.g., show overlaps and unique movies.
         */
    }

    @Override
    public void prepareFailView(String errorMessage) {
        /*
         * TODO(Rana Nagash): Surface the error or prompt the user to adjust input.
         */
    }
}

