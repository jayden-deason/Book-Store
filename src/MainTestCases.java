import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Main Test Cases
 *
 * Testing market functionality with all the internal objects
 *
 * @author Katya Teodorovich, section 001
 * @version November 14, 2022
 */
public class MainTestCases {
    public static void main(String[] args) {
        testRequirements();
//        printAllInfo();
//        creatingSellerTest();
    }

    public static void testRequirements() {
        Market market = Market.getInstance();

        // products
        Product p = new Product("0,Blue Book,Vons,A Blue Book,30,10.99");
        System.out.println(p.getName());
        System.out.println(p.getStoreName());
        System.out.println(p.getDescription());
        System.out.println(p.getQuantity());
        System.out.println(p.getSalePrice());

        // print listings
        Main.printListings(market.getAllProducts(true));

        Product product = market.getProductByIndex(0);
        Main.printProductPage(product);


        // purchases
        Buyer b = new Buyer("1,tommy@gmail.com,bigtom69,<0:2>,<4:3/7:2/5:1>");
        market.addBuyer(b);
        market.makePurchase(b);

        Main.printProductPage(market.getProductByIndex(0));
        System.out.println("^^ Product quantity decreased by 2");

        // sellers
        Seller seller = market.getSellerByEmail("bob@gmail.com");
        System.out.println(seller.getStoreNames());

        Store store = market.getStoreByName(seller.getStoreNames().get(0));
        System.out.println(store.getProductNames());
        store.removeProduct(product);
        System.out.println(store.getProductNames());
        System.out.println("^^ Removed Blue Book");

        System.out.println("------------------------------------------");
        ArrayList<Store> stores = seller.getStores();

        if (stores.size() == 0) {
            System.out.println("No stores!");
        }
        for (Store s : seller.getStores()) {
            System.out.println(s.getName() + " -- " + s.getSales(market));
        }
        System.out.println("------------------------------------------");


    }


    public static void printAllInfo() {
        Market market = Market.getInstance();

        for (Buyer buyer : market.getBuyers()) {
            System.out.println(buyer.getEmail());
            Main.printShoppingCart(buyer, market);
        }

        for (Product product : market.getProducts()) {
            Main.printProductPage(product);
        }

        System.out.println("---------------------------------");
        for (Store store : market.getStores()) {
            System.out.println(store.getName());
            System.out.println(store.getProductNames());
            System.out.println(store.getSellerName());
            System.out.println(store.getSales(market));
            System.out.println("---------------------------------");
        }
    }

    public static void creatingSellerTest() {
        Market market = Market.getInstance();
        printFileContents("Sellers.csv");

        Seller seller = null;
        try {
            seller = new Seller("test@gmail.com", "password");
        } catch (BadNamingException e) {
            e.printStackTrace();
        }

        market.addSeller(seller);
        market.updateAllFiles();
        printFileContents("Sellers.csv");
        printFileContents("Stores.csv");


        Store store = new Store(-1, "test store", "test@gmail.com");
        market.addStore(store);
        market.updateAllFiles();

        printFileContents("Stores.csv");

        Product p = new Product("test product", "test store",
                "blah blah blah", 100, 10, 10, -1);
        market.addProduct(p);
        market.updateAllFiles();

        printFileContents("Stores.csv");

    }

    /**
     * Helper method to print contents of file
     * @param fileName path to file
     */
    public static void printFileContents(String fileName) {
        System.out.println("***************************");
        try {
            // reading file contents
            BufferedReader br = new BufferedReader(new FileReader(fileName));

            String line = br.readLine();
            while (line != null) {
                System.out.println(line);
                line = br.readLine();
            }

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("***************************");

    }
}