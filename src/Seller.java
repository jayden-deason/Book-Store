import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
/**
 * Seller
 *
 * An object representing a seller user in the marketplace
 *
 * @author Visv Shah
 * @version 11/9/22
 */
public class Seller extends User {
    private ArrayList<Store> stores; // the seller's products

    /**
     * Create  a new seller with a given username, password, and list of products
     * @param username the seller's username
     * @param password the seller's password
     */
    public Seller(String username, String password) throws badNamingException{
        super(username, password);
        if(username.contains(",")) {
            throw new badNamingException("Please do not have a comma in your username!");
        }
        if(password.contains(",")) {
            throw new badNamingException("Please do not have a comma in your password!");
        }
        this.stores = new ArrayList<Store>();
    }
    /**
     * Writes over stores.csv to add a new store to it
     * @return the index of the store
     */
    public int writeStoresFile(String fileName, Store s) {
        ArrayList<String> lines = new ArrayList<String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            while(line != null) {
                lines.add(line);
                line = br.readLine();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            if(br != null) {
                try {
                    br.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(fileName);
            for(String str : lines) {
                fw.write(str + "\n");
            }
            int lastIndex = Integer.parseInt(lines.get(lines.size() - 1).split(",")[0]);
            String line = lastIndex + "," + s.getName() + "," + super.getUsername() + ",<";
            for (Product product : s.getProducts().keySet()) {
                line += product.getIndex() + ":" + product.getQuantity() + "/";
            }
            line = line.substring(0, line.length() - 1) + ">";
            fw.write(line + "\n");
            return lastIndex;
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        finally {
            if(fw != null) {
                try {
                    fw.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                    return -1;
                }
            }
        }
    }
    /**
     * Get the seller's available stores
     * @return the list of stores
     */
    public ArrayList<Store> getStores() {
        return stores;
    }
    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }
    /**
     * Creates a store and adds it to the stores ArrayList and to the stores.csv file
     */
    public void addStore(String storeName, String sellerName, String productFile, int sales, int revenue){
        Store s = new Store(storeName, sellerName, productFile, sales, revenue, 0);
        stores.add(s);
        int index = writeStoresFile("stores.csv", s);
        if(index == -1) {
            System.out.println("Something went wrong with adding your store!");
        }
        else {
            s.setIndex(index);
        }
    }
    /**
     * Prints the seller's dashboard
     */
    public void printDashboard() {
        for(Store s : stores) {
            System.out.println("Store: " + s.getName());
            s.displayStatistics();
        }
    }
    /**
     * Prints the seller's dashboard with filters
     */
    public void printSortedDashBoard(int sortType) {
        for(Store s : stores) {
            System.out.println("Store: " + s.getName());
            s.statisticsForSeller(sortType);
        }
    }
    public String toString() {
        String storesStr = "<";
        for(Store s: this.stores) {
            storesStr += s.getIndex() + "/";
        }
        storesStr = storesStr.substring(storesStr.length() - 1) + ">";
        return String.format("%s,%s,%s", this.getUsername(), this.getPassword(), storesStr);

    }
}
