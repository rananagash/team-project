package use_case.common;

import entity.User;

import java.util.Optional;

public interface UserGateway {

    Optional<User> findByUserName(String userName);

    void save(User user);
}

