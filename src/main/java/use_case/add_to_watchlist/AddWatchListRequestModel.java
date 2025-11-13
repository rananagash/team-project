package use_case.add_to_watchlist;

public class AddWatchListRequestModel {

    private final String userName;
    private final String watchListName;
    private final String movieId;

    public AddWatchListRequestModel(String userName, String watchListName, String movieId) {
        this.userName = userName;
        this.watchListName = watchListName;
        this.movieId = movieId;
    }

    public String getUserName() {
        return userName;
    }

    public String getWatchListName() {
        return watchListName;
    }

    public String getMovieId() {
        return movieId;
    }
}

