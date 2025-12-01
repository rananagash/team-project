package use_case.edit_watchedmovie;

import entity.Movie;
import entity.Review;
import entity.User;
import entity.WatchedMovie;
import use_case.common.MovieGateway;
import use_case.common.UserDataAccessInterface;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Interactor for editing a movie in watch history.
 */
public class EditWatchedMovieInteractor implements EditWatchedMovieInputBoundary {

    private final UserDataAccessInterface userDataAccessInterface;
    private final MovieGateway movieGateway;
    private final EditWatchedMovieOutputBoundary presenter;

    public EditWatchedMovieInteractor(UserDataAccessInterface userDataAccessInterface,
                                      MovieGateway movieGateway,
                                      EditWatchedMovieOutputBoundary presenter) {
        this.userDataAccessInterface = userDataAccessInterface;
        this.movieGateway = movieGateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(EditWatchedMovieRequestModel requestModel) {
        User user = userDataAccessInterface.getUser(requestModel.getUserName());

        if (user == null) {
            presenter.prepareFailView("User not found: " + requestModel.getUserName());
            return;
        }

        if (user.getWatchHistory() == null) {
            presenter.prepareFailView("User has no watch history.");
            return;
        }

        // Find the watched movie to update
        WatchedMovie oldWatchedMovie = null;
        for (WatchedMovie wm : user.getWatchHistory().getMovies()) {
            if (requestModel.getMovieId().equals(wm.getMovieId())) {
                oldWatchedMovie = wm;
                break;
            }
        }

        if (oldWatchedMovie == null) {
            presenter.prepareFailView("Movie not found in watch history: " + requestModel.getMovieId());
            return;
        }

        // Get the base Movie entity
        Movie movie = movieGateway.findById(requestModel.getMovieId()).orElse(null);
        if (movie == null) {
            presenter.prepareFailView("Movie not found: " + requestModel.getMovieId());
            return;
        }

        // Validate watched date is not in the future
        LocalDateTime watchedDate = requestModel.getWatchedDate();
        if (watchedDate != null && watchedDate.isAfter(LocalDateTime.now())) {
            presenter.prepareFailView("Watched date cannot be in the future.");
            return;
        }

        // Validate rating if provided
        if (requestModel.getRating() != null) {
            if (requestModel.getRating() < 1 || requestModel.getRating() > 5) {
                presenter.prepareFailView("Rating must be between 1 and 5.");
                return;
            }
        }

        // Remove old watched movie entry
        user.getWatchHistory().removeMovie(oldWatchedMovie);

        // Create new watched movie with updated date
        LocalDateTime newWatchedDate = watchedDate != null ? watchedDate : oldWatchedMovie.getWatchedDate();
        WatchedMovie newWatchedMovie = new WatchedMovie(movie, newWatchedDate);
        user.getWatchHistory().addWatchedMovie(newWatchedMovie);

        // Update or create review if rating or review text is provided
        if (requestModel.getRating() != null || (requestModel.getReview() != null && !requestModel.getReview().trim().isEmpty())) {
            int rating = requestModel.getRating() != null ? requestModel.getRating() : 3; // Default to 3 if not provided
            String reviewText = requestModel.getReview() != null ? requestModel.getReview().trim() : "";

            // Check if review already exists
            Review existingReview = user.getReviewsByMovieId().get(requestModel.getMovieId());
            if (existingReview != null) {
                // Remove old review (reviews are immutable, so we need to create a new one)
                // Note: User.addReview replaces existing review, so we can just add a new one
            }

            // Create new review
            Review review = new Review(
                    UUID.randomUUID().toString(),
                    user,
                    movie,
                    rating,
                    reviewText,
                    LocalDateTime.now()
            );
            user.addReview(review);
        }

        // Save the updated user
        userDataAccessInterface.save(user);

        presenter.prepareSuccessView(
                new EditWatchedMovieResponseModel(
                        requestModel.getUserName(),
                        requestModel.getMovieId(),
                        movie.getTitle(),
                        newWatchedDate,
                        requestModel.getRating(),
                        requestModel.getReview()
                )
        );
    }
}

