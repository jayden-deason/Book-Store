/**
 * Product
 *
 * A class describing an available product in the marketplace
 *
 * @author Katya Teodorovich, section 001
 * @version November 4, 2022
 */
public class Product {
    private String name; // the name of the product
    private String sellerName; // the seller associated with the product
    private String description; // a text description of the product
    private int quantity; // the quantity available of the product
    private double price; // the price of the product
    private int index;

    /**
     * Creates a new product object with the specified parameters
     * @param name the name of the product
     * @param sellerName the seller associated with the product
     * @param description the text description of the product
     * @param quantity the quantity available of the product
     * @param price the price of the product
     * @param index the index of the product in the index.csv file
     */
    public Product(String name, String sellerName, String description, int quantity, double price, int index) {
        this.name = name;
        this.sellerName = sellerName;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.index = index;
    }

    /**
     * Get the name of the product
     * @return name
     */
    public String getName() {
        return name;
    }


    /**
     * Get the seller associated with this product
     * @return seller
     */
    public String getSellerName() {
        return sellerName;
    }

    /**
     * Get the description of this product
     * @return a text description of the product
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the quantity available of the product
     * @return quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Get the price of the product
     * @return
     */
    public double getPrice() {
        return price;
    }
    /**
     * Get the index of the product
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * Set the name of the product
     * @param name a new name for the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the name of seller of the product
     * @param sellerName a new seller for the product
     */
    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    /**
     * Set the description of the product
     * @param description a new product description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the available quantity of the product
     * @param quantity the new quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Set the product price
     * @param price the new price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Set the index
     * @param index the new index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Get a string description of the product, can be used to save to a file
     * @return string description of the product
     */

    @Override
    public String toString() {
        // TODO: implement based on decided format
        return "";
    }
}
