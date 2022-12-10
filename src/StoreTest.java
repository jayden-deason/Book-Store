import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Store Test
 * <p>
 * A main method to test store functionality
 *
 * @author Griffin Chittenden
 * @version 11-09-2022
 */
public class StoreTest {
    /**
     * Use to test basic store functions
     */
    public static void main(String[] args) {
        ArrayList<String> storeLines = new ArrayList<>();
        ArrayList<Store> stores = new ArrayList<>();
        try {
            File file = new File("Stores.csv");
            BufferedReader bfr = new BufferedReader(new FileReader(file));
            for (String line = bfr.readLine(); line != null; line = bfr.readLine()) {
                String[] splitLine = line.split(",");
                storeLines.add(line);
                stores.add(new Store(Integer.parseInt(splitLine[0]), splitLine[1], splitLine[2],
                        Integer.parseInt(splitLine[3]), Double.parseDouble(splitLine[4]), splitLine[5], splitLine[6]));
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Store storeOne = stores.get(0);
        // Sample Buyer object to use for testing. Uses the first line of Buyers.csv
        Buyer buyer = new Buyer("0,joe@yahoo.com,joeshmo123,<0:5/1:2>,<2:4>");
        // Sample Product object to use for testing. USes the first line of Products.csv
        Product product = new Product("2,Green Book,Charlie's Books,A Green Book,20,11.99");
        // Same as the product object above, but the name is changed. Used to test modifyProduct
        Product newProduct = new Product("2,Forest-green Book,Charlie's Books,A Green Book,20,11.99");
        System.out.println("TESTING THE INSTANTIATION OF STORES FROM A FILE");
        for (Store store : stores) {
            System.out.println(store);
            System.out.println("Products by Index: " + store.getProductsByIndex());
//            System.out.println("Products by Sales: " + store.productsBySalesToString());
        }


        System.out.println("TESTING REMOVAL OF A PRODUCT");
        storeOne.removeProduct(product);
        System.out.println(storeOne.getProducts());
        System.out.println("Products by Index: " + storeOne.productsByIndexToString());
//        System.out.println("Products by Sales: " + storeOne.productsBySalesToString());

        System.out.println("TESTING ADDING A PRODUCT");
        storeOne.addProduct(product, null);
        System.out.println(storeOne.getProducts());
        System.out.println("Products by Index: " + storeOne.productsByIndexToString());
//        System.out.println("Products by Sales: " + storeOne.productsBySalesToString());

        System.out.println("TESTING MODIFYING A PRODUCT");
        storeOne.modifyProduct(newProduct);
        System.out.println(storeOne.getProducts());
        System.out.println("Products by Index: " + storeOne.productsByIndexToString());
//        System.out.println("Products by Sales: " + storeOne.productsBySalesToString());

        System.out.println("MODIFYING THE SAME PRODUCT TO RESET THE PRODUCTS.CSV FILE");
        storeOne.modifyProduct(product);
        System.out.println(storeOne.getProducts());
        System.out.println("Products by Index: " + storeOne.productsByIndexToString());

    }
}
