import java.awt.color.ProfileDataException;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Buyer
 * An object representing a buyer user in the marketplace
 *
 * @author Jayden Deason
 * @version
 */
public class Buyer extends User {
    int index;
    File customers = new File("customers.csv");

    /**
     * Creates a new src.Buyer with a given username & password, and an empty shopping cart and purchase history
     *
     * @param username the buyer's username
     * @param password the buyer's password
     */
    public Buyer(String username, String password) throws badNamingException {
        super(username, password);
        if(username.contains(",")) {
            throw new badNamingException("Please do not have a comma in your username!");
        }
        if(password.contains(",")) {
            throw new badNamingException("Please do not have a comma in your password!");
        }
        int index = 0;
        if (customers.exists()) {
            try (BufferedReader bfr = new BufferedReader(new FileReader("Customers.csv"))) {
                String line = bfr.readLine();
                while (line != null) {
                    index = Integer.parseInt(line.split(",")[0]) + 1;
                    line = bfr.readLine();
                }
                this.index = index;
                FileOutputStream fos = new FileOutputStream("Customers.csv", true);
                PrintWriter pw = new PrintWriter(fos);
                pw.printf("%d,%s,%s,<>,<>\n", index, this.getUsername(), this.getPassword());

                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileOutputStream fos = new FileOutputStream("Customers.csv", true);
                PrintWriter pw = new PrintWriter(fos);
                pw.printf("0,%s,%s,<>,<>\n", this.getUsername(), this.getPassword());

                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Creates a new src.Buyer with the index, username, password, shopping cart, and purchase history
     * passed as a parameter string
     *
     * @param buyerLine formatted string containing all relevant info to create a Buyer
     */
    public Buyer(String buyerLine) {
        String[] buyerInfo = buyerLine.split(",");
        this.index = Integer.parseInt(buyerInfo[0]);
        this.setUsername(buyerInfo[1]);
        this.setPassword(buyerInfo[2]);
    }


    /**
     * Get the buyer's shopping cart contents
     *
     * @return the list of products in the buyer's shopping cart in the form index:quantity
     */
    public ArrayList<String> getShoppingCart() {
        ArrayList<String> cartItems = new ArrayList<String>();
        String cart = "";
        try (BufferedReader bfr = new BufferedReader(new FileReader("Customers.csv"))) {
            String line = bfr.readLine();
            while (line != null) {
                if (Integer.parseInt(line.split(",")[0]) == this.getIndex()) {
                    cart = line.split(",")[3].substring(1, line.split(",")[3].length() - 1);
                }
                line = bfr.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] cartArray = cart.split("/");
        Collections.addAll(cartItems, cartArray);
        return cartItems;
    }

    /**
     * Add a product to the shopping cart
     *
     * @param productIndex the index of the product to be added
     * @param quantity     the number of products to be added
     */
    public void addProductToCart(int productIndex, int quantity) {
        ArrayList<String> customerInfo = new ArrayList<String>();
        try (BufferedReader bfr = new BufferedReader(new FileReader("Customers.csv"))) {
            String line = bfr.readLine();
            while (line != null) {
                customerInfo.add(line);
                line = bfr.readLine();
            }
            FileOutputStream fos = new FileOutputStream("Customers.csv", false);
            PrintWriter pw = new PrintWriter(fos);
            for (int i = 0; i < customerInfo.size(); i++) {
                String[] lineArray = customerInfo.get(i).split(",");
                if (Integer.parseInt(lineArray[0]) == this.getIndex()) {
                    if (lineArray[3].length() > 2) {
                        pw.printf("%s,%s,%s,%s,%s\n", lineArray[0], lineArray[1], lineArray[2],
                                String.format("%s/%d:%d>", lineArray[3].substring(0, lineArray[3].length() - 1),
                                        productIndex, quantity), lineArray[4]);
                    } else {
                        pw.printf("%s,%s,%s,%s,%s\n", lineArray[0], lineArray[1], lineArray[2],
                                String.format("%s%d:%d>", lineArray[3].substring(0, lineArray[3].length() - 1),
                                        productIndex, quantity), lineArray[4]);
                    }
                } else {
                    pw.println(customerInfo.get(i));
                }
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Alter the quantity of a product in the buyer's cart
     *
     * @param productIndex the index of the product to be altered
     * @param newQuantity  the new quantity to be stored in the cart
     */
    public void editProductQuantity(int productIndex, int newQuantity) {
        boolean productFound = false;
        ArrayList<String> customerInfo = new ArrayList<String>();
        try (BufferedReader bfr = new BufferedReader(new FileReader("Customers.csv"))) {
            String line = bfr.readLine();
            while (line != null) {
                customerInfo.add(line);
                line = bfr.readLine();
            }
            FileOutputStream fos = new FileOutputStream("Customers.csv", false);
            PrintWriter pw = new PrintWriter(fos);
            for (int i = 0; i < customerInfo.size(); i++) {
                String[] lineArray = customerInfo.get(i).split(",");
                String[] cartListings = lineArray[3].substring(1, lineArray[3].length() - 1).split("/");
                String cartString = "<";

                if (lineArray[3].contains(productIndex + ":") && Integer.parseInt(lineArray[0]) == this.getIndex()) {
                    productFound = true;
                    for (int j = 0; j < cartListings.length; j++) {
                        if (cartListings[j].contains(productIndex + ":")) {
                            cartListings[j] = String.format("%d:%d", productIndex, newQuantity);
                        }
                    }
                    for (int j = 0; j < cartListings.length; j++) {
                        if (!cartListings[j].contains("0"))
                            cartString += cartListings[j] + "/";
                    }
                    if (cartString.length() > 2)
                        lineArray[3] = cartString.substring(0, cartString.length() - 1) + ">";
                    else
                        lineArray[3] = cartString + ">";
                } else if (i < customerInfo.size() - 1 && !productFound) {
                    System.out.println("No matching product in cart");
                }
                String lineString = "";
                for (int j = 0; j < lineArray.length; j++) {
                    lineString += lineArray[j] + ",";
                }
                lineString = lineString.substring(0, lineString.length() - 1);
                pw.println(lineString);
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make a purchase by moving all products in the cart into the purchase history
     */
    public void makePurchase() {
        ArrayList<String> customerInfo = new ArrayList<String>();
        try (BufferedReader bfr = new BufferedReader(new FileReader("Customers.csv"))) {
            String line = bfr.readLine();
            while (line != null) {
                customerInfo.add(line);
                line = bfr.readLine();
            }
            FileOutputStream fos = new FileOutputStream("Customers.csv", false);
            PrintWriter pw = new PrintWriter(fos);
            for (int i = 0; i < customerInfo.size(); i++) {
                String[] lineArray = customerInfo.get(i).split(",");
                if (Integer.parseInt(lineArray[0]) == this.getIndex()) {
                    String temp = lineArray[3];
                    lineArray[3] = "<>";
                    if (lineArray[4].equals("<>"))
                        lineArray[4] = temp;
                    else {
                        lineArray[4] = lineArray[4].substring(0, lineArray[4].length() - 1) +
                                "/" + temp.substring(1);
                    }
                }
                String lineString = "";
                for (int j = 0; j < lineArray.length; j++) {
                    lineString += lineArray[j] + ",";
                }
                lineString = lineString.substring(0, lineString.length() - 1);
                pw.println(lineString);
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the index of the buyer
     */
    public int getIndex() {
        return index;
    }

    /**
     * Export the buyer info to a file
     *
     * @param filename path to export file
     */
    @Override
    public void exportToFile(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            ArrayList<String> customerInfo = new ArrayList<String>();
            ArrayList<String> productInfo = new ArrayList<String>();
            try (BufferedReader bfr = new BufferedReader(new FileReader("Customers.csv"))) {
                String line = bfr.readLine();
                while (line != null) {
                    customerInfo.add(line);
                    line = bfr.readLine();
                }
                FileOutputStream fos = new FileOutputStream(filename, true);
                PrintWriter pw = new PrintWriter(fos);
                pw.println("Product,Quantity");
                for (int i = 0; i < customerInfo.size(); i++) {
                    String[] lineArray = customerInfo.get(i).split(",");
                    if (Integer.parseInt(lineArray[0]) == this.getIndex()) {
                        String[] purchases = lineArray[4].substring(1, lineArray[4].length() - 1).split("/");
                        BufferedReader productReader = new BufferedReader(new FileReader("Products.csv"));
                        line = productReader.readLine();
                        while (line != null) {
                            productInfo.add(line);
                            line = productReader.readLine();
                        }
                        for (int j = 0; j < purchases.length; j++) {
                            for (int k = 0; k < productInfo.size(); k++) {
                                String[] productArray = productInfo.get(k).split(",");
                                if (Integer.parseInt(productArray[0]) == Integer.parseInt(purchases[j].substring(0, 1))) {
                                    purchases[j] = productArray[1] + "," + purchases[j].substring(2, 3);
                                    pw.println(purchases[j]);
                                    break;
                                }
                            }
                        }
                    }
                }
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            System.out.println("A file with that name already exists");
    }

    /**
     * Prints the buyer's dashboard
     */
    //@Override
    public void printDashboard(Scanner scan) {
        ArrayList<String> storeLines = new ArrayList<String>();
        ArrayList<Store> stores = new ArrayList<Store>();
        try (BufferedReader bfr = new BufferedReader(new FileReader("Stores.csv"))) {
            String line = bfr.readLine();
            while (line != null) {
                storeLines.add(line);
                stores.add(new Store(line));
                line = bfr.readLine();
            }
            printStoreInfo(stores);
            int answer;
            do {
                System.out.println("1. Sort by products sold");
                System.out.println("2. Sort by purchase history");
                System.out.println("3. Exit Dashboard");
                answer = scan.nextInt();
                if (answer == 1) {
                    stores.sort((s1, s2) -> Integer.compare(s1.getSales(), s2.getSales()));
                    Collections.reverse(stores);
                    printStoreInfo(stores);
                }
                if (answer == 2) {
                    ArrayList<Store> storesByHistory = new ArrayList<Store>();
                    for (int i = 0; i < stores.size(); i++) {
                        Store store = new Store(stores.get(i).getIndex(), stores.get(i).getName(),
                                stores.get(i).getSellerName(), 0, 0.0, stores.get(i).getProductIndices(),
                                "<0:0>");
                        storesByHistory.add(i, store);
                    }
                    String history;
                    ArrayList<String> purchaseHistory = new ArrayList<String>();
                    BufferedReader customerReader = new BufferedReader(new FileReader("Customers.csv"));
                    String customerLine = customerReader.readLine();
                    while (customerLine != null) {
                        if (Integer.parseInt(customerLine.split(",")[0]) == this.getIndex()) {
                            if (customerLine.split(",")[4].equals("<>")) {
                                System.out.println("No history to compare to");
                                break;
                            } else {
                                history = customerLine.split(",")[4].substring(1,
                                        customerLine.split(",")[4].length() - 1);
                                if (history.length() > 3)
                                    Collections.addAll(purchaseHistory, history.split("/"));
                                else
                                    purchaseHistory.add(history);
                            }
                        }
                        customerLine = customerReader.readLine();
                    }
                    //ArrayList<ArrayList<String>> products = new ArrayList<ArrayList<String>>();
                    ArrayList<Product> productList = new ArrayList<Product>();
                    for (String productInfo : purchaseHistory) {
                        BufferedReader productReader = new BufferedReader(new FileReader("Products.csv"));
                        String productLine = productReader.readLine();
                        while (productLine != null) {
                            if (productInfo.substring(0,1).equals(productLine.split(",")[0])) {
                                Product product = new Product(productLine);
                                product.setQuantity(Integer.parseInt(productInfo.substring(2)));
                                productList.add(product);
                            }
                            productLine = productReader.readLine();
                        }
                    }

                    for (int i = 0; i < storesByHistory.size(); i++) {
                        ArrayList<String> temp = new ArrayList<>();
                        for (int j = 0; j < productList.size(); j++) {
                            if (storesByHistory.get(i).getName().equals(productList.get(j).getStoreName())) {
                           //     temp.add(purchaseHistory.get(j));
                                storesByHistory.get(i).setSales(storesByHistory.get(i).getSales()
                                        + Integer.parseInt(purchaseHistory.get(j).substring(2)));
                            }
                        }
                      //  products.add(i, temp);
                    }
                    storesByHistory.sort((s1, s2) -> Integer.compare(s1.getSales(), s2.getSales()));
                    Collections.reverse(storesByHistory);
                    printStoreInfo(storesByHistory);
                }
            } while (answer != 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printStoreInfo(ArrayList<Store> stores) throws IOException {
        for (Store store : stores) {
            ArrayList<Product> storeProducts = new ArrayList<Product>();
            System.out.println("Store: " + store.getName());
            ArrayList<Integer> productIndices = store.getProductsByIndex();
            for (int index : productIndices) {
                BufferedReader productReader = new BufferedReader(new FileReader("Products.csv"));
                String productLine = productReader.readLine();
                while(productLine != null) {
                    if (Integer.parseInt(productLine.split(",")[0]) == index) {
                        storeProducts.add(new Product(productLine));
                    }
                    productLine = productReader.readLine();
                }
            }
            for (Product product : storeProducts) {
                System.out.println("--" + product.getName() + ": " + product.getQuantity());
            }
        }
    }

    public String toString() {
        String customerString = "No existing customer";
        ArrayList<String> customerInfo = new ArrayList<String>();
        try (BufferedReader bfr = new BufferedReader(new FileReader("Customers.csv"))) {
            String line = bfr.readLine();
            while (line != null) {
                customerInfo.add(line);
                line = bfr.readLine();
            }
            for (int i = 0; i < customerInfo.size(); i++) {
                String[] lineArray = customerInfo.get(i).split(",");
                if (Integer.parseInt(lineArray[0]) == this.getIndex()) {
                    customerString = customerInfo.get(i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customerString;
    }
}
