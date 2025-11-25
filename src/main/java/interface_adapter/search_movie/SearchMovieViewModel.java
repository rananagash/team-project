package interface_adapter.search_movie;

import entity.Movie;
import interface_adapter.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchMovieViewModel extends ViewModel<SearchMovieState> {

    public static final String TITLE_LABEL = "Search Movies";
    public static final String SEARCH_BUTTON_LABEL = "Search";

    public SearchMovieViewModel() {
        super("search movies");
        setState(new SearchMovieState());
    }
}
