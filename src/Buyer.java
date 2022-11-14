import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

/**
 * Buyer
 * An object representing a buyer user in the marketplace
 *
 * @author Jayden Deason - lab sec 001
 * @version November 14, 2022
 */
public class Buyer extends User {
    private int index; // index of user in csv
    private ArrayList<String> shoppingCart; // list of strings describing shopping cart contents
    private ArrayList<String> purchaseHistory; // list of strings describing purchase history

    /**
     * Make a new buyer with a given email and password
     *
     * @param email    buyer's email
     * @param password buyer's password
     * @throws BadNamingException if email or password contain invalid characters
     */
    public Buyer(String email, String password) throws BadNamingException {
        super(email, password);
        if (email.contains(",") || email.contains("/")) {
            throw new BadNamingException("Please do not have a comma in your email!");
        }

        if (password.contains(",") || password.contains("/")) {
            throw new BadNamingException("Please do not have a comma in your password!");
        }

        this.shoppingCart = new ArrayList<>();
        this.purchaseHistory = new ArrayList<>();
    }

    /**
     * Create a new buyer from a line in a csv, from the toString format
     *
     * @param buyerLine line describing a buyer
     */
    public Buyer(String buyerLine) {
        String[] info = buyerLine.split(",");
        this.setIndex(Integer.parseInt(info[0]));
        this.setEmail(info[1]);
        this.setPassword(info[2]);
        this.shoppingCart = new ArrayList<>();
        String cartString = info[3].substring(1, info[3].length() - 1); // remove <>
        if (cartString.length() != 0) {
            shoppingCart.addAll(List.of(cartString.split("/")));
        }

        this.purchaseHistory = new ArrayList<>();
        String histString = info[4].substring(1, info[4].length() - 1); // remove <>
        if (histString.length() != 0) {
            purchaseHistory.addAll(List.of(histString.split("/")));
        }
    }

    /**
     * Get the user's index in the csv
     *
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     * Update the user's csv index
     *
     * @param index new index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Get the user's shopping cart contents in the format [index:quantity, index_quantity]
     *
     * @return shopping cart
     */
    public ArrayList<String> getShoppingCart() {
        return shoppingCart;
    }

    /**
     * Set the user's shopping cart list
     *
     * @param shoppingCart a shopping cart in the format [index:quantity, index_quantity]
     */
    public void setShoppingCart(ArrayList<String> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    /**
     * Get the user's purchase history in the format [index:quantity, index_quantity]
     *
     * @return purchase history
     */
    public ArrayList<String> getPurchaseHistory() {
        return purchaseHistory;
    }

    /**
     * Set the user's purchase history list
     *
     * @param purchaseHistory purchase history in the format [index:quantity, index_quantity]
     */
    public void setPurchaseHistory(ArrayList<String> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    /**
     * Add a product to the user's cart
     *
     * @param productIndex index of the product in the csv file
     * @param quantity     quantity to add to cart
     */
    public void addProductToCart(int productIndex, int quantity) {
        shoppingCart.add(productIndex + ":" + quantity);
    }

    /**
     * Edit an existing product's quantity in the cart
     *
     * @param productIndex index of the product in the csv
     * @param newQuantity  new quantity in the shopping cart
     */
    public void editProductQuantity(int productIndex, int newQuantity) {
        for (int i = 0; i < shoppingCart.size(); i++) {
            if (Integer.parseInt(shoppingCart.get(i).split(":")[0]) == productIndex) {
                shoppingCart.set(i, productIndex + ":" + newQuantity);
            }
        }

    }

    /**
     * Clear the shopping cart and move all contents to purchase history
     */
    public void makePurchase() {
        purchaseHistory.addAll(shoppingCart);
        shoppingCart.clear();
    }

    /**
     * Helper method to get a list as a string in the format <item/item/item>
     *
     * @param list an arraylist of strings
     * @return the list represented as one string
     */
    private String getListAsString(ArrayList<String> list) {
        String out = "<";
        for (int i = 0; i < list.size(); i++) {
            out += list.get(i);
            if (i != list.size() - 1) {
                out += "/";
            }
        }

        return out + ">";
    }


    /**
     * Get a string representation of the buyer
     *
     * @return a description of the buyer to use in a csv file
     */
    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s",
                getIndex(), getEmail(), getPassword(),
                getListAsString(shoppingCart), getListAsString(purchaseHistory));
    }

    /**
     * Export buyer history to a file
     *
     * @param filename path to the file
     * @param market   the market that the buyer is in
     */
    public void exportToFile(String filename, Market market) {
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(filename, false));

            for (String item : purchaseHistory) {
                int idx = Integer.parseInt(item.split(":")[0]);
                int quantity = Integer.parseInt(item.split(":")[1]);
                Product p = market.getProductByIndex(idx);

                pw.printf("Name: %s | Store: %s | Quantity: %d | Price: $%.2f\n",
                        p.getName(), p.getStoreName(), quantity, p.getPrice() * quantity);
            }
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints the buyer's dashboard
     *
     * @param scan a scanner used to implement the menu for the dashboard
     */
    //@Override
    public void printDashboard(Scanner scan, Market market) {

        ArrayList<Store> stores = market.getStores();
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
                    stores.sort((s1, s2) -> Integer.compare(s1.getSales(market), s2.getSales(market)));
                    Collections.reverse(stores);
                    printStoreInfo(stores);
                }
                if (answer == 2) { //runs if sorting based of purchase history
                    //arraylist containing copies of stores to be sorted
                    ArrayList<Store> storesByHistory = new ArrayList<Store>(stores);

                    storesByHistory.sort((s1, s2) -> s1.getSales(market) - s2.getSales(market));
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

    }

    /**
     * Helper method for printDashboard
     * used to print the store and product information
     *
     * @param stores a sorted arraylist that will be printed in the dashboard format
     */
    private void printStoreInfo(ArrayList<Store> stores) {
        System.out.println("------------------------------------------");

        for (Store store : stores) { //runs through all the stores in the arraylist
            ArrayList<Product> storeProducts = store.getProducts(); //arraylist storing the products in a store
            System.out.println("Store: " + store.getName()); //prints the store name

            for (Product product : storeProducts) { //prints all the products for the current store
                System.out.println("--" + product.getName() + ": " + product.getQuantity());
            }
        }
        System.out.println("------------------------------------------");

    }

    /**
     * Prints the recently purchased products
     */
    public void printPreviousPurchases() {
        System.out.println("------------------------------------------");
        for (String item : purchaseHistory) {
            int idx = Integer.parseInt(item.split(":")[0]);
            int quantity = Integer.parseInt(item.split(":")[1]);
            Product p = Market.getInstance().getProductByIndex(idx);

            System.out.printf("Name: %s | Store: %s | Quantity: %d | Price: $%.2f\n",
                    p.getName(), p.getStoreName(), quantity, p.getPrice() * quantity);
        }
        System.out.println("------------------------------------------");

    }
}
