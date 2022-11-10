import java.util.ArrayList;

/**
 * Seller
 *
 * An object representing a seller user in the marketplace
 *
 * @author
 * @version
 */
public class Seller extends User {
    private ArrayList<Store> stores; // the seller's stores

    /**
     * Create  a new seller with a given username, password, and list of products
     * @param username the seller's username
     * @param password the seller's password
     * @param stores the seller's store list
     */
    public Seller(String username, String password, ArrayList<Store> stores) {
        super(username, password);
        this.stores = stores;
    }

    /**
     * Create a new seller using data from a file
     * @param filename path to file describing seller
     */
    public Seller(String filename) {
        // TODO: implement based on file format
    }

    /**
     * Get the seller's stores
     * @return the list of stores
     */
    public ArrayList<Store> getStores() {
        return stores;
    }

    /**
     * Set the seller's store list
     * @param stores an updated list of stores
     */
    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }

    /**
     * Prints the seller's dashboard
     */
    @Override
    public void printDashboard() {
        // TODO: determine appearance of seller dashboard
    }
}
