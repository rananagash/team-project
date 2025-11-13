package interface_adapter.review_movie;

import use_case.review_movie.ReviewMovieInputBoundary;
import use_case.review_movie.ReviewMovieRequestModel;

public class ReviewMovieController {

    private final ReviewMovieInputBoundary interactor;

    public ReviewMovieController(ReviewMovieInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void submitReview(String userName, String movieId, int rating, String comment) {
        ReviewMovieRequestModel requestModel =
                new ReviewMovieRequestModel(userName, movieId, rating, comment);
        interactor.execute(requestModel);
    }
}
