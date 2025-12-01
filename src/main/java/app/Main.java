package app;

import javax.swing.JFrame;

/**
 * Application entry point for the MovieNight application.
 *
 * <p>This class initializes all views, use cases, and controllers
 * through {@link AppBuilder}, constructs the application window,
 * and displays the UI to the user.</p>
 */
public class Main {

    /**
     * Launches the MovieNight application.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();
        final JFrame application = appBuilder
                .addLoginView()
                .addSignupView()
                .addLoggedInView()
                .addProfileView()
                .addViewWatchListsView()
                .addSignupUseCase()
                .addLoginUseCase()
                .addLogoutUseCase()
                .addSearchMoviesUseCase()
                .addChangePasswordUseCase()
                .addViewProfileUseCase()
                .addViewWatchListsUseCase()
                .addFilterMoviesUseCase()
                .addViewWatchHistoryUseCase()
                .addAddToWatchListUseCase()
                .addRecordWatchHistoryPopup()
                .addAddReviewPopup()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
