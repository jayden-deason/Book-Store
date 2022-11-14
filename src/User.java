/**
 * User
 *
 * A parent class for users in the marketplace
 *
 * @author Katya Teodorovich, section 001
 * @version November 13, 2022
 */
public class User {
    private String email; // the user's email
    private String password; // the user's password

    /**
     * Creates a new User object with a blank email and password
     */
    public User() {
        this.email = "";
        this.password = "";
    }

    /**
     * Creates a new User object with a specified email and password
     * @param email the user's email
     * @param password the user's password
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Get the user's email
     * @return email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Get the user's password
     * @return password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Set the user's email
     * @param email new email for the user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Set the user's password
     * @param password new password for the user
     */
    public void setPassword(String password) {
        this.password = password;
    }

}