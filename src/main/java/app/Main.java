package app;

import data_access.TMDbMovieDataAccessObject;
import entity.Movie;

import javax.swing.*;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
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
                .addViewWatchHistoryUseCase()
                .addFilterMoviesUseCase()
                .addAddToWatchListUseCase()
                .addRecordWatchHistoryPopup()
                .addAddReviewPopup()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);

        // This is just testing out the data access object.
        // the id of Avengers Endgame is 299534
        // Run in background thread to avoid blocking UI startup
        new Thread(() -> {
            try {
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
            } catch (Exception e) {
                System.err.println("Error in test API call: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}