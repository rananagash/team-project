package interface_adapter.review_movie;

import use_case.review_movie.ReviewMovieOutputBoundary;
import use_case.review_movie.ReviewMovieResponseModel;

public class ReviewMoviePresenter implements ReviewMovieOutputBoundary {

    private final ReviewMovieViewModel viewModel;

    public ReviewMoviePresenter(ReviewMovieViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ReviewMovieResponseModel responseModel) {
        String msg = "Review added for movie " + responseModel.getMovieId() +
                " by user " + responseModel.getUserName() +
                " with rating " + responseModel.getRating();

        viewModel.setMessage(msg);
        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setError(errorMessage);
        viewModel.firePropertyChange();
    }
}
