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
    private int index;

    /**
     * Create  a new seller with a given username, password, and list of products
     * When a seller gets created using this constructor, it gets added to the Sellers.csv file
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
     * Create a new seller using the line store in the Seller.csv file, this does not add a new line to the Seller
     * .csv file because it is only used to create a new Marketplace after relaunching the application
     * @param line the line taken from the Seller.csv file
     */
    public Seller(String line) {
        super(line.split(",")[1], line.split(",")[2]);
        String[] parts = line.split(",");
        this.index = Integer.parseInt(parts[0]);
        String[] storesIndex = parts[3].substring(1, parts[3].length() - 2).split("/");
        this.stores = new ArrayList<Store>();
        int i = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("Stores.csv"));
            String l = br.readLine();
            while(l != null) {
                for(String n : storesIndex) {
                    if(Integer.parseInt(n) == i) {
                        Store s = new Store(l);
                        this.stores.add(s);
                        break;
                    }
                }
                l = br.readLine();
                i++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(br != null) {
                try {
                    br.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
            return -1;
        }
        finally {
            if(br != null) {
                try {
                    br.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                    return -1;
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
            s.setIndex(lastIndex);
            int updateSellerFile = updateSellerFile(lastIndex);
            if(updateSellerFile == -1) {
                System.out.println("Something went wrong with updating your profile!");
                return -1;
            }
            String line = lastIndex + "," + s.getName() + "," + super.getUsername() + ",<";
            for (Product product : s.getProducts()) {
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
     * Updates Seller.csv when a new store is added, by storing a reference to that store in the seller's row.
     * @params indexToAdd : states the index of the new store in the Store.csv file
     */
    public int updateSellerFile(int indexToAdd) {
        ArrayList<String> lines = new ArrayList<String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("Seller.csv"));
            String line = br.readLine();
            while(line != null) {
                if(line.split(",")[1].equals(this.getUsername())) {
                    String[] parts = line.split(",");
                    String storesStringArray = parts[3].substring(1, parts[3].length() - 1) + "/" + indexToAdd;
                    String newLine = String.format("%s,%s,%s,%s", parts[0], parts[1], parts[2], storesStringArray);
                    lines.add(newLine);
                }
                else {
                    lines.add(line);
                }

                line = br.readLine();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        finally {
            if(br != null) {
                try {
                    br.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                    return -1;
                }
            }
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter("Seller.csv");
            for(String str : lines) {
                fw.write(str + "\n");
            }
            return 0;
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
     * Exports a
     * @return the index of the store
     */
    public void exportProducts(String fileName, String storeName){
        for(Store s: stores) {
            if(s.getName().equals(storeName)) {
                FileWriter fw = null;
                try {
                    fw = new FileWriter(fileName);
                    int index = 0;
                    for(Product product : s.getProducts()) {
                        fw.write(index + "," + product.toString() + "\n");
                        index++;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if(fw != null) {
                        try {
                            fw.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
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
    public void addStore(String storeName){
        for(Store store : stores) {
            if(store.getName().equals(storeName)) {
                System.out.println("Error: You already have a store with the same name!");
                return;
            }
        }
        Store s = new Store(0, storeName, this.getUsername(), 0, 0, "", "0");
        //int index, String storeName, String sellerName, int sales, double revenue, String productIndices,
        //                 String productSales
        stores.add(s);
        int index = writeStoresFile("Stores.csv", s);
        if(index == -1) {
            System.out.println("Something went wrong with adding your store!");
        }
        else {
            s.setIndex(index);
        }
    }

    /**
     * Prints the seller's dashboard with a sortType
     *@param sortType if sortType == 0, then it will not sort
     *                 if sortType == 1, then it will print everything ordered alphabetically
     *                 if sortType == 2, then it will print everything based on the quantity of products being dealt
     *                 wit
     */
    public void printDashboard(int sortType) {
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
    public static void main(String[] args) throws badNamingException{
        Seller s = new Seller("0,bob@gmail.com,bob123,<1/2>");
        System.out.println(s);
    }
}