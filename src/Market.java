import java.io.*;
import java.util.ArrayList;

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
            }

            br.close();

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
    public Product getProductByIndex(int idx) {
        if (products.get(idx).getIndex() == idx) return products.get(idx);

        for (Product p : products) {
            if (p.getIndex() == idx) {
                return p;
            }
        }
        return null;
    }

    public User getUserByEmail(String email) {
        for (User user : getAllUsers()) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }

        return null;
    }


    /**
     * Make a purchase by calling the store's makePurchase method for every product in a shopping cart.
     * Also empties buyer's shopping cart via the buyer's makePurchase method.
     * Throws a runtime exception if an object is out of stock.
     *
     * @param buyer the buyer making the purchase
     */
    public void makePurchase(Buyer buyer) {
        ArrayList<Product> products = new ArrayList<>();
        ArrayList<Integer> quantities = new ArrayList<>();

        for (String line : buyer.getShoppingCart()) {
            int productIndex = Integer.parseInt(line.split(":")[0]);

            Product p = getProductByIndex(productIndex);

            if (p.getQuantity() <= 0) {
                throw new RuntimeException(String.format("\"%s\"out of stock!", p.getName()));
            }

            products.add(p);
            quantities.add(Integer.parseInt(line.split(":")[1]));
        }

        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            int quantity = quantities.get(i);

            Store s = getStoreByName(p.getStoreName());
            s.makePurchase(buyer, quantity, p);
        }


        buyer.makePurchase();
        updateAllFiles();
    }

    public ArrayList<String> getStoreNames() {
        ArrayList<String> out = new ArrayList<>();

        for (Store s : stores) {
            out.add(s.getName());
        }

        return out;
    }


    /**
     * Find a seller by their email
     *
     * @param email seller's email
     * @return seller with matching email, null if none found
     */
    public Seller getSellerByEmail(String email) {
        for (Seller s : sellers) {
            if (s.getEmail().equals(email)) { // TODO: email vs. username confusion
                return s;
            }
        }
        return null;
    }

    /**
     * Find a buyer by their email
     *
     * @param email buyer's email
     * @return buyer with matching email, null if none found
     */
    public Buyer getBuyerByEmail(String email) {
        for (Buyer b : buyers) {
            if (b.getEmail().equals(email)) {
                return b;
            }
        }
        return null;
    }

    /**
     * Find a store with a given name
     *
     * @param name name of store
     * @return store with that name, null if none found
     */
    public Store getStoreByName(String name) {
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

    public ArrayList<Product> sortByPrice() {
        products.sort((s1, s2) -> {
            return (int) (100 * (s1.getPrice() - s2.getPrice()));
            // multiply by 100 so integer cast doesn't truncate down to 0 if < 1
        });

        return products;
    }

    public ArrayList<Product> sortByQuantity() {
        products.sort((s1, s2) -> s1.getQuantity() - s2.getQuantity());

        return products;
    }

    /**
     * Returns a list of products that match the following conditions (provided they are not null)
     *
     * @param name        a name that the product has to match
     * @param storeName   a storename to match the product's storename
     * @param description a description that the product's description must contain
     * @return a list of products matching name, storename, and containing the description
     */
    public ArrayList<Product> matchConditions(String name, String storeName, String description) {
        ArrayList<Product> out = new ArrayList<>();

        for (Product p : products) {
            if (name == null || p.getName().equalsIgnoreCase(name)) {
                if (storeName == null || p.getStoreName().equalsIgnoreCase(storeName)) {
                    // decided to do .contains() for description instead of equals...
                    // doesn't really make sense to have to type the entire description?
                    if (description != null && p.getDescription().contains(description)) {
                        out.add(p);
                    }
                }
            }
        }

        return out;
    }

    /**
     * Adds a new user (buyer or seller to the masterlist)
     *
     * @param user the user to be added
     */
    public void createUser(User user) {
        if (user instanceof Buyer) {
            Buyer b = (Buyer) user;
            b.setIndex(buyers.size());
            buyers.add(b);
        } else if (user instanceof Seller) {
            Seller s = (Seller) user;
            s.setIndex(sellers.size());
            sellers.add(s);
        }

        updateAllFiles();
    }

    public ArrayList<Buyer> getBuyers() {
        return buyers;
    }

    public ArrayList<Seller> getSellers() {
        return sellers;
    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
}
