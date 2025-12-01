package interface_adapter.add_to_watchlist;

public class WatchListOption {

    private final String watchListId;
    private final String name;

    public WatchListOption(String watchListId, String name) {
        this.watchListId = watchListId;
        this.name = name;
    }

    public String getWatchListId() {
        return watchListId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
