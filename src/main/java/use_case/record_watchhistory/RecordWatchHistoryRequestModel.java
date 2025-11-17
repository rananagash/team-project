package use_case.record_watchhistory;

import java.time.LocalDateTime;

public class RecordWatchHistoryRequestModel {

    private final String userName;
    private final String movieId;
    private final LocalDateTime watchedAt;

    public RecordWatchHistoryRequestModel(String userName, String movieId, LocalDateTime watchedAt) {
        this.userName = userName;
        this.movieId = movieId;
        this.watchedAt = watchedAt;
    }

    public String getUserName() {
        return userName;
    }

    public String getMovieId() {
        return movieId;
    }

    public LocalDateTime getWatchedAt() {
        return watchedAt;
    }
}


