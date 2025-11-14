package entity.factories;

import entity.User;
import entity.WatchList;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Factory for creating Watch Lists
 */
public class WatchListFactory {

    // private constructor to prevent instantiation
    private WatchListFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    // create initial default watch list
    public static WatchList createDefaultWatchList(User user) {
        return new WatchList(
                UUID.randomUUID().toString(),
                user,
                user.getUserName() + "'s Watch List",
                LocalDateTime.now()
        );
    }

    public static WatchList createWatchList(User user, String name) {
        return new WatchList(
                UUID.randomUUID().toString(),
                user,
                name,
                LocalDateTime.now()
        );
    }
}
