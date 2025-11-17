package use_case.view_profile;

/**
 * Input data for the View Profile use case.
 */
public class ViewProfileInputData {
    private final String username;

    public ViewProfileInputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}