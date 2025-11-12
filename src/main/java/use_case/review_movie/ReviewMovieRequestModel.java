package use_case.review_movie;

public class ReviewMovieRequestModel {

    private final String userName;
    private final String movieId;
    private final int rating;
    private final String comment;

    public ReviewMovieRequestModel(String userName, String movieId, int rating, String comment) {
        this.userName = userName;
        this.movieId = movieId;
        this.rating = rating;
        this.comment = comment;
    }

    public String getUserName() {
        return userName;
    }

    public String getMovieId() {
        return movieId;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}

