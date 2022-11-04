package src;

import java.util.ArrayList;

/**
 * src.Seller
 *
 * An object representing a seller user in the marketplace
 *
 * @author
 * @version
 */
public class Seller extends User {
    private ArrayList<Product> products; // the seller's products

    /**
     * Create  a new seller with a given username, password, and list of products
     * @param username the seller's username
     * @param password the seller's password
     * @param products the seller's product list
     */
    public Seller(String username, String password, ArrayList<Product> products) {
        super(username, password);
        this.products = products;
    }

    /**
     * Create a new seller using data from a file
     * @param filename path to file describing seller
     */
    public Seller(String filename) {
        // TODO: implement based on file format
    }

    /**
     * Get the seller's available product
     * @return the list of products
     */
    public ArrayList<Product> getProducts() {
        return products;
    }

    /**
     * Set the seller's product list
     * @param products an updated list of products
     */
    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    /**
     * Prints the seller's dashboard
     */
    @Override
    public void printDashboard() {
        // TODO: determine appearance of seller dashboard
    }
}
