import java.io.*;
import java.util.ArrayList;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

/**
 * Seller
 * <p>
 * A class that representing a seller user in the marketplace, has functionality for creating and storing stores,
 * displaying content for sellers, and exporting a csv of products from a store
 *
 * @author Visv Shah
 * @version 11/13/22
 */
public class Seller extends User implements java.io.Serializable{
    private ArrayList<Store> stores; // the seller's products
    private int index; // the index of the Seller in the Seller.csv file

    /**
     * Create  a new seller with a given email and password
     * When a seller gets created using this constructor, it gets added to the Sellers.csv file
     *
     * @param email    the seller's email
     * @param password the seller's password
     *                 throws badNaming Exception when a email of password contains a comma, as this would mess up the csv file
     */
    public Seller(String email, String password) throws BadNamingException {
        super(email, password);
        if (email.contains(",")) {
            throw new BadNamingException("Please do not have a comma in your email!");
        }
        if (password.contains(",")) {
            throw new BadNamingException("Please do not have a comma in your password!");
        }
        this.stores = new ArrayList<Store>();
        this.index = -1;
    }

    /**
     * Create a new seller using the line stored in the Seller.csv file, when this is called, it does not add a new line
     * to the Seller .csv file because it is only used to create a new Marketplace after relaunching the application.
     *
     * @param line the line taken from the Seller.csv file
     */
    public Seller(String line) {
        String[] info = line.split(",");
        this.index = Integer.parseInt(info[0]);
        this.setEmail(info[1]);
        this.setPassword(info[2]);
        this.stores = new ArrayList<>();
    }

//    public Seller(String line) {
//        super(line.split(",")[1], line.split(",")[2]);
//        String[] parts = line.split(",");
//        this.index = Integer.parseInt(parts[0]);
//        String[] storesIndex = parts[3].substring(1, parts[3].length() - 1).split("/");
//        this.stores = new ArrayList<Store>();
//        int i = 0;
//        BufferedReader br = null;
//        try {
//            br = new BufferedReader(new FileReader("Stores.csv"));
//            String l = br.readLine();
//            while (l != null) {
//                for (String n : storesIndex) {
//                    if (Integer.parseInt(n) == i) {
//                        Store s = new Store(l);
//                        this.stores.add(s);
//                        break;
//                    }
//                }
//                l = br.readLine();
//                i++;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (br != null) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    /**
     * Adds a new store to Stores.csv. First reads Stores.csv and stores the lines and then writes Stores.csv including
     * the new store
     *
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
            for (String str : lines) {
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
     *
     * @params indexToAdd : states the index of the new store in the Store.csv file
     */
    public int updateSellerFile(int indexToAdd) {
        ArrayList<String> lines = new ArrayList<String>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("Sellers.csv"));
            String line = br.readLine();
            while (line != null) {
                if (line.split(",")[1].equals(this.getEmail())) {
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
            for (String str : lines) {
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
     *
     * @param fileName  the fileName that the user wants to write a new file to
     * @param storeName the store that the user wants to export products from
     * @return the index of the store
     */
    public void exportProducts(String fileName, String storeName) {
        for (Store s : stores) {
            if (s.getName().equals(storeName)) {
                FileWriter fw = null;
                try {
                    fw = new FileWriter(fileName);

                    for (Product product : s.getProducts()) {
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

    public ArrayList<Product> getProducts(boolean nonzero) {
        ArrayList<Product> out = new ArrayList<>();

        for (Store s : stores) {
            for (Product p : s.getProducts()) {
                if (p.getQuantity() != 0) out.add(p);
            }
        }

        return out;
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }

    /**
     * This function is used to create a new store. It makes no store with the same name exists already for that
     * seller. Then, It will call the functions to save that store in the Stores.csv and update the Sellers.csv row
     * with a reference to the new store.
     */
    public int addStore(String storeName, Market market) {
        for (Store store : stores) {
            if (store.getName().equals(storeName)) {
                System.out.println("Error: You already have a store with the same name!");
                return -1;
            }
        }
        Store s = new Store(0, storeName, this.getEmail(), 0, 0, "", "");
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

    public void addStore(Store s) {
        stores.add(s);
    }

    /**
     * Prints the seller's dashboard with a sortType
     *
     * @param sortType if sortType == 0, then it will not sort
     *                 if sortType == 1, then it will print everything ordered alphabetically
     *                 if sortType == 2, then it will print everything based on the quantity of products being dealt
     *                 with
     */
    public void printDashboard(int sortType, Market market) {
        System.out.println("------------------------------------------");
        for (Store s : stores) {
            System.out.println("Store: " + s.getName());
            for (String line : s.statisticsForSeller(sortType, market)) {
                System.out.println(line);
            }
        }
        System.out.println("------------------------------------------");

    }

    public String getDashboardString(int sortType, Market market) {
        ArrayList<String> out = new ArrayList<>();
        out.add("------------------------------------------");
        for (Store s : stores) {
            out.add("Store: " + s.getName());
            out.addAll(s.statisticsForSeller(sortType, market));

        }
        out.add("------------------------------------------");

        return String.join("\n", out);
    }

    /**
     * Prints the seller object in a format that matches the Seller.csv row. It does not include the index.
     */
    public String toString() {
        String storesStr = "<";
        for (int i = 0; i < stores.size(); i++) {
            storesStr += stores.get(i).getIndex();

            if (i != stores.size() - 1) {
                storesStr += "/";
            }
        }
        storesStr += ">";
        // hacky solution if there's a slash at the beginning for some reason
        if (storesStr.charAt(1) == '/') {
            storesStr = storesStr.substring(0, 2) + storesStr.substring(2);
        }
        return String.format("%d,%s,%s,%s", this.getIndex(), this.getEmail(), this.getPassword(), storesStr);

    }

    public int getIndex() {
        return index;
    }

    /**
     * Prints all of the products in customer carts
     *
     * @param market the entire marketplace that this seller is in
     */
    public void viewProductsInCart(Market market) {
        System.out.println("In Customer Carts: ");
        int number = 1;
        for (Buyer b : market.getBuyers()) {
            ArrayList<String> items = b.getShoppingCart();
            for (String item : items) {
                Product p = market.getProductByIndex(Integer.parseInt(item.split(":")[0]));
                if (market.getStoreByName(p.getStoreName()).getSellerName().equals(this.getEmail())) {
                    System.out.println(number + ") " + b.getEmail());
                    number++;
                    System.out.println("Product: " + p.getName() + " | Quantity: " + item.split(":")[1]);
                    System.out.println("Store: " + p.getStoreName() + " | Description: " + p.getDescription());
                }
            }


        }
    }
    public HashMap<Product, String> sendProductsInCart(Market market) {
        HashMap<Product, String> productsInCart = new HashMap<Product, String>();
        for (Buyer b : market.getBuyers()) {
            ArrayList<String> items = b.getShoppingCart();
            for (String item : items) {
                Product p = market.getProductByIndex(Integer.parseInt(item.split(":")[0]));
                if (market.getStoreByName(p.getStoreName()).getSellerName().equals(this.getEmail())) {
                    String s = b.getEmail() + "," + item.split(":")[1];
                    productsInCart.put(p, s);
                }
            }
        }
        return productsInCart;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    public Store getStoreByName(String storeName) {
        for (Store s : stores) {
            if (s.getName().equals(storeName)) {
                return s;
            }
        }

        return null;
    }

    public ArrayList<String> getStoreNames() {
        ArrayList<String> out = new ArrayList<>();
        for (Store s : stores) {
            out.add(s.getName());
        }

        return out;
    }

    /**
     * the main method is used for testing.
     */
    public static void main(String[] args) throws BadNamingException {
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
        } catch (BadNamingException e) {
            System.out.println(e.getMessage().equals("Please do not have a comma in your password!"));
            System.out.println("Actual: \"" + e.getMessage() + "\" == Expected: \"Please do not have a comma in your " +
                    "password!\"");

        }
        //Testing the addStore method updates the files appropriately
        System.out.print("Test 3 - Testing that Stores.csv is updated with creation of new store: ");
//        s1.addStore("testStore");
        BufferedReader br1 = null;
        int newStoreIndex = 0;
        try {
            br1 = new BufferedReader(new FileReader("Stores.csv"));
            String line = br1.readLine();
            String line2 = "";
            int index = -1;
            while (line != null) {
                line = br1.readLine();
                if (line != null) {
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
                if (line != null && s1.getEmail().equals(line.split(",")[1])) {
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
//        s1.addStore("testStore");
        System.out.println("Actual: \"" + "Error: You already have a store with the same name!" + "\"" + " == Expected: \"Error: You already have a store with the same name!\"");
    }

    public ArrayList<String> getStoreNamesSorted(String sortType) {
        ArrayList<Store> temp = new ArrayList<>(getStores());
        if (sortType.equals("sales")) {
            temp.sort((s1, s2) -> {
                return s1.getSales(Market.getInstance()) - s2.getSales(Market.getInstance());
            });
        } else if (sortType.equals("alphabet")) {
            temp.sort((s1,s2) -> {
                return s1.getName().compareTo(s2.getName());
            });
        }

        ArrayList<String> out = new ArrayList<>();
        for (Store store : temp) {
            out.add(store.getName());
        }

        return out;
    }

}