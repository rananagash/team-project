package use_case.add_to_watchlist;

public class AddWatchListResponseModel {

    private final boolean success;
    private final String message;

    public AddWatchListResponseModel(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getMessage() {return message;}
    public boolean isSuccess() {return success;}
}

