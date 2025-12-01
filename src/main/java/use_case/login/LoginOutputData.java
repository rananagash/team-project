package use_case.login;

import java.util.List;

/**
 * Output Data for the Login Use Case.
 */
public class LoginOutputData {

    private final String username;
    private final List<String> watchListIds;
    private final List<String> watchListNames;

    public LoginOutputData(String username, List<String> watchListIds, List<String> watchListNames) {
        this.username = username;
        this.watchListIds = watchListIds;
        this.watchListNames = watchListNames;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getWatchListIds() {
        return watchListIds;
    }

    public List<String> getWatchListNames() {
        return watchListNames;
    }

}
