package use_case.review_movie;

import entity.User;

public interface ReviewMovieUserDataAccessInterface {
    User getUser(String username);

    void save(User user);
}
