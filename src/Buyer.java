import java.io.*;
import java.util.ArrayList;

/**
 * Buyer
 *
 * An object representing a buyer user in the marketplace
 *
 * @author
 * @version
 */
public class Buyer extends User {
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
                    index++;
                    line = bfr.readLine();
                }
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
     * @return the list of products in the buyer's shopping cart
     */
    public ArrayList<Product> getShoppingCart() {
       // TODO: implement with file i/o
        return new ArrayList<Product>();
    }

    /**
     * Add a product to the shopping cart
     * @param product the product to be added
     */
    public void addProductToCart(Product product) {
        // TODO: implement with file i/o
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
