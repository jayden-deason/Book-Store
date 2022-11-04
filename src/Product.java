package src;

/**
 * src.Product
 *
 * A class describing an available product in the marketplace
 *
 * @author Katya Teodorovich, section 001
 * @version November 4, 2022
 */
public class Product {
    private String name; // the name of the product
    private Seller seller; // the seller associated with the product
    private String description; // a text description of the product
    private int quantity; // the quantity available of the product
    private double price; // the price of the product

    /**
     * Creates a new product object with the specified parameters
     * @param name the name of the product
     * @param seller the seller associated with the product
     * @param description the text description of the product
     * @param quantity the quantity available of the product
     * @param price the price of the product
     */
    public Product(String name, Seller seller, String description, int quantity, double price) {
        this.name = name;
        this.seller = seller;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
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
    public Seller getSeller() {
        return seller;
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
     * Set the name of the product
     * @param name a new name for the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the seller of the product
     * @param seller a new seller for the product
     */
    public void setSeller(Seller seller) {
        this.seller = seller;
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
     * Get a string description of the product, can be used to save to a file
     * @return string description of the product
     */
    @Override
    public String toString() {
        // TODO: implement based on decided format
        return "";
    }
}