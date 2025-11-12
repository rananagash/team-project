package data_access;

import entity.User;
import use_case.common.UserGateway;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserDataAccessObject implements UserGateway {

    private final Map<String, User> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<User> findByUserName(String userName) {
        return Optional.ofNullable(storage.get(userName));
    }

    @Override
    public void save(User user) {
        storage.put(user.getUserName(), user);
    }
}

