import java.util.Scanner;
import java.util.ArrayList;

/**
 * Main
 * <p>
 * The main class used to launch a dashboard for a user to log in and perform actions in the marketplace
 *
 * @author Megan Long, 001
 * @version 11/14/2022
 */
public class Main {

    /**
     * The main method that launches the entire dashboard for users to interact with
     *
     * @param args command-line args
     */
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Market market = Market.getInstance();

//        while (true) {
        System.out.println("Welcome to the book marketplace!!");
        System.out.println("Are you a buyer or a seller? (buyer/seller)");
        String answer = scan.nextLine().toLowerCase();

        if (answer.equals("buyer")) {
            signUp("buyer", market, scan);
//            break;
        } else if (answer.equals("seller")) {
            signUp("seller", market, scan);
//            break;
        } else {
            // if answer is not "buyer" or "seller"
            System.out.println("Invalid input.");
        }
//        }
    }

    /**
     * Steps following the user signing in
     *
     * @param action either "buyer" or "seller", for specific user types
     * @param market the market object
     * @param scan   the scanner to read terminal input
     */
    public static void signUp(String action, Market market, Scanner scan) {
//        while (true) {
        System.out.println("Do you have an account? (y/n)");
        String answer = scan.nextLine().toLowerCase();

        if (answer.equals("yes") || answer.equals("y")) {
            while (true) {
                String email = "";
                while (true) {
                    System.out.println("Enter your email.");
                    email = scan.nextLine();
                    if (email.contains("@") && email.contains(".")) {
                        break;
                    } else {
                        System.out.println("Please enter a valid email.");
                    }
                }

                User user = market.getUserByEmail(email);
                if (user == null) {
                    System.out.println("Account does not exist.");
                } else {
                    System.out.println("What is your password?");
                    String password = scan.nextLine();
                    if (user.getPassword().equals(password)) {
                        if (user instanceof Seller) {
                            sell((Seller) user, market, scan);
                        } else {
                            buy((Buyer) user, market, scan);
                        }
                        break;
                    } else {
                        System.out.println("Password does not match.");
                    }
                }
            }
        } else if (answer.equals("no") || answer.equals("n")) {
            try {
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
//                    break;
            } catch (BadNamingException e) {
                System.out.println(e.getMessage());
            }
        } else {
            // if answer is not "yes" or "no"
            System.out.println("Invalid input.");
        }
//        }
    }

    /**
     * Steps to follow for sellers
     *
     * @param seller the seller object
     * @param market the market that the seller is in
     * @param scan   the scanner for terminal input
     */
    public static void sell(Seller seller, Market market, Scanner scan) {
        while (true) {
            System.out.println("Enter a number:");
            System.out.println("1 - Create a store.");
            System.out.println("2 - Add, edit, or remove books to a store.");
            System.out.println("3 - View sales.");
            System.out.println("4 - View dashboard.");
            System.out.println("5 - Export books to .csv.");
            System.out.println("6 - Import books from .csv.");
            System.out.println("7 - View books in Customer Carts.");
            System.out.println("8 - Exit.");
            String answer = scan.nextLine();
            if (answer.equals("1")) {
                System.out.println("What is the store name?");
                String storeName = scan.nextLine();
                market.addStore(storeName, seller.getEmail());
                market.updateAllFiles();
            } else if (answer.equals("2")) {
                System.out.println("What is the store name?");
                System.out.println(seller.getStoreNames());
                String storeName = scan.nextLine();
                Store store = seller.getStoreByName(storeName);

                if (store == null) {
                    System.out.println("Store does not exist!");
                } else {
                    System.out.println("What is the book name?");
                    String name = scan.nextLine();
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

                            if (market.getProductByName(name) != null) {
                                System.out.println("What is the index of the book being added/edited/removed?");
                                index = Integer.parseInt(scan.nextLine());
                            }
                            if (quantity < 0 || price < 0.0) {
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
                    Product product = new Product(name, storeName, description, quantity, price, index);
                    while (true) {
                        System.out.println("Enter a number:");
                        System.out.println("1 - Add Book.");
                        System.out.println("2 - Delete Book.");
                        System.out.println("3 - Edit Book.");
                        System.out.println("4 - Exit.");
                        String answerTwo = scan.nextLine();
                        if (answerTwo.equals("1")) {
                            market.addProduct(product);
                            market.updateAllFiles();
                        } else if (answerTwo.equals("2")) {
                            store.removeProduct(product);
                            market.updateAllFiles();
                        } else if (answerTwo.equals("3")) {
                            store.modifyProduct(product);
                            market.updateAllFiles();
                        } else if (answerTwo.equals("4")) {
                            break;
                        } else {
                            // if answer is not 1, 2, 3, or 4
                            System.out.println("Invalid input.");
                        }
                    }
                }
            } else if (answer.equals("3")) {
                System.out.println("------------------------------------------");
                ArrayList<Store> stores = seller.getStores();

                if (stores.size() == 0) {
                    System.out.println("No stores!");
                }
                for (Store store : seller.getStores()) {
                    System.out.println(store.getName() + " -- " + store.getSales(market));
                }
                System.out.println("------------------------------------------");

            } else if (answer.equals("4")) {
                while (true) {
                    System.out.println("Enter a number:");
                    System.out.println("1 - No sorting.");
                    System.out.println("2 - Sort alphabetically.");
                    System.out.println("3 - Sort by quantity.");
                    String answerTwo = scan.nextLine();
                    if (answerTwo.equals("1")) {
                        seller.printDashboard(0, market);
                        break;
                    } else if (answerTwo.equals("2")) {
                        seller.printDashboard(1, market);
                        break;
                    } else if (answerTwo.equals("3")) {
                        seller.printDashboard(2, market);
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
//                seller.exportProducts(fileName, store);
                System.out.println(market.getStoreByName(store));
                market.printToFile(market.getStoreByName(store).getProducts(), fileName);
                System.out.println("Export complete!");
            } else if (answer.equals("6")) {
                System.out.println("What is the file name to import from?");
                String fileName = scan.nextLine();
                market.addProductsFromFile(fileName);
                System.out.println("Import complete!");
            } else if (answer.equals("7")) {
                System.out.println("------------------------------------------");
                seller.viewProductsInCart(market);
                System.out.println("------------------------------------------");

            } else if (answer.equals("8")) {
                System.out.println("Have a nice day!");
                return;

            } else {
                System.out.println("Invalid input.");
            }
        }
    }

    /**
     * Steps to follow for buyers signing in
     *
     * @param buyer  the buyer object
     * @param market the market that the buyer is in
     * @param scan   the scanner used for terminal input
     */
    public static void buy(Buyer buyer, Market market, Scanner scan) {
        while (true) {
            System.out.println("Enter a number:");
            System.out.println("1 - View marketplace.");
            System.out.println("2 - View shopping cart.");
            System.out.println("3 - Browse by store.");
            System.out.println("4 - Export shopping history.");
            System.out.println("5 - Checkout shopping cart.");
            System.out.println("6 - Edit/remove shopping cart.");
            System.out.println("7 - Print dashboard.");
            System.out.println("8 - Review previous purchases.");
            System.out.println("9 - Exit.");
            String answer = scan.nextLine();
            if (answer.equals("1")) {
                printListings(market.getAllProducts(true));

                System.out.println("1. Select a product");
                System.out.println("2. Search by criteria");
                System.out.println("3. Exit");

                int choice = scan.nextInt();
                scan.nextLine();

                while (choice != 3) {

                    if (choice == 1) {
                        System.out.println("Would you like to select a product? (y/n)");
                        String input = scan.nextLine();

                        System.out.println("Enter an item's index to learn more");
                        int idx = scan.nextInt();
                        scan.nextLine();

                        Product p = market.getProductByIndex(idx);
                        printProductPage(p);

                        System.out.println("Would you like to add to cart? (y/n)");
                        String inp = scan.nextLine();

                        if (inp.equalsIgnoreCase("y") || inp.equalsIgnoreCase("yes")) {
                            System.out.println("What is the quantity?");
                            int quantity = scan.nextInt(); // TODO: add checks for valid input
                            scan.nextLine();

                            buyer.addProductToCart(idx, quantity);
                            market.updateAllFiles();
                            System.out.println("Added to cart!");
                            printShoppingCart(buyer, market);

                        }
                    } else if (choice == 2) {
                        System.out.println("Enter the product name. (leave empty if not searching by name)");
                        String name = scan.nextLine();
                        if (name.equals("")) name = null;

                        System.out.println("Enter the store name. (leave empty if not searching by store)");
                        String storeName = scan.nextLine();
                        if (storeName.equals("")) storeName = null;

                        System.out.println("Enter description terms. (leave empty if not searching by description)");
                        String description = scan.nextLine();
                        if (description.equals("")) description = null;

                        System.out.println("Search results:");
                        printListings(market.matchConditions(name, storeName, description));

                    } else {
                        break;
                    }

                    System.out.println("1. Select a product");
                    System.out.println("2. Search by criteria");
                    System.out.println("3. Exit");

                    choice = scan.nextInt();
                    scan.nextLine();
                }


            } else if (answer.equals("2")) {
                printShoppingCart(buyer, market);

            } else if (answer.equals("3")) {
                System.out.println("Which store would you like to search?");
                System.out.println(market.getStoreNames());
                String storeName = scan.nextLine();
                Store store = market.getStoreByName(storeName);
                if (store == null) {
                    System.out.println("Store does not exist.");
                } else {
                    System.out.println("What is the book?");
                    System.out.println(store.getProductNames());
                    String productName = scan.nextLine();
                    Product product = store.getProductByName(productName);

                    if (product == null) {
                        System.out.println("Book does not exist.");
                    } else {
                        printProductPage(product);
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
                            market.updateAllFiles();
                            System.out.println("Added to cart!");
                            printShoppingCart(buyer, market);
                        }
                    }
                }
            } else if (answer.equals("4")) {
                System.out.println("What is the file name to export to?");
                String fileName = scan.nextLine();
                buyer.exportToFile(fileName, market);
                System.out.println("Exported!");
            } else if (answer.equals("5")) {
                market.makePurchase(buyer);
                System.out.println("Checked out!");
            } else if (answer.equals("6")) {
                printShoppingCart(buyer, market);
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
                            if (index == Integer.parseInt(products.get(i).split(":")[0])) {
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
                                market.updateAllFiles();
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
                buyer.printDashboard(scan, market);
            } else if (answer.equals("8")) {
                buyer.printPreviousPurchases();
            } else if (answer.equals("9")) {
                System.out.println("Have a nice day!");
                return;
            } else {
                System.out.println("Invalid input.");
            }
        }

    }

    /**
     * Return a nicely formatted string to print a product
     *
     * @param p product
     * @return string representation of product
     */
    public static String productString(Product p) {
        return String.format("%d) Name: %s | Store: %s | $%.2f",
                p.getIndex(), p.getName(), p.getStoreName(), p.getPrice());
    }

    /**
     * Print the page with a product description
     *
     * @param p product
     */
    public static void printProductPage(Product p) {
        System.out.println("------------------------------------------");
        System.out.println(productString(p) + " | Quantity: " + p.getQuantity());
        System.out.println(p.getDescription());
        System.out.println("------------------------------------------");
    }

    /**
     * Print the contents of a buyer's shopping cart
     *
     * @param buyer  the buyer
     * @param market the market that the buyer is in
     */
    public static void printShoppingCart(Buyer buyer, Market market) {
        ArrayList<String> shoppingCart = buyer.getShoppingCart();

        System.out.println("------------------------------------------");

        if (shoppingCart.size() == 0) {
            System.out.println("Your cart is empty!");
            System.out.println("------------------------------------------");
            return;
        }
        double price = 0;

        for (int i = 0; i < shoppingCart.size(); i++) {
            Product p = market.getProductByIndex(Integer.parseInt(shoppingCart.get(i).split(":")[0]));
            int quantity = Integer.parseInt(shoppingCart.get(i).split(":")[1]);
            System.out.printf("%d) Name: %s | Quantity: %d | Price: $%.2f\n",
                    p.getIndex(), p.getName(), quantity, p.getPrice() * quantity);

            price += p.getPrice() * quantity;
        }
        System.out.printf("Your total price is: $%.2f\n", price);
        System.out.println("------------------------------------------");

    }

    public static void printListings(ArrayList<Product> products) {
        System.out.println("------------------------------------------");
        if (products.size() == 0) {
            System.out.println("Empty list!");
        }
        for (int i = 0; i < products.size(); i++) {
            System.out.println(productString(products.get(i)));
        }
        System.out.println("------------------------------------------");
    }

}
