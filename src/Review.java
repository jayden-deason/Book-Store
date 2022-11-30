/**
 * Review
 *
 * A class to store product reviews
 *
 * @author Katya Teodorovich, section 001
 * @version November 29, 2022
 */
public class Review {
    private String userEmail; // email of the reviewer
    private String review; // text of the review
    private int stars; // number of stars (out of 5)

    //todo: javadocs
    public Review(String userEmail, String review, int stars) {
        this.userEmail = userEmail;
        this.review = review;
        this.stars = stars;
    }

    public Review(String reviewToString) {
        String[] info = reviewToString.split(":");
        this.userEmail = info[0];
        this.stars = Integer.parseInt(info[1]);
        this.review = info[2];
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    @Override
    public String toString() {
        return userEmail + ":" + stars + ":" + review;
    }


}
