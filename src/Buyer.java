import java.util.ArrayList;

/**
 * Buyer
 *
 * An object representing a buyer user in the marketplace
 *
 * @author
 * @version
 */
public class Buyer extends User {
    private ArrayList<Product> shoppingCart; // the user's shopping cart of products

    /**
     * Creates a new src.Buyer with a given username & password, and an empty shopping cart
     * @param username the buyer's username
     * @param password the buyer's password
     */
    public Buyer(String username, String password) {
        super(username, password);
        this.shoppingCart = new ArrayList<>();
    }

    /**
     * Creates a new src.Buyer with a given username, password, and shopping cart
     * @param username the buyer's username
     * @param password the buyer's password
     * @param shoppingCart the buyer's shopping cart
     */
    public Buyer(String username, String password, ArrayList<Product> shoppingCart) {
        super(username, password);
        this.shoppingCart = shoppingCart;
    }

    /**
     * Creates a new src.Buyer from a text file
     * @param filename path to the file describing the buyer
     */
    public Buyer(String filename) {
        //TODO: implement
    }

    /**
     * Get the buyer's shopping cart contents
     * @return the list of products in the buyer's shopping cart
     */
    public ArrayList<Product> getShoppingCart() {
        return shoppingCart;
    }

    /**
     * Set the buyer's shopping cart
     * @param shoppingCart
     */
    public void setShoppingCart(ArrayList<Product> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    /**
     * Add a product to the shopping cart
     * @param product the product to be added
     */
    public void addProductToCart(Product product) {
        shoppingCart.add(product);
    }

    /**
     * Export the buyer info to a file
     * @param filename path to export file
     */
    @Override
    public void exportToFile(String filename) {
        // TODO: implement this, however we are saving the data to a file
    }

    /**
     * Prints the buyer's dashboard
     */
    @Override
    public void printDashboard() {
        // TODO: determine a format for a buyer's dashboard
    }
}
