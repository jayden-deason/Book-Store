import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Buyer
 * An object representing a buyer user in the marketplace
 *
 * @author Jayden Deason - lab sec 001
 * @version November 13, 2022
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
        if(username.contains(",")) { //tests for correct username formatting
            throw new badNamingException("Please do not have a comma in your username!");
        }
        if(password.contains(",")) { //tests for correct password formatting
            throw new badNamingException("Please do not have a comma in your password!");
        }
        int index = 0;
        if (customers.exists()) { //runs for adding new buyer to the customer.csv
            try (BufferedReader bfr = new BufferedReader(new FileReader("Customers.csv"))) {
                String line = bfr.readLine();
                while (line != null) {
                    index = Integer.parseInt(line.split(",")[0]) + 1; //increments for the last existing customer index plus one
                    line = bfr.readLine();
                }
                this.index = index;
                FileOutputStream fos = new FileOutputStream("Customers.csv", true);
                PrintWriter pw = new PrintWriter(fos);
                pw.printf("%d,%s,%s,<>,<>\n", index, this.getUsername(), this.getPassword()); //adds this buyer to the customers.csv

                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { //runs for adding the first buyer to the customer.csv
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
        ArrayList<String> cartItems = new ArrayList<String>(); //arraylist for items in the cart
        String cart = "";
        try (BufferedReader bfr = new BufferedReader(new FileReader("Customers.csv"))) {
            String line = bfr.readLine();
            while (line != null) {
                if (Integer.parseInt(line.split(",")[0]) == this.getIndex()) { //runs if the index of the line matches that of the customer
                    cart = line.split(",")[3].substring(1, line.split(",")[3].length() - 1); //parses the cart from the customers.csv
                }
                line = bfr.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] cartArray = cart.split("/");
        Collections.addAll(cartItems, cartArray); //puts the cart items in an array into the arraylist to be returned
        return cartItems;
    }

    /**
     * Add a product to the shopping cart
     *
     * @param productIndex the index of the product to be added
     * @param quantity     the number of products to be added
     */
    public void addProductToCart(int productIndex, int quantity) {
        ArrayList<String> customerInfo = new ArrayList<String>(); //arraylist containing all the customer lines from customers.csv
        try (BufferedReader bfr = new BufferedReader(new FileReader("Customers.csv"))) {
            String line = bfr.readLine();
            while (line != null) {
                customerInfo.add(line); //adds lines to customerInfo
                line = bfr.readLine();
            }
            FileOutputStream fos = new FileOutputStream("Customers.csv", false);
            PrintWriter pw = new PrintWriter(fos);
            for (int i = 0; i < customerInfo.size(); i++) {
                String[] lineArray = customerInfo.get(i).split(","); //splits the line into its parts stored in an array
                if (Integer.parseInt(lineArray[0]) == this.getIndex()) { //runs if the index of the line matches that of the customer
                    if (lineArray[3].length() > 2) { //adds products into a cart already containing products
                        pw.printf("%s,%s,%s,%s,%s\n", lineArray[0], lineArray[1], lineArray[2],
                                String.format("%s/%d:%d>", lineArray[3].substring(0, lineArray[3].length() - 1),
                                        productIndex, quantity), lineArray[4]);
                    } else { //adds products into an empty cart
                        pw.printf("%s,%s,%s,%s,%s\n", lineArray[0], lineArray[1], lineArray[2],
                                String.format("%s%d:%d>", lineArray[3].substring(0, lineArray[3].length() - 1),
                                        productIndex, quantity), lineArray[4]);
                    }
                } else {
                    pw.println(customerInfo.get(i)); //re-writes the lines to the customers.csv
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
        boolean productFound = false; //variable to track if the product to be edited has been found
        ArrayList<String> customerInfo = new ArrayList<String>(); //arraylist containing all the customer lines from customers.csv
        try (BufferedReader bfr = new BufferedReader(new FileReader("Customers.csv"))) {
            String line = bfr.readLine();
            while (line != null) {
                customerInfo.add(line); //adds lines to customerInfo
                line = bfr.readLine();
            }
            FileOutputStream fos = new FileOutputStream("Customers.csv", false);
            PrintWriter pw = new PrintWriter(fos);
            for (int i = 0; i < customerInfo.size(); i++) {
                String[] lineArray = customerInfo.get(i).split(","); //array containing the various aspects of a customer
                String[] cartListings = lineArray[3].substring(1, lineArray[3].length() - 1).split("/"); //array containing products in the cart
                String cartString = "<";

                if (lineArray[3].contains(productIndex + ":") && Integer.parseInt(lineArray[0]) == this.getIndex()) { //runs if the product to be edited has been found on the line
                    productFound = true;
                    for (int j = 0; j < cartListings.length; j++) { //runs through all products in cart
                        if (cartListings[j].contains(productIndex + ":")) { //runs if the correct product has been found
                            cartListings[j] = String.format("%d:%d", productIndex, newQuantity);
                        }
                    }
                    for (int j = 0; j < cartListings.length; j++) {
                        if (!cartListings[j].contains("0")) //runs if setting the quantity to 0 and removes the product from the cart
                            cartString += cartListings[j] + "/";
                    }
                    if (cartString.length() > 2) //updates the cart if there were multiple products in the cart
                        lineArray[3] = cartString.substring(0, cartString.length() - 1) + ">";
                    else //updates the cart if there was only one product in the cart
                        lineArray[3] = cartString + ">";
                } else if (i < customerInfo.size() - 1 && !productFound) { //runs there was no matching product in the cart
                    System.out.println("No matching product in cart");
                }
                String lineString = "";
                for (int j = 0; j < lineArray.length; j++) { //rebuilds the line to be written to the customers.csv
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
        ArrayList<String> customerInfo = new ArrayList<String>(); //arraylist containing all the customer lines from customers.cs
        try (BufferedReader bfr = new BufferedReader(new FileReader("Customers.csv"))) {
            String line = bfr.readLine();
            while (line != null) {
                customerInfo.add(line); //adds lines to customerInfo
                line = bfr.readLine();
            }
            FileOutputStream fos = new FileOutputStream("Customers.csv", false);
            PrintWriter pw = new PrintWriter(fos);
            for (int i = 0; i < customerInfo.size(); i++) {
                String[] lineArray = customerInfo.get(i).split(","); //splits the customer line into its elements
                if (Integer.parseInt(lineArray[0]) == this.getIndex()) { //runs when the line index matches that of the customer
                    String temp = lineArray[3]; //sets temp to be the shopping cart
                    lineArray[3] = "<>"; //sets the shopping cart to be empty
                    if (lineArray[4].equals("<>")) //runs if the purchase history is empty and swaps with the shopping cart
                        lineArray[4] = temp;
                    else { //runs if the purchase history is not empty and appends the cart to the history
                        lineArray[4] = lineArray[4].substring(0, lineArray[4].length() - 1) +
                                "/" + temp.substring(1);
                    }
                }
                String lineString = "";
                for (int j = 0; j < lineArray.length; j++) { //builds the line to be written to the customer file
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
        if (!file.exists()) { //checks that the file to be exported doesn't already exist
            ArrayList<String> customerInfo = new ArrayList<String>(); //arraylist holding all the customer lines
            ArrayList<String> productInfo = new ArrayList<String>(); //arraylist holding all the product lines
            try (BufferedReader bfr = new BufferedReader(new FileReader("Customers.csv"))) {
                String line = bfr.readLine();
                while (line != null) {
                    customerInfo.add(line);
                    line = bfr.readLine();
                }
                FileOutputStream fos = new FileOutputStream(filename, true);
                PrintWriter pw = new PrintWriter(fos);
                pw.println("Product,Quantity"); //prints headers for the exported *.csv file
                for (int i = 0; i < customerInfo.size(); i++) {
                    String[] lineArray = customerInfo.get(i).split(",");
                    if (Integer.parseInt(lineArray[0]) == this.getIndex()) {
                        String[] purchases = lineArray[4].substring(1, lineArray[4].length() - 1).split("/"); //adds purchase history to an array
                        BufferedReader productReader = new BufferedReader(new FileReader("Products.csv"));
                        line = productReader.readLine();
                        while (line != null) {
                            productInfo.add(line);
                            line = productReader.readLine();
                        }
                        for (int j = 0; j < purchases.length; j++) { //finds the matching products lines for products in the purchase history
                            for (int k = 0; k < productInfo.size(); k++) {
                                String[] productArray = productInfo.get(k).split(",");
                                if (Integer.parseInt(productArray[0]) == Integer.parseInt(purchases[j].substring(0, 1))) {
                                    purchases[j] = productArray[1] + "," + purchases[j].substring(2, 3);
                                    pw.println(purchases[j]); //writes the product name and quantity purchased to the export file
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
        } else //runs if the file to be exported already exists
            System.out.println("A file with that name already exists");
    }

    /**
     * Prints the buyer's dashboard
     *
     * @param scan a scanner used to implement the menu for the dashboard
     */
    //@Override
    public void printDashboard(Scanner scan, Market market) {
        ArrayList<String> storeLines = new ArrayList<String>(); //arraylist storing all the lines from stores.csv
        ArrayList<Store> stores = new ArrayList<Store>(); //arraylist containing stores created from the stores.csv
        try (BufferedReader bfr = new BufferedReader(new FileReader("Stores.csv"))) {
            String line = bfr.readLine();
            while (line != null) {
                storeLines.add(line);
                stores.add(new Store(line)); //creates a store and adds it to the stores arraylist
                line = bfr.readLine();
            }
            printStoreInfo(stores); //runs method to print the dashboard (unsorted)
            int answer = -1;
            do { //runs until buyer chooses to exit the dashboard, manages sorting
                System.out.println("1. Sort by products sold");
                System.out.println("2. Sort by purchase history");
                System.out.println("3. Exit Dashboard");

                try {
                    answer = scan.nextInt();
                    scan.nextLine();
                    if (answer < 1 || answer > 3) //throws exception if the answer is an invalid number
                        throw new NumberFormatException();
                    if (answer == 1) { //runs if sorting based off total sales
                        stores.sort((s1, s2) -> Integer.compare(s1.getSales(), s2.getSales()));
                        Collections.reverse(stores);
                        printStoreInfo(stores);
                    }
                    if (answer == 2) { //runs if sorting based of purchase history
                        ArrayList<Store> storesByHistory = new ArrayList<Store>(); //arraylist containing copies of stores to be sorted
                        for (int i = 0; i < stores.size(); i++) { //copies elements from the stores arraylist into storesByHistory
                            Store store = new Store(stores.get(i).getIndex(), stores.get(i).getName(),
                                    stores.get(i).getSellerName(), 0, 0.0, stores.get(i).getProductIndices(),
                                    "<0:0>");
                            storesByHistory.add(i, store);
                        }
                        String history;
                        ArrayList<String> purchaseHistory = new ArrayList<String>(); //arraylist storing purchase history
                        BufferedReader customerReader = new BufferedReader(new FileReader("Customers.csv"));
                        String customerLine = customerReader.readLine();
                        while (customerLine != null) {
                            if (Integer.parseInt(customerLine.split(",")[0]) == this.getIndex()) {
                                if (customerLine.split(",")[4].equals("<>")) { //runs if the purchase history is empty
                                    System.out.println("No history to compare to");
                                    break;
                                } else { //runs if the purchase history exists
                                    history = customerLine.split(",")[4].substring(1,
                                            customerLine.split(",")[4].length() - 1);
                                    if (history.length() > 3) //runs if the purchase history contains more than one product
                                        Collections.addAll(purchaseHistory, history.split("/"));
                                    else //runs if there is only one product in the purchase history
                                        purchaseHistory.add(history);
                                }
                            }
                            customerLine = customerReader.readLine();
                        }
                        ArrayList<Product> productList = new ArrayList<Product>(); //an arraylist containing the products for a store
                        for (String productInfo : purchaseHistory) {
                            BufferedReader productReader = new BufferedReader(new FileReader("Products.csv"));
                            String productLine = productReader.readLine();
                            while (productLine != null) {
                                if (productInfo.substring(0, 1).equals(productLine.split(",")[0])) {
                                    Product product = new Product(productLine);
                                    product.setQuantity(Integer.parseInt(productInfo.substring(2)));
                                    productList.add(product);
                                }
                                productLine = productReader.readLine();
                            }
                        }

                        for (int i = 0; i < storesByHistory.size(); i++) { //updates the stores in storeByHistory with sales for one customer
                            for (int j = 0; j < productList.size(); j++) {
                                if (storesByHistory.get(i).getName().equals(productList.get(j).getStoreName())) {
                                    storesByHistory.get(i).setSales(storesByHistory.get(i).getSales()
                                            + Integer.parseInt(purchaseHistory.get(j).substring(2)));
                                }
                            }
                        }
                        storesByHistory.sort((s1, s2) -> Integer.compare(s1.getSales(), s2.getSales()));
                        Collections.reverse(storesByHistory);
                        printStoreInfo(storesByHistory);
                    }
                } catch (NumberFormatException e) { //catches exception for incorrect answer values
                    System.out.println("Enter a valid integer");
                } catch (InputMismatchException e) { //catches exception for non-integer answer values
                    System.out.println("Enter an integer");
                    scan.nextLine();
                }
            } while (answer != 3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Helper method for printDashboard
     * used to print the store and product information
     *
     * @param stores a sorted arraylist that will be printed in the dashboard format
     */
    private void printStoreInfo(ArrayList<Store> stores) throws IOException {
        for (Store store : stores) { //runs through all the stores in the arraylist
            ArrayList<Product> storeProducts = new ArrayList<Product>(); //arraylist storing the products in a store
            System.out.println("Store: " + store.getName()); //prints the store name
            ArrayList<Integer> productIndices = store.getProductsByIndex();
            for (int index : productIndices) { //runs through all the indices of the products
                BufferedReader productReader = new BufferedReader(new FileReader("Products.csv"));
                String productLine = productReader.readLine();
                while(productLine != null) {
                    if (Integer.parseInt(productLine.split(",")[0]) == index) {
                        storeProducts.add(new Product(productLine)); //stores the products for a store as a new product
                    }
                    productLine = productReader.readLine();
                }
            }
            for (Product product : storeProducts) { //prints all the products for the current store
                System.out.println("--" + product.getName() + ": " + product.getQuantity());
            }
        }
    }

    /**
     * Returns the corresponding customer string from the customers.csv
     */
    public String toString() {
        String customerString = "No existing customer"; //default return string for if the customer doesn't exist
        ArrayList<String> customerInfo = new ArrayList<String>();
        try (BufferedReader bfr = new BufferedReader(new FileReader("Customers.csv"))) {
            String line = bfr.readLine();
            while (line != null) {
                customerInfo.add(line);
                line = bfr.readLine();
            }
            for (int i = 0; i < customerInfo.size(); i++) {
                String[] lineArray = customerInfo.get(i).split(",");
                if (Integer.parseInt(lineArray[0]) == this.getIndex()) { //runs if the customer index matches that of the line
                    customerString = customerInfo.get(i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customerString;
    }
}
