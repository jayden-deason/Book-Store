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
    private double price; // the price of the product
    private int index; // the index of the product in the csv

    /**
     * Creates a new product object with the specified parameters
     *
     * @param name        the name of the product
     * @param storeName   the store associated with the product
     * @param description the text description of the product
     * @param quantity    the quantity available of the product
     * @param price       the price of the product
     * @param index       the index of the product in the csv
     */
    public Product(String name, String storeName, String description, int quantity, double price, int index) {
        this.name = name;
        this.storeName = storeName;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.index = index;
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
        this.price = Double.parseDouble(info[5]);
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
    public double getPrice() {
        return price;
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
     * @param price the new price
     */
    public void setPrice(double price) {
        this.price = price;
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
        return Objects.hash(name, storeName, price, quantity, description, storeName);
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
                    && obj.getPrice() == this.getPrice()
                    && obj.getStoreName().equals(this.getStoreName()));
        }
    }
    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%d,%.2f",
                getIndex(), getName(), getStoreName(), getDescription(), getQuantity(), getPrice());
    }

}
