import java.util.Scanner;
import java.util.ArrayList;

/**
 * Main
 *
 * The main class used to launch a dashboard for a user to log in and perform actions in the marketplace
 *
 * @author Megan Long, 001
 *
 * @version 11/12/2022
 */
public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
//        System.out.println("What is the buyer file?");
//        String buyerFile = scan.nextLine();
//        System.out.println("What is the seller file?");
//        String sellerFile = scan.nextLine();
//        System.out.println("What is the store file?");
//        String storesFile = scan.nextLine();
//        System.out.println("What is the product file?");
//        String productsFile = scan.nextLine();
//        Market market = new Market(buyerFile, sellerFile, storesFile, productsFile);

        Market market = new Market("Customers.csv", "Sellers.csv", "Stores.csv", "Products.csv");
        while (true) {
            System.out.println("Are you a buyer or a seller? (buyer/seller)");
            String answer = scan.nextLine();
            answer = answer.toLowerCase();
            if (answer.equals("buyer")) {
                signUp("buyer", market, scan);
                break;
            } else if (answer.equals("seller")) {
                signUp("seller", market, scan);
                break;
            } else {
                // if answer is not "buyer" or "seller"
                System.out.println("Invalid input.");
            }
        }
    }
    public static void signUp(String action, Market market, Scanner scan) {
        while (true) {
            System.out.println("Do you have an account? (y/n)");
            String answer = scan.nextLine();
            answer = answer.toLowerCase();
            if (answer.equals("yes") || answer.equals("y")) {
                while (true) {
                    String username = "";
                    while (true) {
                        System.out.println("Enter your email.");
                        username = scan.nextLine();
                        if (username.contains("@") && username.contains(".")) {
                            break;
                        } else {
                            System.out.println("Please enter a valid email.");
                        }
                    }
                    if (action.equals("seller")) {
                        Seller seller = market.getSellerByEmail(username);
                        if (seller == null) {
                            System.out.println("Account does not exist.");
                        } else {
                            System.out.println("What is your password?");
                            String password = scan.nextLine();
                            if (seller.getPassword().equals(password)) {
                                sell(seller, market, scan);
                                break;
                            } else {
                                System.out.println("Password does not match.");
                            }
                        }
                    } else {
                        Buyer buyer = market.getBuyerByEmail(username);
                        if (buyer == null) {
                            System.out.println("Account does not exist.");
                        } else {
                            System.out.println("What is your password?");
                            String password = scan.nextLine();
                            if (buyer.getPassword().equals(password)) {
                                buy(buyer, market, scan);
                                break;
                            } else {
                                System.out.println("Password does not match.");
                            }
                        }
                    }
                }
                break;
            } else if (answer.equals("no") || answer.equals("n")) {
                try {
                    String username = "";
                    while (true) {
                        System.out.println("Enter your username.");
                        username = scan.nextLine();
                        if (username.contains("@") && username.contains(".")) {
                            break;
                        } else {
                            System.out.println("Please enter a valid email.");
                        }
                    }
                    System.out.println("Enter your password.");
                    String password = scan.nextLine();
                    if (action.equals("seller")) {
                        Seller seller = new Seller(username, password);
                        market.createUser(seller);
                        sell(seller, market, scan);
                    } else {
                        Buyer buyer = new Buyer(username, password);
                        market.createUser(buyer);
                        buy(buyer, market, scan);
                    }
                    break;
                } catch (badNamingException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                // if answer is not "yes" or "no"
                System.out.println("Invalid input.");
            }
        }
    }
    public static void sell(Seller seller, Market market, Scanner scan) {
        while (true) {
            System.out.println("Enter a number:");
            System.out.println("1 - Create a store.");
            System.out.println("2 - Add, edit, or remove books to a store.");
            System.out.println("3 - View sales.");
            System.out.println("4 - View dashboard.");
            System.out.println("5 - Export books to .csv.");
            System.out.println("6 - View books in Customer Carts.");
            System.out.println("7 - Exit.");
            String answer = scan.nextLine();
            if (answer.equals("1")) {
                System.out.println("What is the store name?");
                String storeName = scan.nextLine();
                seller.addStore(storeName);
            } else if (answer.equals("2")) {
                System.out.println("What is the store name?");
                String storeName = scan.nextLine();
                Store store = null;
                for (int i = 0; i < seller.getStores().size(); i++) {
                    if (seller.getStores().get(i).getName().equals(storeName)) {
                        store = seller.getStores().get(i);
                    }
                }
                if (store == null) {
                    System.out.println("Store does not exist!");
                } else {
                    System.out.println("What is the book name?");
                    String name = scan.nextLine();
                    System.out.println("What is the book's store name?");
                    String productStoreName = scan.nextLine();
                    System.out.println("What is the book's description?");
                    String description = scan.nextLine();
                    int quantity = -1;
                    double price = -1.0;
                    int index = -1;
                    boolean num;
                    while (true) {
                        try {
                            num = true;
                            System.out.println("What is the quantity?");
                            quantity = Integer.parseInt(scan.nextLine());
                            System.out.println("What is the price?");
                            price = Double.parseDouble(scan.nextLine());
                            System.out.println("What is the index of the book being added/edited/removed?");
                            index = Integer.parseInt(scan.nextLine());
                            if (quantity < 0 || price < 0.0 || index < 0) {
                                num = false;
                            }
                        } catch (NumberFormatException e) {
                            num = false;
                        }
                        if (num) {
                            break;
                        } else {
                            System.out.println("Please enter valid numbers!");
                        }
                    }
                    Product product = new Product(name, productStoreName, description, quantity, price, index);
                    while (true) {
                        System.out.println("Enter a number:");
                        System.out.println("1 - Add Book.");
                        System.out.println("2 - Delete Book.");
                        System.out.println("3 - Edit Book.");
                        System.out.println("4 - Import books.");
                        String answerTwo = scan.nextLine();
                        if (answerTwo.equals("1")) {
                            store.addProduct(product);
                        } else if (answerTwo.equals("2")) {
                            store.removeProduct(product);
                        } else if (answerTwo.equals("3")) {
                            store.modifyProduct(product);
                        } else if (answerTwo.equals("4")) {
                            System.out.println("What is the file name to import?");
                            String fileName = scan.nextLine();
                            store.importProducts(fileName);
                        } else {
                            // if answer is not 1, 2, 3, or 4
                            System.out.println("Invalid input.");
                        }
                    }
                }
            } else if (answer.equals("3")) {
                ArrayList<Store> stores = seller.getStores();
                for (int i = 0; i < stores.size(); i++) {
                    System.out.println(stores.get(i).getSales());
                }
            } else if (answer.equals("4")) {
                while (true) {
                    System.out.println("Enter a number:");
                    System.out.println("1 - No sorting.");
                    System.out.println("2 - Sort alphabetically.");
                    System.out.println("3 - Sort by quantity.");
                    String answerTwo = scan.nextLine();
                    if (answerTwo.equals("1")) {
                        seller.printDashboard(0);
                        break;
                    } else if (answerTwo.equals("2")) {
                        seller.printDashboard(1);
                        break;
                    } else if (answerTwo.equals("3")) {
                        seller.printDashboard(2);
                        break;
                    } else {
                        // if answer is not 1, 2, or 3
                        System.out.println("Invalid input.");
                    }
                }
            } else if (answer.equals("5")) {
                System.out.println("What is the file name to export to?");
                String fileName = scan.nextLine();
                System.out.println("What is the store name?");
                String store = scan.nextLine();
                seller.exportProducts(fileName, store);
                System.out.println("Export complete!");
            } else if (answer.equals("6")) {
                seller.viewProductsInCart(market.getBuyers(), market.getProducts());
            } else if (answer.equals("7")) {
                System.out.println("Have a nice day!");
                break;
            } else {
                System.out.println("Invalid input.");
            }
        }
    }
    public static void buy(Buyer buyer, Market market, Scanner scan) {
        while (true) {
            System.out.println("Enter a number:");
            System.out.println("1 - View marketplace.");
            System.out.println("2 - View dashboard.");
            System.out.println("3 - Find book.");
            System.out.println("4 - Export shopping history.");
            System.out.println("5 - Checkout shopping cart.");
            System.out.println("6 - Edit/remove shopping cart.");
            System.out.println("7 - Exit.");
            String answer = scan.nextLine();
            if (answer.equals("1")) {
                ArrayList<Product> products = market.getAllProducts(true);
                System.out.println("-----");
                for (int i = 0; i < products.size(); i++) {
                    System.out.println(productString(products.get(i)));
                }
                System.out.println("-----");

            } else if (answer.equals("2")) {
                buyer.printDashboard(scan);
            } else if (answer.equals("3")) {
                System.out.println("Which store would you like to search?");
                String storeName = scan.nextLine();
                Store store = market.getStoreByName(storeName);
                if (store == null) {
                    System.out.println("Store does not exist.");
                } else {
                    System.out.println("What is the book?");
                    String productName = scan.nextLine();
                    Product product = null;
                    ArrayList<Product> products = store.getProducts();
                    for (int i = 0; i < products.size(); i++) {
                        if (productName.equals(products.get(i).getName())) {
                            product = products.get(i);
                        }
                    }
                    if (product == null) {
                        System.out.println("Book does not exist.");
                    } else {
                        System.out.println(productString(product));
                        System.out.println("Would you like to add to cart? (y/n)");
                        String answerTwo = scan.nextLine();
                        answerTwo = answerTwo.toLowerCase();
                        if (answerTwo.equals("yes") || answerTwo.equals("y")) {
                            boolean num;
                            int quantity = -1;
                            while (true) {
                                num = true;
                                System.out.println("What is the quantity?");
                                try {
                                    quantity = Integer.parseInt(scan.nextLine());
                                } catch (NumberFormatException e) {
                                    num = false;
                                }
                                if (quantity < 1) {
                                    num = false;
                                }
                                if (num) {
                                    break;
                                } else {
                                    System.out.println("Invalid quantity.");
                                }
                            }
                            buyer.addProductToCart(product.getIndex(), quantity);
                            System.out.println("Added to cart!");
                        }
                    }
                }
            } else if (answer.equals("4")) {
                System.out.println("What is the file name to export to?");
                String fileName = scan.nextLine();
                buyer.exportToFile(fileName);
            } else if (answer.equals("5")) {
                //TODO: checkout
                buyer.makePurchase();
                System.out.println("Checked out!");
            } else if(answer.equals("6")) {
                boolean num;
                int index = -1;
                int quantity = -1;
                while (true) {
                    num = true;
                    System.out.println("What is the index of the book being edited");
                    try {
                        index = Integer.parseInt(scan.nextLine());
                    } catch (NumberFormatException e) {
                        num = false;
                    }
                    if (index < 0) {
                        num = false;
                    }
                    if (num) {
                        ArrayList<String> products = buyer.getShoppingCart();
                        boolean exists = false;
                        for (int i = 0; i < products.size(); i++) {
                            if (index == Integer.parseInt(products.get(i).split(",")[0])) {
                                exists = true;
                                while (true) {
                                    num = true;
                                    System.out.println("What is the new quantity? (0 for delete)");
                                    try {
                                        quantity = Integer.parseInt(scan.nextLine());
                                    } catch (NumberFormatException e) {
                                        num = false;
                                    }
                                    if (quantity < 1) {
                                        num = false;
                                    }
                                    if (num) {
                                        break;
                                    } else {
                                        System.out.println("Invalid quantity.");
                                    }
                                }
                                buyer.editProductQuantity(index, quantity);
                            }
                        }
                        if (!(exists)) {
                            System.out.println("Book did not exist within cart!");
                        }
                    } else {
                        System.out.println("Invalid index.");
                    }
                    break;
                }
            } else if (answer.equals("7")) {
                System.out.println("Have a nice day!");
                break;
            } else {
                System.out.println("Invalid input.");
            }
        }
    }

    private static String productString(Product p) {
        return String.format("%d) Name: %s | $%.2f | Quantity: %d | Store %s",
                p.getIndex(), p.getName(), p.getPrice(), p.getQuantity(), p.getStoreName());
    }
}
