package interface_adapter.add_to_watchlist;

import use_case.add_to_watchlist.AddWatchListOutputBoundary;
import use_case.add_to_watchlist.AddWatchListResponseModel;
import view.AddToWatchListPopup;

import javax.swing.*;

public class AddWatchListPresenter implements AddWatchListOutputBoundary {

    private final AddToWatchListPopup popup;

    //TODO(Alana): This currently is set to take in a popup parameter, but it might be more correct to have it
    // take the viewManagerModel and a viewModel? I'm not sure if it's different because it is a popup model,
    // not a refresh of a full view? Will need to validate this before final product
    public AddWatchListPresenter(AddToWatchListPopup popup) {
        this.popup = popup;
    }

    @Override
    public void prepareSuccessView(AddWatchListResponseModel responseModel) {
        popup.showResult(responseModel.getMessage());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        popup.showResult(errorMessage);
    }
}

