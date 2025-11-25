package interface_adapter.compare_watchlists;

import entity.Movie;

import java.util.ArrayList;
import java.util.List;

public class CompareWatchListState {
    private String baseUserName = "";
    private String targetUserName = "";
    private List<Movie> commonMovies = new ArrayList<>();
    private List<Movie> baseOnlyMovies = new ArrayList<>();
    private List<Movie> targetOnlyMovies = new ArrayList<>();
    private String error;

    public CompareWatchListState(CompareWatchListState copy) {
        baseUserName = copy.baseUserName;
        targetUserName = copy.targetUserName;
        commonMovies = new ArrayList<>(copy.commonMovies);
        baseOnlyMovies = new ArrayList<>(copy.baseOnlyMovies);
        targetOnlyMovies = new ArrayList<>(copy.targetOnlyMovies);
        error = copy.error;
    }

    public CompareWatchListState() {
    }

    public String getBaseUserName() {
        return baseUserName;
    }

    public void setBaseUserName(String baseUserName) {
        this.baseUserName = baseUserName;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
    }

    public List<Movie> getCommonMovies() {
        return commonMovies;
    }

    public void setCommonMovies(List<Movie> commonMovies) {
        this.commonMovies = commonMovies;
    }

    public List<Movie> getBaseOnlyMovies() {
        return baseOnlyMovies;
    }

    public void setBaseOnlyMovies(List<Movie> baseOnlyMovies) {
        this.baseOnlyMovies = baseOnlyMovies;
    }

    public List<Movie> getTargetOnlyMovies() {
        return targetOnlyMovies;
    }

    public void setTargetOnlyMovies(List<Movie> targetOnlyMovies) {
        this.targetOnlyMovies = targetOnlyMovies;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
