package use_case.search_movie;

public interface SearchMovieOutputBoundary {

    void prepareSuccessView(SearchMovieResponseModel responseModel);

    void prepareFailView(String errorMessage);
}

