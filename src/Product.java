import java.util.ArrayList;
import java.util.Objects;

/**
 * Product
 * <p>
 * A class describing an available product in the marketplace
 *
 * @author Katya Teodorovich, section 001
 * @version November 4, 2022
 */
public class Product {
    private String name; // the name of the product
    private String storeName; // the store associated with the product
    private String description; // a text description of the product
    private int quantity; // the quantity available of the product
    private double salePrice; // the price of the product
    private double originalPrice; // the original price of the product
    private int index; // the index of the product in the csv
    private ArrayList<Review> reviews; // the list of product reviews

    /**
     * Creates a new product object with the specified parameters
     *
     * @param name          the name of the product
     * @param storeName     the store associated with the product
     * @param description   the text description of the product
     * @param quantity      the quantity available of the product
     * @param salePrice     the price of the product
     * @param originalPrice the price of the product
     * @param index         the index of the product in the csv
     */
    public Product(String name, String storeName, String description, int quantity, double salePrice, double originalPrice, int index) {
        this.name = name;
        this.storeName = storeName;
        this.description = description;
        this.quantity = quantity;
        this.salePrice = salePrice;
        this.originalPrice = originalPrice;
        this.index = index;
        this.reviews = new ArrayList<>();
    }

    /**
     * Create a product from the description in a line of text
     *
     * @param line a line describing the file in the given toString format
     */
    public Product(String line) {
        String[] info = line.split(",");
        this.index = Integer.parseInt(info[0]);
        this.name = info[1];
        this.storeName = info[2];
        this.description = info[3];
        this.quantity = Integer.parseInt(info[4]);
        this.salePrice = Double.parseDouble(info[5]);
        this.originalPrice = Double.parseDouble(info[6]);
        this.reviews = new ArrayList<>();

        String[] revs = info[7].substring(1, info[7].length() - 1).split("/");
        for (String review : revs) {
            if (!review.equals("")) {
                addReview(new Review(review));
            }
        }
    }

    /**
     * Get the name of the product
     *
     * @return name
     */
    public String getName() {
        return name;
    }


    /**
     * Get the store associated with this product
     *
     * @return store
     */
    public String getStoreName() {
        return storeName;
    }

    /**
     * Get the description of this product
     *
     * @return a text description of the product
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the quantity available of the product
     *
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Get the price of the product
     *
     * @return
     */
    public double getSalePrice() {
        return salePrice;
    }

    /**
     * Set the name of the product
     *
     * @param name a new name for the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the name of store of the product
     *
     * @param storeName a new store for the product
     */
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    /**
     * Set the description of the product
     *
     * @param description a new product description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the available quantity of the product
     *
     * @param quantity the new quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Set the product price
     *
     * @param salePrice the new price
     */
    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    /**
     * Get the product's index in the csv
     *
     * @return index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Set the product's index in the csv
     *
     * @param index new index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, storeName, salePrice, quantity, description, storeName);
    }

    /**
     * Get the original price of the product
     *
     * @return original price
     */
    public double getOriginalPrice() {
        return originalPrice;
    }

    /**
     * Set the original price of the product
     *
     * @param originalPrice original price
     */
    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    /**
     * Return whether the product is on sale or not
     *
     * @return if the product is on sale
     */
    public boolean isOnSale() {
        return originalPrice != salePrice;
    }

    /**
     * Get a string description of the product, can be used to save to a file
     *
     * @return string description of the product
     */

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Product)) {
            return false;
        } else {
            Product obj = (Product) object;
            return ((obj.getIndex() == this.getIndex())
                    && obj.getName().equals(this.getName())
                    && obj.getQuantity() == this.getQuantity()
                    && obj.getDescription().equals(this.getDescription())
                    && obj.getSalePrice() == this.getSalePrice()
                    && obj.getStoreName().equals(this.getStoreName()));
        }
    }

    /**
     * Add a review to the product's reviews
     *
     * @param r new review
     */
    public void addReview(Review r) {
        reviews.add(r);
    }

    /**
     * Return the list of reviews
     * @return list of product reviews
     */
    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public String getReviewString() {
        String out = "<";
        for (int i = 0; i < reviews.size(); i++) {
            out += reviews.get(i).toString();
            if (i < reviews.size() - 1) {
                out += "/";
            }
        }

        return out + ">";
    }

    /**
     * Return a string representation of the product, to be used in csvs
     *
     * @return the string for the product
     */
    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%d,%.2f, %.2f,%s",
                getIndex(), getName(), getStoreName(), getDescription(), getQuantity(), getSalePrice(),
                getOriginalPrice(), getReviewString());
    }

    /**
     * Get the average customer rating for the product
     * @return average rating, -1 if no reviews
     */
    public double getAverageRating() {
        if (reviews.size() == 0) return -1;

        double out = 0;
        for (Review r : reviews) {
            out += r.getRating();
        }

        out /= reviews.size();

        return out;

    }

}