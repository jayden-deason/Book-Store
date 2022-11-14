import java.util.ArrayList;
import java.util.Scanner;

/**
 * BuyerTest
 *
 * The class used to test Buyer functionality
 *
 * @author Jayden Deason - lab sec 001
 * @version November 13, 2022
 */
public class BuyerTest {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
//        Market market = Market.getInstance();

        Buyer buyer = null;
        Buyer buyer1 = null;

        try {
            buyer = new Buyer("test@gmail.com", "password");
            buyer1 = new Buyer("admin@gmail.com", "secure");
        } catch (BadNamingException e) {
            e.printStackTrace();
        }

        Product product = new Product("testProd", "test", "Test", 5, 40.0, 5);
        buyer.addProductToCart(product.getIndex(), 2);

        ArrayList<String> test = buyer.getShoppingCart();
        for(int i = 0; i < test.size(); i++) {
            System.out.println(test.get(i));
        }
        buyer.addProductToCart(3, 8);
        buyer.addProductToCart(4, 2);
        buyer.editProductQuantity(product.getIndex(), 1);
        buyer.makePurchase();
        buyer.addProductToCart(product.getIndex(), 3);
        buyer.makePurchase();
//        buyer.exportToFile("purchases.csv");
//        buyer.exportToFile("purchases.csv");
        System.out.println(buyer);
        System.out.println(buyer1);
//        buyer.printDashboard(scan);
    }
}