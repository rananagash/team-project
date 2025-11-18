package use_case.review_movie;

public class ReviewMovieResponseModel {

    private final String reviewId;
    private final String userName;
    private final String movieId;
    private final int rating;
    private final String comment;

    public ReviewMovieResponseModel(String reviewId,
                                    String userName,
                                    String movieId,
                                    int rating,
                                    String comment) {
        this.reviewId = reviewId;
        this.userName = userName;
        this.movieId = movieId;
        this.rating = rating;
        this.comment = comment;
    }

    public String getReviewId() {
        return reviewId;
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

    //public String getMessage() {return message;}
}

