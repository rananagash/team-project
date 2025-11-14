package interface_adapter.add_to_watchlist;

import use_case.add_to_watchlist.AddWatchListOutputBoundary;
import use_case.add_to_watchlist.AddWatchListResponseModel;
import view.AddToWatchListPopup;

import javax.swing.*;

public class AddWatchListPresenter implements AddWatchListOutputBoundary {

    private final AddToWatchListPopup popup;

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

