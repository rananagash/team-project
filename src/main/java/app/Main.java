package app;

import data_access.TMDbMovieDataAccessObject;
import entity.Movie;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.view_watchhistory.ViewWatchHistoryController;
import view.LoggedInView;

import javax.swing.*;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws Exception {
        AppBuilder builder = new AppBuilder();

        /*
         * TODO(team): Build the main window here and inject controllers into the UI.
         */
        builder.buildSearchMovieController();
        builder.buildFilterMoviesController();
        builder.buildAddWatchListController();
        builder.buildCompareWatchListController();
        builder.buildReviewMovieController();

        // 创建主窗口
        JFrame application = new JFrame("Movie App");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.setSize(800, 600);

        // 创建 ViewWatchHistoryController（带弹窗，传入主窗口）
        ViewWatchHistoryController viewHistoryController =
                builder.buildViewWatchHistoryController(application);

        // 创建 LoggedInView 和 ViewModel
        LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
        LoggedInView loggedInView = new LoggedInView(loggedInViewModel);

        // 连接 ViewWatchHistoryController 到 LoggedInView
        loggedInView.setViewWatchHistoryController(viewHistoryController);

        // 设置测试用户名（用于demo）
        LoggedInState state = loggedInViewModel.getState();
        state.setUsername("demo_user");
        loggedInViewModel.setState(state);

        // 将 view 添加到主窗口
        application.add(loggedInView);
        application.setLocationRelativeTo(null); // 居中显示
        application.setVisible(true);

        // 可选：添加一些测试数据用于demo
        // 取消下面的注释来添加测试观看历史数据
        /*
        RecordWatchHistoryController recordController = builder.buildRecordWatchHistoryController();
        try {
            recordController.recordMovie("demo_user", "299534"); // Avengers Endgame
            recordController.recordMovie("demo_user", "550");    // Fight Club
            System.out.println("Test watch history data added for demo_user");
        } catch (Exception e) {
            System.out.println("Note: Could not add test data. You can still demo the empty state.");
        }
        */



        // This is just testing out the data access object.
        // the id of Avengers Endgame is 299534
        // it prints out
        TMDbMovieDataAccessObject dao = new TMDbMovieDataAccessObject();
        Optional<Movie> result = dao.findById("299534"); // Avengers Endgame

        if (result.isPresent()) {
            Movie m = result.get();
            System.out.println("Movie fetched successfully:");
            System.out.println("ID: " + m.getMovieId());
            System.out.println("Title: " + m.getTitle());
            System.out.println("Plot: " + m.getPlot());
            System.out.println("Genres: " + m.getGenreIds());
            System.out.println("Rating: " + m.getRating());
        } else {
            System.out.println("Movie not found or error occurred.");
        }






    }
}
