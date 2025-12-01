package use_case.search_movie;

public class SearchMovieRequestModel {

    private final String query;
    private final int page;

    public SearchMovieRequestModel(String query,  int page) {

        this.query = query;
        this.page = page;
    }

    public String getQuery() {
        return query;
    }

    public int getPage() {
        return page;
    }
}

