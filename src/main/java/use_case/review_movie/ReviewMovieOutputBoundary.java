package use_case.review_movie;

public interface ReviewMovieOutputBoundary {

    void prepareSuccessView(ReviewMovieResponseModel responseModel);

    void prepareFailView(String errorMessage);
}

