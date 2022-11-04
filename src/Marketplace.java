package src;

import java.util.ArrayList;

/**
 * src.Marketplace
 *
 * An object representing a marketplace consisting of buyers and sellers
 *
 * @author
 * @version
 */
public class Marketplace {
    private ArrayList<Buyer> buyers; // the list of buyers in the marketplace
    private ArrayList<Seller> sellers; // the list of sellers in the marketplace

    // TODO: decide how to organize constructors
    // reading from a file?

    /**
     * Get list of all available products in the marketplace
     * @return list of products
     */
    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> out = new ArrayList<>();
        for (Seller s : sellers) {
            out.addAll(s.getProducts());
        }

        return out;

        // this is probably not very optimal, as we make a new arraylist each time
        // find better implementation?
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> out = new ArrayList<>();
        out.addAll(sellers);
        out.addAll(buyers);

        return out;

        // again, could be optimized
    }
}
