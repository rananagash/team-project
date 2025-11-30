package interface_adapter.logged_in;

import entity.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * The State information representing the logged-in user.
 */
public class LoggedInState {
    private String username = "";

    private String password = "";
    private String passwordError;

    private List<Movie> searchResults = new ArrayList<>();
    private int currentPage = 1;
    private int totalPages = 1;
    private String lastQuery = "";
    private String searchError;


    public LoggedInState(LoggedInState copy) {
        username = copy.username;
        password = copy.password;
        passwordError = copy.passwordError;
        searchResults = new ArrayList<>(copy.searchResults);
        currentPage = copy.currentPage;
        totalPages = copy.totalPages;
        lastQuery = copy.lastQuery;
        searchError = copy.searchError;
    }
    // Because of the previous copy constructor, the default constructor must be explicit.
    public LoggedInState() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public List<Movie> getSearchResults() { return searchResults; }
    public void setSearchResults(List<Movie> searchResults) {
        this.searchResults = searchResults != null ? new ArrayList<>(searchResults) : new ArrayList<>();
    }

    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public String getLastQuery() { return lastQuery; }
    public void setLastQuery(String lastQuery) { this.lastQuery = lastQuery; }

    public String getSearchError() { return searchError; }
    public void setSearchError(String searchError) { this.searchError = searchError; }

    @Override
    public String toString() {
        return "LoggedInState{" +
                "username='" + username + '\'' +
                ", searchResultsCount=" + searchResults.size() +
                ", currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                ", lastQuery='" + lastQuery + '\'' +
                '}';
    }
}
