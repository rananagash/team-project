package interface_adapter.add_to_watchlist;

import use_case.add_to_watchlist.AddWatchListOutputBoundary;
import use_case.add_to_watchlist.AddWatchListResponseModel;

public class AddWatchListPresenter implements AddWatchListOutputBoundary {

    @Override
    public void prepareSuccessView(AddWatchListResponseModel responseModel) {
        /*
         * TODO(Alana Watson): Push the success result to the UI layer, e.g., refresh the list or show a toast.
         */
    }

    @Override
    public void prepareFailView(String errorMessage) {
        /*
         * TODO(Alana Watson): Display the error message according to the UI design.
         */
    }
}

