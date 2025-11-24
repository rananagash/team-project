package use_case.common;

public class MovieDataAccessException extends Exception {

    public enum Type {
        NETWORK,    // my side and server side network issues?
        TMDB_ERROR, // TMDb server issues
        UNKNOWN     // something else
    }

    private final Type type;

    public MovieDataAccessException(Type type, String message) {
        super(message);
        this.type = type;
    }

    public MovieDataAccessException(Type type, String message, Throwable cause) {
        super(message, cause);
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}