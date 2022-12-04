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
    private int rating; // number of stars (out of 5)

    //todo: javadocs
    public Review(String userEmail, String review, int rating) {
        this.userEmail = userEmail;
        this.review = review;
        this.rating = rating;
    }

    public Review(String reviewToString) {
        String[] info = reviewToString.split(":");
        this.userEmail = info[0];
        this.rating = Integer.parseInt(info[1]);
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return userEmail + ":" + rating + ":" + review;
    }

    public void printString() {
        System.out.print(userEmail + "\t");
        for (int i = 0; i < rating; i++) {
            System.out.print("*");
        }

        System.out.println();
        System.out.println(review);

    }


}