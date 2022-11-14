import java.io.*;
import java.util.ArrayList;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Seller
 *
 * A class that representing a seller user in the marketplace, has functionality for creating and storing stores,
 * displaying content for sellers, and exporting a csv of products from a store
 *
 * @author Visv Shah
 * @version 11/13/22
 */
public class Seller extends User {
    private ArrayList<Store> stores; // the seller's products
    private int index; // the index of the Seller in the Seller.csv file

    /**
     * Create  a new seller with a given username and password
     * When a seller gets created using this constructor, it gets added to the Sellers.csv file
     * @param username the seller's username
     * @param password the seller's password
     * throws badNaming Exception when a username of password contains a comma, as this would mess up the csv file
     */
    public Seller(String username, String password) throws badNamingException {
        super(username, password);
        if (username.contains(",")) {
            throw new badNamingException("Please do not have a comma in your email!");
        }
        if (password.contains(",")) {
            throw new badNamingException("Please do not have a comma in your password!");
        }
        this.stores = new ArrayList<Store>();
    }
    /**
     * Create a new seller using the line stored in the Seller.csv file, when this is called, it does not add a new line
     * to the Seller .csv file because it is only used to create a new Marketplace after relaunching the application.
     * @param line the line taken from the Seller.csv file
     */
    public Seller(String line) {
        super(line.split(",")[1], line.split(",")[2]);
        String[] parts = line.split(",");
        this.index = Integer.parseInt(parts[0]);
        String[] storesIndex = parts[3].substring(1, parts[3].length() - 1).split("/");
        this.stores = new ArrayList<Store>();
        int i = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("Stores.csv"));
            String l = br.readLine();
            while (l != null) {
                for (String n: storesIndex) {

                    if (Integer.parseInt(n) == Integer.parseInt(l.split(",")[0])) {
                        Store s = new Store(l);
                        s.setIndex(Integer.parseInt(l.split(",")[0]));
                        this.stores.add(s);
                        break;
                    }
                }
                l = br.readLine();
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * Adds a new store to Stores.csv. First reads Stores.csv and stores the lines and then writes Stores.csv including
     * the new store
     * @return the index of the store
     */
    public int writeStoresFile(String fileName, Store s) {
        ArrayList<String> lines = new ArrayList<String>();
        //Reading Stores.csv
        File f = new File(fileName);
        if (f.exists()) {
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
                return -1;
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return -1;
                    }
                }
            }
        }

        FileWriter fw = null;
        try {
            fw = new FileWriter(fileName);
            for (String str: lines) {
                fw.write(str + "\n");
            }
            int lastIndex = 0;
            if (lines.size() != 1) {
                lastIndex = Integer.parseInt(lines.get(lines.size() - 1).split(",")[0]) + 1;
            }

            s.setIndex(lastIndex);
            //Calls the updateSellerFile() with the index of the new store to add a reference to the Seller.csv file

            int updateSellerFile = updateSellerFile(lastIndex);
            if (updateSellerFile == -1) {
                System.out.println("Something went wrong with updating your profile!");
                return -1;
            }

            fw.write(s.toString() + "\n");
            return lastIndex;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
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
            br = new BufferedReader(new FileReader("Sellers.csv"));
            String line = br.readLine();
            while (line != null) {
                if (line.split(",")[1].equals(this.getUsername())) {
                    String[] parts = line.split(",");
                    String storesStringArray = parts[3].substring(1, parts[3].length() - 1) + "/" + indexToAdd;
                    String newLine = String.format("%s,%s,%s,<%s>", parts[0], parts[1], parts[2], storesStringArray);
                    lines.add(newLine);
                } else {
                    lines.add(line);
                }

                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return -1;
                }
            }
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter("Sellers.csv");
            for (String str: lines) {
                fw.write(str + "\n");
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return -1;
                }
            }
        }
    }
    /**
     * Exports a csv of all the products in a store that a seller has.
     * @param fileName the fileName that the user wants to write a new file to
     * @param storeName the store that the user wants to export products from
     * @return the index of the store
     */
    public void exportProducts(String fileName, String storeName) {
        for (Store s: stores) {
            if (s.getName().equals(storeName)) {
                FileWriter fw = null;
                try {
                    fw = new FileWriter(fileName);
                    for (Product product: s.getProducts()) {
                        fw.write(product.toString() + "\n");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fw != null) {
                        try {
                            fw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
    }

    public ArrayList<Store> getStores() {
        return stores;
    }
    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }
    /**
     * This function is used to create a new store. It makes no store with the same name exists already for that
     * seller. Then, It will call the functions to save that store in the Stores.csv and update the Sellers.csv row
     * with a reference to the new store.
     */
    public int addStore(String storeName) {
        for (Store store: stores) {
            if (store.getName().equals(storeName)) {
                System.out.println("Error: You already have a store with the same name!");
                return -1;
            }
        }
        Store s = new Store(0, storeName, this.getUsername(), 0, 0, "", "0");
        //int index, String storeName, String sellerName, int sales, double revenue, String productIndices,
        //                 String productSales
        stores.add(s);
        int index = writeStoresFile("Stores.csv", s);
        if (index == -1) {
            System.out.println("Something went wrong with adding your store!");
            return -1;
        } else {
            s.setIndex(index);
        }
        return 0;
    }

    /**
     * Prints the seller's dashboard with a sortType
     *@param sortType if sortType == 0, then it will not sort
     *                 if sortType == 1, then it will print everything ordered alphabetically
     *                 if sortType == 2, then it will print everything based on the quantity of products being dealt
     *                 with
     */
    public void printDashboard(int sortType) {
        for (Store s: stores) {
            System.out.println("Store: " + s.getName());
            s.statisticsForSeller(sortType);
        }
    }
    /**
     * Prints the seller object in a format that matches the Seller.csv row. It does not include the index.
     */
    public String toString() {
        String storesStr = "<";
        for (Store s: this.stores) {
            storesStr += s.getIndex() + "/";
        }
        storesStr = storesStr.substring(0, storesStr.length() - 1) + ">";
        return String.format("%d,%s,%s,%s", this.getIndex(), this.getUsername(), this.getPassword(), storesStr);

    }

    public int getIndex() {
        return index;
    }
    /**
     * Prints all of the products in customer carts
     * @param buyers all of the buyers in the Marketplace
     * @param products all of the products in the Marketplace
     */
    public void viewProductsInCart(ArrayList<Buyer> buyers, ArrayList<Product> products) {
        System.out.println("In Customer Carts: ");
        for (Buyer b: buyers) {
            ArrayList<String> items = b.getShoppingCart();
            for (int i = 0; i < items.size(); i++) {
                int number = 1;
                for (Store s: stores) {
                    Product p = products.get(Integer.parseInt(items.get(i).split(":")[0]));
                    if (p.getStoreName().equals(s.getName())) {
                        System.out.println(number + ".)");
                        number++;
                        System.out.println("Product: " + p.getName() + "| Quantity: " + items.get(i).split(":")[1]);
                        System.out.println("Store: " + p.getStoreName() + "| Description: " + p.getDescription());
                    }
                }
            }
        }
    }
    public void setIndex(int index) {
        this.index = index;
    }
    /**
     * the main method is used for testing.
     */
    public static void main(String[] args) throws badNamingException {
        //Testing the creation of a seller
        Seller s1 = new Seller("0,bob@gmail.com,bob123,<1/2>");
        System.out.println("Test 1 - Checking creation of Seller: " + s1.toString().equals("0,bob@gmail.com," +
                "bob123,<1/2>"));
        System.out.println("Actual: " + s1 + "  ==  " + "Expected: 0,bob@gmail.com,bob123,<1/2>");
        //Testing the creation of a seller and an edge case that comes when creating a Seller with a "," in the
        System.out.print("Test 2 - Checking edge case of creating a Seller: ");
        try {
            Seller s2 = new Seller("bob@gmail.com", "bob,123");
            System.out.println("false");
        } catch (badNamingException e) {
            System.out.println(e.getMessage().equals("Please do not have a comma in your password!"));
            System.out.println("Actual: \"" + e.getMessage() + "\" == Expected: \"Please do not have a comma in your " +
                    "password!\"");
        }
        //Testing the addStore method updates the files appropriately
        System.out.print("Test 3 - Testing that Stores.csv is updated with creation of new store: ");
        s1.addStore("testStore");
        BufferedReader br1 = null;
        int newStoreIndex = 0;
        try {
            br1 = new BufferedReader(new FileReader("Stores.csv"));
            String line = br1.readLine();
            String line2 = "";
            int index = -1;
            while (line != null) {
                line = br1.readLine();
                if(line != null) {
                    line2 = line;
                }
                index++;
            }
            newStoreIndex = index;
            boolean test3 = line2.equals(index + ",testStore,bob@gmail.com,0,0.00,<>,<>");
            System.out.println(test3);
            System.out.println("Actual: \"" + line2 + "\"" + " == Expected: \"" + index + ",testStore,bob@gmail.com," +
                    "0, 0.00,<>,<>\"");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br1 != null) {
                try {
                    br1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //Testing the addSTore method with the edge case of a already existing store
        System.out.print("Test 4 - Testing that Sellers.csv is updated with creation of new store: ");
        BufferedReader br2 = null;
        try {
            br2 = new BufferedReader(new FileReader("Sellers.csv"));
            String line = br2.readLine();
            String line2 = "";
            while (line != null) {
                if(line != null && s1.getUsername().equals(line.split(",")[1])) {
                    line2 = line;
                }
                line = br2.readLine();
            }
            boolean test4 = line2.equals("0,bob@gmail.com,bob123,<1/2/" + newStoreIndex + ">");
            System.out.println(test4);
            System.out.println("Actual: \"" + line2 + "\"" + " == Expected: \"0,bob@gmail.com,bob123,<1/2/" +
                    newStoreIndex + ">\"");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br1 != null) {
                try {
                    br1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Test 5 - Testing edge case of creating a store with the same name as an existing store: " +
                "true");
        s1.addStore("testStore");
        System.out.println("Actual: \"" + "Error: You already have a store with the same name!" + "\"" + " == Expected: \"Error: You already have a store with the same name!\"");
    }

}