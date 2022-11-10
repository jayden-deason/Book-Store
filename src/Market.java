import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Market
 * <p>
 * An object representing a marketplace consisting of buyers and sellers
 *
 * @author Katya Teodorovich, section 001
 * @version November 9, 2022
 */
public class Market {
    private ArrayList<Buyer> buyers; // the list of buyers in the marketplace
    private ArrayList<Seller> sellers; // the list of sellers
    private ArrayList<Store> stores; // the list of stores in the marketplace
    private ArrayList<Product> products; // the master list of products in the marketplace

    private final String buyersFile; // the filename to write buyer info to
    private final String sellersFile; // the filename to write seller info to
    private final String storesFile; // the filename for store info
    private final String productsFile; // the filename for product info

    /**
     * Create a new Market object by reading from 4 files
     *
     * @param buyersFile   file with buyer info
     * @param sellersFile  file with seller info
     * @param storesFile   file with store info
     * @param productsFile file with product info
     */
    public Market(String buyersFile, String sellersFile, String storesFile, String productsFile) {
        this.buyersFile = buyersFile;
        this.sellersFile = sellersFile;
        this.storesFile = storesFile;
        this.productsFile = productsFile;

        // sellers
        ArrayList<String> lines = readFile(sellersFile);
        this.sellers = new ArrayList<>();
        for (String line : lines) {
            sellers.add(new Seller(line));
        }

        // stores
        lines = readFile(storesFile);
        this.stores = new ArrayList<>();
        for (String line : lines) {
            Store s = new Store(line);
            stores.add(s);
            Seller seller = getSellerByEmail(line.split(",")[2]);
            seller.getStores().add(s);
        }

        // products
        lines = readFile(productsFile);
        this.products = new ArrayList<>();
        for (String line : lines) {
            Product p = new Product(line);
            Store s = getStoreByName(line.split(",")[2]);
            s.addProduct(p);
            p.setStoreName(s.getName());
            products.add(p);
        }

        // buyers
        lines = readFile(buyersFile);
        this.buyers = new ArrayList<>();
        for (String line : lines) {
            Buyer b = new Buyer(line);
            String cart = line.split(",")[4];
            String[] cartArr = cart.substring(1, cart.length() - 1).split(",");

            for (String s : cartArr) {
                Product p = getProductByIndex(Integer.parseInt(s));
                b.addProductToCart(p);
            }
            buyers.add(b);
        }
    }

    /**
     * Reads contents of file as an arraylist of lines
     *
     * @param filename path to file
     * @return lines of the file
     */
    private ArrayList<String> readFile(String filename) {
        ArrayList<String> lines = new ArrayList<String>();

        try {
            // reading file contents
            BufferedReader br = new BufferedReader(new FileReader(filename));

            String line = br.readLine();
            while (line != null) {
                lines.add(line);
                line = br.readLine();
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    /**
     * Get a product from the masterlist by its index. Typically just access that index of the list, but in case that
     * doesn't match -- loop through and find equivalent index
     *
     * @param idx index of product
     * @return product at that index, null if no matches
     */
    private Product getProductByIndex(int idx) {
        if (products.get(idx).getIndex() == idx) return products.get(idx);

        for (Product p : products) {
            if (p.getIndex() == idx) {
                return p;
            }
        }
        return null;
    }

    /**
     * Find a seller by their email
     *
     * @param email seller's email
     * @return seller with matching email, null if none found
     */
    private Seller getSellerByEmail(String email) {
        for (Seller s : sellers) {
//            if (s.getEmail().equals(email)) {
//                return s;
//            }
        }
        return null;
    }

    /**
     * Find a store with a given name
     *
     * @param name name of store
     * @return store with that name, null if none found
     */
    private Store getStoreByName(String name) {
        for (Store s : stores) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Get list of all available products in the marketplace
     *
     * @return list of products
     */
    public ArrayList<Product> getAllProducts(boolean nonzero) {
        if (!nonzero) return products;

        ArrayList<Product> out = new ArrayList<>();
        for (Product p : products) {
            if (p.getQuantity() > 0) {
                out.add(p);
            }
        }

        return out;
    }

    /**
     * Get a list of all users in the market
     *
     * @return combined list of buyers and sellers
     */
    public ArrayList<User> getAllUsers() {
        ArrayList<User> out = new ArrayList<>();
        out.addAll(sellers);
        out.addAll(buyers);

        return out;
    }

    /**
     * Update all 4 files describing the market by printing to files with the object's toString methods
     */
    public void updateAllFiles() {
        printToFile(buyers, buyersFile);
        printToFile(sellers, sellersFile);
        printToFile(stores, storesFile);
        printToFile(products, productsFile);
    }

    /**
     * Print contents of an arraylist to a file using toString
     *
     * @param list     a list of objects
     * @param filename the file to write to
     */
    private void printToFile(ArrayList list, String filename) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(filename, false));

            for (Object o : list) {
                pw.println(o.toString());
            }
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Product> sortByPrice() {
        products.sort((s1, s2) -> {
            return (int) (100 * (s1.getPrice() - s2.getPrice()));
            // multiply by 100 so integer cast doesn't truncate down to 0 if < 1
        });

        return products;
    }

    private ArrayList<Product> sortByQuantity() {
        products.sort((s1, s2) -> s1.getQuantity() - s2.getQuantity());

        return products;
    }
}
