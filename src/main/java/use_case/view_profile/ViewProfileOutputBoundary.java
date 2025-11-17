package use_case.view_profile;

/**
 * Output boundary for the View Profile use case.
 */
public interface ViewProfileOutputBoundary {

    /**
     * Prepares the success view with profile data.
     * @param viewProfileOutputData the profile data to display
     */
    void prepareSuccessView(ViewProfileOutputData viewProfileOutputData);

    /**
     * Prepares the failure view with an error message.
     * @param error the error message to display
     */
    void prepareFailView(String error);
}