package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.review_movie.ReviewMovieController;
import interface_adapter.review_movie.ReviewMovieViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.view_watchhistory.ViewWatchHistoryController;

import javax.swing.*;
import java.awt.*;

/**
 * Logged In View (final corrected version)
 */
public class LoggedInView extends JPanel {

    private final LoggedInViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    private ReviewMovieController reviewMovieController;
    private ReviewMovieViewModel reviewMovieViewModel;
    private LogoutController logoutController;
    private ChangePasswordController changePasswordController;
    private ViewWatchHistoryController viewWatchHistoryController;

    public LoggedInView(LoggedInViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;

        setLayout(new BorderLayout());

        JLabel label = new JLabel("Logged In Home");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }

    public void setReviewMovieController(ReviewMovieController controller) {
        this.reviewMovieController = controller;
    }

    public void setReviewMovieViewModel(ReviewMovieViewModel viewModel) {
        this.reviewMovieViewModel = viewModel;
    }

    public void setLogoutController(LogoutController controller) {
        this.logoutController = controller;
    }

    public void setChangePasswordController(ChangePasswordController controller) {
        this.changePasswordController = controller;
    }

    public void setViewWatchHistoryController(ViewWatchHistoryController controller) {
        this.viewWatchHistoryController = controller;
    }

    public String getViewName() {
        return viewModel.getViewName();
    }
}
