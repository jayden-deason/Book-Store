/**
 * Review
 * <p>
 * A class to store product reviews. Not currently being used in the new GUI implementation.
 *
 * @author Katya Teodorovich, section 001
 * @version November 29, 2022
 */
public class Review implements java.io.Serializable {
    private String userEmail; // email of the reviewer
    private String review; // text of the review
    private int rating; // number of stars (out of 5)

    /**
     * Create a new review object with the given parameters
     *
     * @param userEmail the email of the author
     * @param review    the text of the review
     * @param rating    the rating (out of 5)
     */
    public Review(String userEmail, String review, int rating) {
        this.userEmail = userEmail;
        this.review = review;
        this.rating = rating;
    }

    /**
     * Creates a new review from the review.toString() format
     *
     * @param reviewToString a string describing the review (email:rating:review text)
     */
    public Review(String reviewToString) {
        String[] info = reviewToString.split(":");
        this.userEmail = info[0];
        this.rating = Integer.parseInt(info[1]);
        this.review = info[2];
    }

    /**
     * Get the author email
     *
     * @return user email
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Change the user's email
     *
     * @param userEmail new user email
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * Get the text of the review
     *
     * @return review contents
     */
    public String getReview() {
        return review;
    }

    /**
     * Change the text of the review
     *
     * @param review new review contents
     */
    public void setReview(String review) {
        this.review = review;
    }

    /**
     * Get the rating
     *
     * @return rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * Change the rating
     *
     * @param rating new rating
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Return a string representation of the review in the format:
     * user email:rating:review text
     * Used to save to a csv
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return userEmail + ":" + rating + ":" + review;
    }

    /**
     * Nicely prints a formatted review string
     */
    public void printString() {
        System.out.print(userEmail + "\t");
        for (int i = 0; i < rating; i++) {
            System.out.print("*");
        }

        System.out.println();
        System.out.println(review);

    }


}
