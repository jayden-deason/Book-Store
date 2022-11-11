import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Buyer
 *
 * An object representing a buyer user in the marketplace
 *
 * @author
 * @version
 */
public class Buyer extends User {
    int index;
    File customers = new File("customers.csv");
    /**
     * Creates a new src.Buyer with a given username & password, and an empty shopping cart
     * @param username the buyer's username
     * @param password the buyer's password
     */
    public Buyer(String username, String password) {
        super(username, password);
        int index = 0;
        if (customers.exists()) {
            try (BufferedReader bfr = new BufferedReader(new FileReader("customers.csv"))) {
                String line = bfr.readLine();
                while (line != null) {
                    index = Integer.parseInt(line.split(",")[0]) + 1;
                    line = bfr.readLine();
                }
                this.index = index;
                FileOutputStream fos = new FileOutputStream("customers.csv", true);
                PrintWriter pw = new PrintWriter(fos);
                pw.printf("%d,%s,%s,<>\n", index, this.getUsername(), this.getPassword());

                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileOutputStream fos = new FileOutputStream("customers.csv", true);
                PrintWriter pw = new PrintWriter(fos);
                pw.printf("0,%s,%s,<>\n", this.getUsername(), this.getPassword());

                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get the buyer's shopping cart contents
     * @return the list of products in the buyer's shopping cart in the form index:quantity
     */
    public ArrayList<String> getShoppingCart() {
        ArrayList<String> cartItems = new ArrayList<String>();
        String cart = "";
        try (BufferedReader bfr = new BufferedReader(new FileReader("customers.csv"))) {
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
     * @param product the product to be added
     * @param quantity the number of products to be added
     */
    public void addProductToCart(Product product, int quantity) {
        ArrayList<String> customerInfo = new ArrayList<String>();
        try (BufferedReader bfr = new BufferedReader(new FileReader("customers.csv"))) {
            String line = bfr.readLine();
            while (line != null) {
                customerInfo.add(line);
                line = bfr.readLine();
            }
            FileOutputStream fos = new FileOutputStream("customers.csv", false);
            PrintWriter pw = new PrintWriter(fos);
            for (int i = 0; i < customerInfo.size(); i++) {
                String[] lineArray = customerInfo.get(i).split(",");
                if (Integer.parseInt(lineArray[0]) == this.getIndex()) {
                    if (lineArray[3].length() > 2) {
                        pw.printf("%s,%s,%s,%s\n", lineArray[0], lineArray[1], lineArray[2],
                                String.format("%s/%d:%d>", lineArray[3].substring(0, lineArray[3].length() - 1),
                                        product.getIndex(), quantity));
                    } else {
                        pw.printf("%s,%s,%s,%s\n", lineArray[0], lineArray[1], lineArray[2],
                                String.format("%s%d:%d>", lineArray[3].substring(0, lineArray[3].length() - 1),
                                        product.getIndex(), quantity));
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

    public int getIndex() {
        return index;
    }

    /**
     * Export the buyer info to a file
     * @param filename path to export file
     */
    @Override
    public void exportToFile(String filename) {
        // TODO: implement this, however we are saving the data to a file
    }

    /**
     * Prints the buyer's dashboard
     */
    @Override
    public void printDashboard() {
        // TODO: determine a format for a buyer's dashboard
    }
}
