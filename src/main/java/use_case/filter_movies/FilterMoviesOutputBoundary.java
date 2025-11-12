package use_case.filter_movies;

public interface FilterMoviesOutputBoundary {

    void prepareSuccessView(FilterMoviesResponseModel responseModel);

    void prepareFailView(String errorMessage);
}

