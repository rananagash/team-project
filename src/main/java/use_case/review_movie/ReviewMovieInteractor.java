package use_case.review_movie;

import entity.Movie;
import entity.Review;
import entity.User;
import use_case.common.MovieGateway;
import use_case.common.UserGateway;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReviewMovieInteractor implements ReviewMovieInputBoundary {

    private final UserGateway userGateway;
    private final MovieGateway movieGateway;
    private final ReviewMovieOutputBoundary presenter;

    public ReviewMovieInteractor(UserGateway userGateway,
                                 MovieGateway movieGateway,
                                 ReviewMovieOutputBoundary presenter) {
        this.userGateway = userGateway;
        this.movieGateway = movieGateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(ReviewMovieRequestModel requestModel) {
        if (requestModel.getRating() < 1 || requestModel.getRating() > 5) {
            presenter.prepareFailView("Rating must be between 1 and 5.");
            return;
        }

        User user = userGateway.findByUserName(requestModel.getUserName()).orElse(null);
        if (user == null) {
            presenter.prepareFailView("User not found: " + requestModel.getUserName());
            return;
        }

        /*
         * TODO(Oliver Bisbee): Decide whether to update existing reviews and whether to sync with remote services or friend feeds.
         */
        Movie movie = movieGateway.findById(requestModel.getMovieId()).orElse(null);
        if (movie == null) {
            presenter.prepareFailView("Movie not found: " + requestModel.getMovieId());
            return;
        }

        Review review = new Review(UUID.randomUUID().toString(), user, movie,
                requestModel.getRating(), requestModel.getComment(), LocalDateTime.now());
        user.addReview(review);
        userGateway.save(user);
        presenter.prepareSuccessView(new ReviewMovieResponseModel(
                review.getReviewId(),
                user.getUserName(),
                movie.getMovieId(),
                review.getRating(),
                review.getComment()));
    }
}

