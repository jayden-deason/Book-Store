import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Seller
 * <p>
 * An object representing a seller user in the marketplace
 *
 * @author Visv Shah
 * @version 11/9/22
 */
public class Seller extends User {
    private ArrayList<Store> stores; // the seller's stores

    /**
     * Create  a new seller with a given username, password, and list of products
     *
     * @param username the seller's username
     * @param password the seller's password
     * @param stores   the seller's store list
     */
    public Seller(String username, String password, ArrayList<Store> stores) {
        super(username, password);
        this.stores = stores;
    }

    public Seller(String username, String password) {
        super(username, password);
        this.stores = new ArrayList<Store>();
    }

    /**
     * Writes over stores.csv to add a new store to it
     *
     * @return the list of stores
     */
    public boolean writeStoresFile(String fileName, Store s) {
        ArrayList<String> lines = new ArrayList<String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            while (line != null) {
                lines.add(line);
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileName);
            for (String str : lines) {
                fw.write(str + "\n");
            }
            int lastIndex = Integer.parseInt(lines.get(lines.size() - 1).split(",")[0]);
            String line = lastIndex + "," + s.getName() + "," + super.getUsername() + ",<";
            for (Product product : s.getProducts().keySet()) {
                line += product.getIndex() + ":" + product.getQuantity() + "/";
            }
            line = line.substring(0, line.length() - 1) + ">";
            fw.write(line + "\n");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
    }

    /**
     * Get the seller's available stores
     *
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
     * Creates a store and adds it to the stores ArrayList and to the stores.csv file
     */
    public void addStore(String storeName, String sellerName, String productFile, int sales, int revenue) {
        Store s = new Store(storeName, sellerName, productFile, sales, revenue);
        stores.add(s);
        writeStoresFile("stores.csv", s);
    }

    /**
     * Prints the seller's dashboard
     */
    public void printDashboard() {
        for (Store s : stores) {
            System.out.println("Store: " + s.getName());
            s.displayStatistics();
        }
    }

    /**
     * Prints the seller's dashboard with filters
     */
    public void printSortedDashBoard() {
        for (Store s : stores) {
            System.out.println("Store: " + s.getName());
            s.displayStatistics();
        }
    }

    public String toString() {
        String storesStr = "<";
        for (Store s : this.stores) {
            storesStr += s.getIndex() + "/";
        }
        storesStr = storesStr.substring(storesStr.length() - 1) + ">";
        return String.format("%s,%s,%s", this.getUsername(), this.getPassword(), storesStr);

    }
}
