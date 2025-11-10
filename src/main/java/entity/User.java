package entity;

public class User {

    private String userName;
    private String password;
    private List<WatchList> watchLists;
    private WatchHistory watchHistory;
    private Map<Movie, Review> reviews;

    /**
     * Creates a new user with the given non-empty username and non-empty password.
     * @param userName
     * @param password
     * @throws IllegalArgumentException if the password or username are empty
     */
    public User(String userName, String password) {
        if ("".equals(userName)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() { return userName; }

    public String getPassword() { return password; }
}
