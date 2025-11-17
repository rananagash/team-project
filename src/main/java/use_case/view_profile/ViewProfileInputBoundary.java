package use_case.view_profile;

/**
 * Input boundary for the View Profile use case.
 */
public interface ViewProfileInputBoundary {

    /**
     * Executes the view profile use case.
     * @param viewProfileInputData the input data containing username
     */
    void execute(ViewProfileInputData viewProfileInputData);
}