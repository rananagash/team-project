package use_case.record_watchhistory;

import java.time.LocalDateTime;

public class RecordWatchHistoryResponseModel {

    private final String userName;
    private final String movieId;
    private final String movieTitle;
    private final LocalDateTime watchedAt;

    public RecordWatchHistoryResponseModel(String userName, String movieId, String movieTitle, LocalDateTime watchedAt) {
        this.userName = userName;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.watchedAt = watchedAt;
    }

    public String getUserName() {
        return userName;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public LocalDateTime getWatchedAt() {
        return watchedAt;
    }
}


