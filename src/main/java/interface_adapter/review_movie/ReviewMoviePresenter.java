package interface_adapter.review_movie;

import use_case.review_movie.ReviewMovieOutputBoundary;
import use_case.review_movie.ReviewMovieResponseModel;

public class ReviewMoviePresenter implements ReviewMovieOutputBoundary {

    @Override
    public void prepareSuccessView(ReviewMovieResponseModel responseModel) {
        // TODO: push success info to the UI
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // TODO: show error info
    }
}