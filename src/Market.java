import java.io.*;
import java.util.ArrayList;

/**
 * Market
 * <p>
 * An object representing a marketplace consisting of buyers and sellers
 *
 * @author Katya Teodorovich, section 001
 * @version November 14, 2022
 */
public class Market implements java.io.Serializable{
    private ArrayList<Buyer> buyers; // the list of buyers in the marketplace
    private ArrayList<Seller> sellers; // the list of sellers
    private ArrayList<Store> stores; // the list of stores in the marketplace
    private ArrayList<Product> products; // the master list of products in the marketplace

    private final String buyersFile; // the filename to write buyer info to
    private final String sellersFile; // the filename to write seller info to
    private final String storesFile; // the filename for store info
    private final String productsFile; // the filename for product info

    private static Market INSTANCE; // singleton pattern

    /**
     * Get the only existing instance of the market
     *
     * @return market instance
     */
    public static Market getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Market("Customers.csv", "Sellers.csv", "Stores.csv", "Products.csv");
        }

        return INSTANCE;
    }

    /**
     * Create a new Market object by reading from 4 files
     *
     * @param buyersFile   file with buyer info
     * @param sellersFile  file with seller info
     * @param storesFile   file with store info
     * @param productsFile file with product info
     */
    private Market(String buyersFile, String sellersFile, String storesFile, String productsFile) {
        this.buyersFile = buyersFile;
        this.sellersFile = sellersFile;
        this.storesFile = storesFile;
        this.productsFile = productsFile;

        this.sellers = new ArrayList<>();
        this.stores = new ArrayList<>();
        this.products = new ArrayList<>();
        this.buyers = new ArrayList<>();

        // sellers
        ArrayList<String> lines = readFile(sellersFile);
        for (String line : lines) {
            addSeller(new Seller(line));
        }

        // stores
        lines = readFile(storesFile);
        for (String line : lines) {
            Store s = new Store(line);
            addStore(s);
        }

        // products
        addProductsFromFile(productsFile);

        // buyers
        lines = readFile(buyersFile);
        for (String line : lines) {
            addBuyer(new Buyer(line));
        }
    }

    /**
     * Add all products from a given file for a specific seller
     *
     * @param fileName path to product file
     * @param seller   seller importing the books
     */
    public void addSellerProductsFromFile(String fileName, Seller seller) {
        ArrayList<String> lines = readFile(fileName);
        for (String line : lines) {
            Product p = new Product(line);
            p.setStoreName(line.split(",")[2]);

            if (seller.getStoreNames().contains(p.getStoreName())) {
                addProduct(p);
            } else {
                System.out.printf("Skipping '%s,' store does not belong to %s!\n", p.getName(), seller.getEmail());
            }

        }
    }

    /**
     * Add all products from a given file
     *
     * @param fileName path to product file
     */
    public void addProductsFromFile(String fileName) {
        ArrayList<String> lines = readFile(fileName);
        for (String line : lines) {
            Product p = new Product(line);
            p.setStoreName(line.split(",")[2]);
            addProduct(p);
        }
    }


    private int getNextIndex() {
        int maxIdx = -1;
        for (Product p : products) {
            if (p.getIndex() > maxIdx) {
                maxIdx = p.getIndex();
            }
        }

        return maxIdx + 1;
    }

    /**
     * Add a seller to the market
     *
     * @param s seller
     */
    public void addSeller(Seller s) {
        if (s.getIndex() == -1) {
            s.setIndex(sellers.size());
        }
        sellers.add(s);
    }

    /**
     * Add a buyer to the market
     *
     * @param b buyer
     */
    public void addBuyer(Buyer b) {
        if (b.getIndex() == -1) {
            b.setIndex(buyers.size());
        }
        buyers.add(b);
    }

    /**
     * Add a product to the market and to its respective store
     *
     * @param p product
     */
    public void addProduct(Product p) {
        if (p.getIndex() == -1) {
            p.setIndex(getNextIndex());
        }
        Store s = getStoreByName(p.getStoreName());
        if (s == null) {
            throw new RuntimeException(String.format("No store with name '%s'", p.getStoreName()));
        } else {
            s.addProduct(p, this);
            products.add(p);
        }
    }

    /**
     * Add a store to the market and its respective seller
     *
     * @param s store
     */
    public void addStore(Store s) {
        if (s.getIndex() == -1) {
            s.setIndex(stores.size());
        }
        if (getStoreByName(s.getName()) != null) throw new RuntimeException("store already exists!");
        Seller seller = getSellerByEmail(s.getSellerName());
        seller.addStore(s);
        stores.add(s);
    }

    /**
     * Create a new, empty store with a name & seller. Add it to the market and to the respective seller
     *
     * @param storeName  new store name
     * @param sellerName new store seller
     */
    public void addStore(String storeName, String sellerName) {
        int idx = stores.size();
        Store s = new Store(idx, storeName, sellerName, 0, 0, "", "");
        addStore(s);
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
        for (Product p : products) {
            if (p.getIndex() == idx) {
                return p;
            }
        }
        return null;
    }

    /**
     * Get all emails for existing accounts
     *
     * @return list of emails
     */
    public ArrayList<String> getAllEmails() {
        ArrayList<User> users = getAllUsers();
        ArrayList<String> out = new ArrayList<>();

        for (User user : users) {
            out.add(user.getEmail());
        }

        return out;
    }

    /**
     * Get a product object from the masterlist given its name
     *
     * @param name the name of the product
     * @return a product object, or null if not found
     */
    public Product getProductByName(String name) {
        for (Product p : products) {
            if (p.getName().equals(name)) {
                return p;
            }
        }

        return null;
    }

    /**
     * Get a user that matches the given email
     *
     * @param email target email
     * @return user with that email, null if none found
     */
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
//            p.setQuantity(p.getQuantity() - quantities.get(i));
            s.makePurchase(buyer, quantity, p);
        }


        buyer.makePurchase();
        updateAllFiles();
    }

    /**
     * Get a list of all store names in the marketplace
     *
     * @return store names
     */
    public ArrayList<String> getStoreNames() {
        ArrayList<String> out = new ArrayList<>();

        for (Store s : stores) {
            out.add(s.getName());
        }

        return out;
    }

    /**
     * Remove a product from the market
     *
     * @param p product to remove
     */
    public void removeProduct(Product p) {
        Store s = getStoreByName(p.getStoreName());
        s.removeProduct(p);
        products.remove(p);

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
    public void printToFile(ArrayList list, String filename) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(filename, false));

            if (list.size() == 0) {
                pw.println("Empty list!");
            }
            for (Object o : list) {
                pw.println(o.toString());
            }
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the total number of sales in purchase history for a product
     *
     * @param p product
     * @return quantity of that product sold
     */
    public int getSalesForProduct(Product p) {
        int idx = products.indexOf(p);
        return getSalesForProduct(idx);

    }

    public int getCustomersForProduct(Product p) {
        int customers = 0;

        for (Buyer buyer : buyers) {
            for (String purchase : buyer.getPurchaseHistory()) {
                if (Integer.parseInt(purchase.split(":")[0]) == p.getIndex()) {
                    customers += 1;
                    break; // break inner loop if customer purchased at least 1 item
                }
            }
        }

        return customers;
    }

    /**
     * Get the number of sales for a product by its index
     *
     * @param index index of a product
     * @return quantity of that product sold
     */
    public int getSalesForProduct(int index) {
        int sales = 0;

        for (Buyer buyer : buyers) {
            for (String purchase : buyer.getPurchaseHistory()) {
                if (Integer.parseInt(purchase.split(":")[0]) == index) {
                    sales += Integer.parseInt(purchase.split(":")[1]);
                }
            }
        }

        return sales;

    }

    /**
     * Sort the list of products by price
     *
     * @return the list of products, sorted
     */
    public ArrayList<Product> sortByPrice() {
        ArrayList<Product> temp = new ArrayList<>(products);
        temp.sort((s1, s2) -> {
            return (int) (100 * (s1.getSalePrice() - s2.getSalePrice()));
            // multiply by 100 so integer cast doesn't truncate down to 0 if < 1
        });

        return temp;
    }

    public ArrayList<Product> sortBySales() {
        ArrayList<Product> temp = new ArrayList<>(products);

        temp.sort((s1, s2) -> {
            return getSalesForProduct(s1) - getSalesForProduct(s2);
        });

        return temp;
    }

    /**
     * Sort the list of products by quantity
     *
     * @return the list of products, sorted
     */
    public ArrayList<Product> sortByQuantity() {
        ArrayList<Product> temp = new ArrayList<>(products);

        temp.sort((s1, s2) -> s1.getQuantity() - s2.getQuantity());

        return temp;
    }

    public ArrayList<Store> sortStoresByProductsSold() {
        ArrayList<Store> out = new ArrayList<>(stores);
        out.sort((s1, s2) -> {
            int sum1 = 0;
            for (Product p : s1.getProducts()) {
                sum1 += getSalesForProduct(p);
            }

            int sum2 = 0;
            for (Product p : s2.getProducts()) {
                sum2 += getSalesForProduct(p);
            }

            return sum1 - sum2;
        });

        return out;
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
        if (name != null) name = name.toLowerCase();
        if (storeName != null) storeName = storeName.toLowerCase();
        if (description != null) description = description.toLowerCase();

        for (Product p : products) {
            if (name == null || p.getName().toLowerCase().contains(name)) {
                if (storeName == null || p.getStoreName().toLowerCase().contains(storeName)) {
                    // decided to do .contains() for description instead of equals...
                    // doesn't really make sense to have to type the entire description?
                    if (description == null || p.getDescription().toLowerCase().contains(description)) {
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


    /**
     * Get the list of buyers in the market
     *
     * @return list of buyers
     */
    public ArrayList<Buyer> getBuyers() {
        return buyers;
    }

    /**
     * Get the list of sellers in the market
     *
     * @return list of sellers
     */
    public ArrayList<Seller> getSellers() {
        return sellers;
    }

    /**
     * Get the list of stores in the market
     *
     * @return list of stores
     */
    public ArrayList<Store> getStores() {
        return stores;
    }

    /**
     * Get the list of products in the market
     *
     * @return list of products
     */
    public ArrayList<Product> getProducts() {
        return products;
    }
}