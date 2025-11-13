package use_case.view_watchhistory;

public class ViewWatchHistoryRequestModel {

    private final String userName;

    public ViewWatchHistoryRequestModel(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}

