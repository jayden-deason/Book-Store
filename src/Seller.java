import java.io.*;
import java.util.ArrayList;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Seller
 * <p>
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
                Integer.parseInt(lines.get(lines.size() - 1).split(",")[0]);
            }
            s.setIndex(lastIndex);
            //Calls the updateSellerFile() with the index of the new store to add a reference to the Seller.csv file
            int updateSellerFile = updateSellerFile(lastIndex);
            if (updateSellerFile == -1) {
                System.out.println("Something went wrong with updating your profile!");
                return -1;
            }
            String line = lastIndex + "," + s.getName() + "," + super.getEmail() + ",<";
            for (Product product : s.getProducts()) {
                line += product.getIndex() + ":" + product.getQuantity() + "/";
            }
            line = line.substring(0, line.length() - 1) + ">";
            fw.write(line + "\n");
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
                    String newLine = String.format("%s,%s,%s,%s", parts[0], parts[1], parts[2], storesStringArray);
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
                    int index = 0;
                    for (Product product : s.getProducts()) {
                        fw.write(index + "," + product.toString() + "\n");
                        index++;
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
    public void addStore(String storeName) {
        for (Store store : stores) {
            if (store.getName().equals(storeName)) {
                System.out.println("Error: You already have a store with the same name!");
                return;
            }
        }
        Store s = new Store(0, storeName, this.getEmail(), 0, 0, "", "");
        //int index, String storeName, String sellerName, int sales, double revenue, String productIndices,
        //                 String productSales
        stores.add(s);
        int index = writeStoresFile("Stores.csv", s);
        if (index == -1) {
            System.out.println("Something went wrong with adding your store!");
        } else {
            s.setIndex(index);
        }
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
     *                 wit
     */
    public void printDashboard(int sortType) {
//        for (Store s : stores) {
//            System.out.println("Store: " + s.getName());
//            s.statisticsForSeller(sortType);
//        }
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
        return String.format("%d,%s,%s,%s", this.getIndex(), this.getEmail(), this.getPassword(), storesStr);

    }

    public int getIndex() {
        return index;
    }

    /**
     * Prints all of the products in customer carts
     *
     * @param market   the entire marketplace that this seller is in
     */
    public void viewProductsInCart(Market market) {
        System.out.println("In Customer Carts: ");
        for (Buyer b : market.getBuyers()) {
            ArrayList<String> items = b.getShoppingCart();
            for (String item : items) {
                int number = 1;
                for (Store s : stores) {
                    Product p = market.getProductByIndex(Integer.parseInt(item.split(":")[0]));
                    System.out.println(number + ")");
                    number++;
                    System.out.println("Product: " + p.getName() + "| Quantity: " + item.split(":")[1]);
                    System.out.println("Store: " + p.getStoreName() + "| Description: " + p.getDescription());

                }
            }
        }
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

    /**
     * the main method is used for testing.
     */
    public static void main(String[] args) throws BadNamingException {
        //Testing the creation of a seller
        Seller s1 = new Seller("0,bob@gmail.com,bob123,<1/2>");
        System.out.println(s1 + "==" + "0,bob@gmail.com,bob123,<1/2> : " + s1.toString().equals("0,bob@gmail.com," +
                "bob123,<1/2>"));
        //Testing the creation of a seller and an edge case that comes when creating a Seller with a "," in the
        try {
            Seller s2 = new Seller("bob@gmail.com", "bob,123");
        } catch (BadNamingException e) {
            System.out.println("badNamingException thrown for password: bob,123");
        }
        //Testing the addStore method updates the files appropriately
        //Testing the addSTore method with the edge case of a already existing store
    }

}