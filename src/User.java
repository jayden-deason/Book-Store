/*

 */
public class User {
    private String username; // the user's username
    private String password; // the user's password

    /**
     * Creates a new src.User object with a blank username and password
     */
    public User() {
        this.username = "";
        this.password = "";
    }

    /**
     * Creates a new src.User object with a specified username and password
     * @param username the user's username
     * @param password the user's password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Get the user's username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the user's password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the user's username
     * @param username new username for the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Set the user's password
     * @param password new password for the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Export the user info to a file
     * @param filename path to export file
     */
    public void exportToFile(String filename) {
        // TODO: determine file formatting
    }

    /**
     * Prints a dashboard for the user
     *
     * Each specific type of user (buyer/seller) should implement their own version
     */
    public void printDashboard() {
        // TODO: determine dashboard appearance
    }
}
