package project4;

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

        System.out.println("Welcome to the book marketplace!!");
        System.out.println("Are you a buyer or a seller? (buyer/seller)");

        while (true) {
            String answer = scan.nextLine().toLowerCase();

            if (answer.equals("buyer")) {
                signUp("buyer", market, scan);
                break;
            } else if (answer.equals("seller")) {
                signUp("seller", market, scan);
                break;
            } else {
                // if answer is not "buyer" or "seller"
                System.out.println("Invalid input, try again.");
            }
        }
    }

    /**
     * Steps following the user signing in
     *
     * @param action either "buyer" or "seller", for specific user types
     * @param market the market object
     * @param scan   the scanner to read terminal input
     */
    public static void signUp(String action, Market market, Scanner scan) {
        while (true) {
            System.out.println("Do you have an account? (y/n)");
            String answer = scan.nextLine().toLowerCase();

            if (answer.equals("yes") || answer.equals("y")) {
                String email = "";
                // email prompt
                User user;
                while (true) {
                    System.out.println("Enter your email.");
                    email = scan.nextLine();
                    if (email.contains("@") && email.contains(".")) {
                        user = market.getUserByEmail(email);
                        if (user != null) {
                            break;
                        } else {
                            System.out.println("No account with that email! Try again.");
                        }
                    } else {
                        System.out.println("Please enter a valid email.");
                    }
                }

                while (true) {
                    System.out.println("What is your password?");
                    String password = scan.nextLine();
                    if (user.getPassword().equals(password)) {
                        if (user instanceof Seller) {
                            sell((Seller) user, market, scan);
                        } else {
                            buy((Buyer) user, market, scan);
                        }
                        return;
                    } else {
                        System.out.println("Password does not match. Please try again.");
                    }
                }


            } else if (answer.equals("no") || answer.equals("n")) {
                try {
                    String username = "";
                    while (true) {
                        System.out.println("Enter your email.");
                        username = scan.nextLine();

                        if (market.getAllEmails().contains(username)) {
                            System.out.println("Account already exists with that email!");
                            continue;
                        }


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
                    return;
                } catch (BadNamingException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                // if answer is not "yes" or "no"
                System.out.println("Invalid input.");
            }
        }
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
                String storeName;
                while (true) {
                    System.out.println("What is the store name?");
                    storeName = scan.nextLine();
                    if (market.getStoreNames().contains(storeName)) {
                        System.out.println("Store name already exists! Please enter another!");
                    } else {
                        break;
                    }
                }
                market.addStore(storeName, seller.getEmail());
                market.updateAllFiles();
            } else if (answer.equals("2")) {
                if (seller.getStores().size() == 0) {
                    System.out.println("Create a store before adding books!");
                    continue;
                }

                String storeName;
                while (true) {
                    System.out.println("What is the store name?");
                    System.out.println(seller.getStoreNames());
                    storeName = scan.nextLine();
                    if (!seller.getStoreNames().contains(storeName)) {
                        System.out.println("Invalid store name! Try again.");
                    } else {
                        break;
                    }
                }

                Store store = seller.getStoreByName(storeName);

                int response;
                while (true) {
                    System.out.println("1. Select existing book\n2. Create new book");
                    try {
                        response = Integer.parseInt(scan.nextLine());


                        if (response == 1) {
                            if (store.getProducts().size() == 0) {
                                System.out.println("No existing books! Try making a new book first.");
                                continue;
                            }
                            System.out.println("What is the book's index");
                            for (Product p : store.getProducts()) {
                                System.out.println(productString(p));
                            }

                            int index;
                            Product product;
                            while (true) {
                                try {
                                    index = Integer.parseInt(scan.nextLine());
                                    product = market.getProductByIndex(index);

                                    if (product == null) {
                                        System.out.println("Invalid index! Try again.");
                                        continue;
                                    }
                                    break;
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input! Try again.");
                                }
                            }
                            System.out.println("1. Edit (modify/put on sale)\n2. Remove");

                            int choice;
                            while (true) {
                                try {
                                    choice = Integer.parseInt(scan.nextLine());
                                    break;
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input! Try again.");
                                }
                            }

                            if (choice == 1) {
                                System.out.println("For the following fields, leave blank if you want to keep them " +
                                        "the same");
                                System.out.printf("New name (Current name: '%s'):\n", product.getName());

                                String newName;
                                while (true) {
                                    newName = scan.nextLine();
                                    if (!newName.equals("")) {
                                        if (!newName.contains(",")) {
                                            product.setName(newName);
                                            System.out.println("Updated name!");
                                            break;
                                        } else {
                                            System.out.println("Invalid name! Try again.");
                                        }
                                    } else {
                                        break;
                                    }
                                }


                                System.out.printf("New description (Current description: '%s'):\n",
                                        product.getDescription());
                                String newDescription;
                                while (true) {
                                    newDescription = scan.nextLine();
                                    if (!newDescription.equals("")) {
                                        if (!newName.contains(",")) {
                                            product.setDescription(newDescription);
                                            System.out.println("Updated description!");
                                            break;
                                        } else {
                                            System.out.println("Invalid description! Try again.");
                                        }
                                    } else {
                                        break;
                                    }
                                }

                                System.out.printf("New sale price (Current price: $%.2f):\n", product.getSalePrice());
                                String newSalePrice;
                                double newPrice;
                                while (true) {
                                    newSalePrice = scan.nextLine();

                                    if (!newSalePrice.equals("")) {
                                        try {
                                            newPrice = Double.parseDouble(newSalePrice);
                                            product.setSalePrice(newPrice);
                                            System.out.println("Updated sale price!");
                                            break;
                                        } catch (NumberFormatException e) {
                                            System.out.println("Invalid input! Try again.");
                                        }
                                    } else {
                                        break;
                                    }
                                }

                                System.out.printf("New original price (Current price: $%.2f):\n",
                                        product.getOriginalPrice());
                                String newOriginalPrice;
                                while (true) {
                                    newOriginalPrice = scan.nextLine();

                                    if (!newOriginalPrice.equals("")) {
                                        try {
                                            newPrice = Double.parseDouble(newOriginalPrice);
                                            product.setOriginalPrice(newPrice);
                                            System.out.println("Updated original price!");
                                            break;
                                        } catch (NumberFormatException e) {
                                            System.out.println("Invalid input! Try again.");
                                        }
                                    } else {
                                        break;
                                    }
                                }

                                System.out.printf("New quantity (Current quantity: %d):\n", product.getQuantity());
                                String newQuantity;
                                int quantity;
                                while (true) {
                                    newQuantity = scan.nextLine();

                                    if (!newQuantity.equals("")) {
                                        try {
                                            quantity = Integer.parseInt(newQuantity);
                                            product.setQuantity(quantity);
                                            System.out.println("Updated quantity!");
                                            break;
                                        } catch (NumberFormatException e) {
                                            System.out.println("Invalid input! Try again.");
                                        }
                                    } else {
                                        break;
                                    }
                                }

                                System.out.println("Product modified!");
                                System.out.println(productString(product));

                                market.updateAllFiles();

                            } else if (choice == 2) {
                                // remove
                                market.removeProduct(product);
                                market.updateAllFiles();
                                System.out.println("Removed book!");
                            } else {
                                System.out.println("Invalid input!");
                            }


                        } else if (response == 2) {
                            // add new book

                            System.out.println("The book's name:");
                            String name;
                            while (true) {
                                name = scan.nextLine();
                                if (name.contains(",")) {
                                    System.out.println("Invalid input! Try again.");
                                } else {
                                    break;
                                }
                            }

                            System.out.println("The book's description:");
                            String description;
                            while (true) {
                                description = scan.nextLine();
                                if (description.contains(",")) {
                                    System.out.println("Invalid input! Try again.");
                                } else {
                                    break;
                                }
                            }

                            System.out.println("The book's original price:");
                            String ogPriceString;
                            double originalPrice;
                            while (true) {
                                ogPriceString = scan.nextLine();
                                if (ogPriceString.contains(",")) {
                                    System.out.println("Invalid input! Try again.");
                                } else {
                                    try {
                                        originalPrice = Double.parseDouble(ogPriceString);
                                        break;
                                    } catch (NumberFormatException e) {
                                        System.out.println("Invalid input! Try again.");
                                    }
                                }
                            }

                            System.out.println("The book's sale price:");
                            String salePriceString;
                            double salePrice;
                            while (true) {
                                salePriceString = scan.nextLine();
                                if (salePriceString.contains(",")) {
                                    System.out.println("Invalid input! Try again.");
                                } else {
                                    try {
                                        salePrice = Double.parseDouble(salePriceString);
                                        break;
                                    } catch (NumberFormatException e) {
                                        System.out.println("Invalid input! Try again.");
                                    }
                                }
                            }

                            System.out.println("The book's quantity:");
                            String quantityString;
                            int quantity;
                            while (true) {
                                quantityString = scan.nextLine();
                                if (quantityString.contains(",")) {
                                    System.out.println("Invalid input! Try again.");
                                } else {
                                    try {
                                        quantity = Integer.parseInt(quantityString);
                                        break;
                                    } catch (NumberFormatException e) {
                                        System.out.println("Invalid input! Try again.");
                                    }
                                }
                            }

                            Product p = new Product(name, storeName, description, quantity,
                                    salePrice, originalPrice, -1);

                            printProductPage(p);

                            System.out.println("Add this book? (y/n)");
                            while (true) {
                                String temp = scan.nextLine();
                                if (temp.equalsIgnoreCase("y")) { //todo: check invalid input
                                    market.addProduct(p);
                                    System.out.println("Product added!");
                                    market.updateAllFiles();
                                    break;
                                } else if (temp.equalsIgnoreCase("n")) {
                                    break;
                                } else {
                                    System.out.println("Invalid input! Try again.");
                                }
                            }
                            break;

                        } else {
                            System.out.println("Invalid input!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input! Try again.");
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
                String storeName;
                while (true) {
                    storeName = scan.nextLine();
//                seller.exportProducts(fileName, store);
                    Store store = market.getStoreByName(storeName);
                    if (store == null) {
                        System.out.println("Invalid store name! Try again.");
                    } else if (!store.getSellerName().equals(seller.getEmail())) {
                        System.out.println("This store does not belong to you! Enter another name");
                    } else {

                        market.printToFile(store.getProducts(), fileName);
                        System.out.println("Export complete!");
                        break;
                    }
                }
            } else if (answer.equals("6")) {
                System.out.println("What is the file name to import from?");
                String fileName = scan.nextLine();
                market.addSellerProductsFromFile(fileName, seller);
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
                        System.out.println("Enter an item's index to learn more");
                        int idx = scan.nextInt();
                        scan.nextLine();

                        Product p = market.getProductByIndex(idx);
                        printProductPage(p);

                        System.out.println("Would you like to add to cart? (y/n)");
                        String inp = scan.nextLine();

                        if (inp.equalsIgnoreCase("y") || inp.equalsIgnoreCase("yes")) {
                            System.out.println("What is the quantity?");
                            int quantity = scan.nextInt();
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
                while (true) {
                    System.out.println("1. Leave a review\n2. Exit");
                    String response = scan.nextLine();
                    int nextStep;
                    try {
                        nextStep = Integer.parseInt(response);

                        if (nextStep == 1) {
                            System.out.println("What is the product's index?");
                            int index;
                            while (true) {
                                response = scan.nextLine();
                                try {
                                    index = Integer.parseInt(response);

                                    Product product = market.getProductByIndex(index);
                                    if (product == null) {
                                        System.out.println("No item with that index! Try again.");
                                        continue;
                                    }

                                    if (!buyer.previouslyPurchasedItem(product)) {
                                        System.out.println("This item is not in purchase history! Try again.");
                                        continue;
                                    }

                                    System.out.println("What is your rating? (0-5)");
                                    while (true) {
                                        response = scan.nextLine();

                                        int rating;
                                        try {
                                            rating = Integer.parseInt(response);

                                            if (rating < 0 || rating > 5) {
                                                System.out.println("Please enter a number between 0 and 5!");
                                                continue;
                                            }

                                            System.out.println("Enter your review");
                                            String reviewText = scan.nextLine();

                                            Review review = new Review(buyer.getEmail(), reviewText, rating);
                                            System.out.println("- - - - - - - - - - - - - - - - - - - - -");
                                            review.printString();
                                            System.out.println("- - - - - - - - - - - - - - - - - - - - -");

                                            System.out.println("Add this review? (y/n)");
                                            while (true) {
                                                response = scan.nextLine().toLowerCase();

                                                if (response.equals("y")) {
                                                    product.addReview(review);
                                                    market.updateAllFiles();
                                                    System.out.println("Added review!");
                                                    printProductPage(product);
                                                    break;
                                                } else if (response.equals("n")) {
                                                    break;
                                                } else {
                                                    System.out.println("Invalid input! Try again.");
                                                }
                                            }
                                            break;

                                        } catch (NumberFormatException e) {
                                            System.out.println("Please enter an integer value!");
                                        }
                                    }
                                    break;
                                } catch (NumberFormatException e) {
                                    System.out.println("Please enter an integer value!");
                                }
                            }
                        } else if (nextStep == 2) {
                            break;
                        } else {
                            System.out.println("Invalid input! Try again.");
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input! Try again.");
                    }
                }

            } else if (answer.equals("9")) {
                System.out.println("Have a nice day!");
                market.updateAllFiles(); // rewrite all files just in case
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
        if (p.isOnSale()) {
            return String.format("%d) On Sale! Name: %s | Store: %s | Sale Price: $%.2f | Original Price: $%.2f",
                    p.getIndex(), p.getName(), p.getStoreName(), p.getSalePrice(), p.getOriginalPrice());
        }
        return String.format("%d) Name: %s | Store: %s | $%.2f",
                p.getIndex(), p.getName(), p.getStoreName(), p.getSalePrice());
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

        String rating = p.getAverageRating() == -1 ? "n/a" : String.valueOf(p.getAverageRating());
        System.out.printf("\nReviews (Average rating: %s stars)\n- - - - - - - - - - - - - - - - - - - - -\n", rating);

        if (p.getReviews().size() == 0) {
            System.out.println("No reviews yet!\n- - - - - - - - - - - - - - - - - - - - -");
        }
        for (Review r : p.getReviews()) {
            r.printString();
            System.out.println("- - - - - - - - - - - - - - - - - - - - -");

        }
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
            if (p != null) {
                int quantity = Integer.parseInt(shoppingCart.get(i).split(":")[1]);
                if (p.isOnSale()) {
                    System.out.printf(
                            "%d) On Sale! Name: %s | Quantity: %d | Sale Price: $%.2f | Original Price $%.2f\n",
                            p.getIndex(), p.getName(), quantity, p.getSalePrice() * quantity,
                            p.getOriginalPrice() * quantity);
                } else {
                    System.out.printf("%d) Name: %s | Quantity: %d | Price: $%.2f\n",
                            p.getIndex(), p.getName(), quantity, p.getSalePrice() * quantity);
                }

                price += p.getSalePrice() * quantity;
            }
        }
        System.out.printf("Your total price is: $%.2f\n", price);
        System.out.println("------------------------------------------");

    }

    /**
     * Print the product page for every product in a list
     *
     * @param products a list of product objects
     */
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