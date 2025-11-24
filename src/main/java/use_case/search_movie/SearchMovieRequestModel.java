package use_case.search_movie;

public class SearchMovieRequestModel {

    private final String query;

    public SearchMovieRequestModel(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}

