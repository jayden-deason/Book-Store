import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

/**
 * Buyer
 * An object representing a buyer user in the marketplace
 *
 * @author Jayden Deason - lab sec 001
 * @version November 13, 2022
 */
public class Buyer extends User {
    private int index;
    private ArrayList<String> shoppingCart;
    private ArrayList<String> purchaseHistory;

    public Buyer(String username, String password) throws BadNamingException {
        super(username, password);
        if (username.contains(",") || username.contains("/")) {
            throw new BadNamingException("Please do not have a comma in your username!");
        }

        if (password.contains(",") || password.contains("/")) {
            throw new BadNamingException("Please do not have a comma in your password!");
        }

        this.shoppingCart = new ArrayList<>();
        this.purchaseHistory = new ArrayList<>();
    }

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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ArrayList<String> getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ArrayList<String> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public ArrayList<String> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void setPurchaseHistory(ArrayList<String> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    public void addProductToCart(int productIndex, int quantity) {
        shoppingCart.add(productIndex + ":" + quantity);
    }

    public void editProductQuantity(int productIndex, int newQuantity) {
        for (int i = 0; i < shoppingCart.size(); i++) {
            if (Integer.parseInt(shoppingCart.get(i).split(":")[0]) == productIndex) {
                shoppingCart.set(i, productIndex + ":" + newQuantity);
            }
        }

    }

    public void makePurchase() {
        purchaseHistory.addAll(shoppingCart);
        shoppingCart.clear();
    }

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


    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s",
                getIndex(), getEmail(), getPassword(),
                getListAsString(shoppingCart), getListAsString(purchaseHistory));
    }

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

    //    /**
//     * Prints the buyer's dashboard
//     *
//     * @param scan a scanner used to implement the menu for the dashboard
//     */
//    //@Override
//    public void printDashboard(Scanner scan) {
//        ArrayList<String> storeLines = new ArrayList<String>(); //arraylist storing all the lines from stores.csv
//        ArrayList<Store> stores = new ArrayList<Store>(); //arraylist containing stores created from the stores.csv
//        try (BufferedReader bfr = new BufferedReader(new FileReader("Stores.csv"))) {
//            String line = bfr.readLine();
//            while (line != null) {
//                storeLines.add(line);
//                stores.add(new Store(line)); //creates a store and adds it to the stores arraylist
//                line = bfr.readLine();
//            }
//            printStoreInfo(stores); //runs method to print the dashboard (unsorted)
//            int answer = -1;
//            do { //runs until buyer chooses to exit the dashboard, manages sorting
//                System.out.println("1. Sort by products sold");
//                System.out.println("2. Sort by purchase history");
//                System.out.println("3. Exit Dashboard");
//
//                try {
//                    answer = scan.nextInt();
//                    scan.nextLine();
//                    if (answer < 1 || answer > 3) //throws exception if the answer is an invalid number
//                        throw new NumberFormatException();
//                    if (answer == 1) { //runs if sorting based off total sales
//                        stores.sort((s1, s2) -> Integer.compare(s1.getSales(), s2.getSales()));
//                        Collections.reverse(stores);
//                        printStoreInfo(stores);
//                    }
//                    if (answer == 2) { //runs if sorting based of purchase history
//                        ArrayList<Store> storesByHistory = new ArrayList<Store>(); //arraylist containing copies of stores to be sorted
//                        for (int i = 0; i < stores.size(); i++) { //copies elements from the stores arraylist into storesByHistory
//                            Store store = new Store(stores.get(i).getIndex(), stores.get(i).getName(),
//                                    stores.get(i).getSellerName(), 0, 0.0, stores.get(i).getProductIndices(),
//                                    "<0:0>");
//                            storesByHistory.add(i, store);
//                        }
//                        String history;
//                        ArrayList<String> purchaseHistory = new ArrayList<String>(); //arraylist storing purchase history
//                        BufferedReader customerReader = new BufferedReader(new FileReader("Customers.csv"));
//                        String customerLine = customerReader.readLine();
//                        while (customerLine != null) {
//                            if (Integer.parseInt(customerLine.split(",")[0]) == this.getIndex()) {
//                                if (customerLine.split(",")[4].equals("<>")) { //runs if the purchase history is empty
//                                    System.out.println("No history to compare to");
//                                    break;
//                                } else { //runs if the purchase history exists
//                                    history = customerLine.split(",")[4].substring(1,
//                                            customerLine.split(",")[4].length() - 1);
//                                    if (history.length() > 3) //runs if the purchase history contains more than one product
//                                        Collections.addAll(purchaseHistory, history.split("/"));
//                                    else //runs if there is only one product in the purchase history
//                                        purchaseHistory.add(history);
//                                }
//                            }
//                            customerLine = customerReader.readLine();
//                        }
//                        ArrayList<Product> productList = new ArrayList<Product>(); //an arraylist containing the products for a store
//                        for (String productInfo : purchaseHistory) {
//                            BufferedReader productReader = new BufferedReader(new FileReader("Products.csv"));
//                            String productLine = productReader.readLine();
//                            while (productLine != null) {
//                                if (productInfo.substring(0, 1).equals(productLine.split(",")[0])) {
//                                    Product product = new Product(productLine);
//                                    product.setQuantity(Integer.parseInt(productInfo.substring(2)));
//                                    productList.add(product);
//                                }
//                                productLine = productReader.readLine();
//                            }
//                        }
//
//                        for (int i = 0; i < storesByHistory.size(); i++) { //updates the stores in storeByHistory with sales for one customer
//                            for (int j = 0; j < productList.size(); j++) {
//                                if (storesByHistory.get(i).getName().equals(productList.get(j).getStoreName())) {
//                                    storesByHistory.get(i).setSales(storesByHistory.get(i).getSales()
//                                            + Integer.parseInt(purchaseHistory.get(j).substring(2)));
//                                }
//                            }
//                        }
//                        storesByHistory.sort((s1, s2) -> Integer.compare(s1.getSales(), s2.getSales()));
//                        Collections.reverse(storesByHistory);
//                        printStoreInfo(storesByHistory);
//                    }
//                } catch (NumberFormatException e) { //catches exception for incorrect answer values
//                    System.out.println("Enter a valid integer");
//                } catch (InputMismatchException e) { //catches exception for non-integer answer values
//                    System.out.println("Enter an integer");
//                    scan.nextLine();
//                }
//            } while (answer != 3);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    /**
//     * Helper method for printDashboard
//     * used to print the store and product information
//     *
//     * @param stores a sorted arraylist that will be printed in the dashboard format
//     */
//    private void printStoreInfo(ArrayList<Store> stores) throws IOException {
//        for (Store store : stores) { //runs through all the stores in the arraylist
//            ArrayList<Product> storeProducts = new ArrayList<Product>(); //arraylist storing the products in a store
//            System.out.println("Store: " + store.getName()); //prints the store name
//            ArrayList<Integer> productIndices = store.getProductsByIndex();
//            for (int index : productIndices) { //runs through all the indices of the products
//                BufferedReader productReader = new BufferedReader(new FileReader("Products.csv"));
//                String productLine = productReader.readLine();
//                while(productLine != null) {
//                    if (Integer.parseInt(productLine.split(",")[0]) == index) {
//                        storeProducts.add(new Product(productLine)); //stores the products for a store as a new product
//                    }
//                    productLine = productReader.readLine();
//                }
//            }
//            for (Product product : storeProducts) { //prints all the products for the current store
//                System.out.println("--" + product.getName() + ": " + product.getQuantity());
//            }
//        }
//    }
}
