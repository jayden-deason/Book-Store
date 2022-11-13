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
        System.out.println("What is the buyer file?");
        String buyerFile = scan.nextLine();
        System.out.println("What is the seller file?");
        String sellerFile = scan.nextLine();
        System.out.println("What is the store file?");
        String storesFile = scan.nextLine();
        System.out.println("What is the product file?");
        String productsFile = scan.nextLine();
        Market market = new Market(buyerFile, sellerFile, storesFile, productsFile);
        while (true) {
            System.out.println("Are you a buyer or a seller?");
            String answer = scan.nextLine();
            answer = answer.toLowerCase();
            if (answer.equals("buyer")) {
                signUp("buyer", market);
                break;
            } else if (answer.equals("seller")) {
                signUp("seller", market);
                break;
            } else {
                // if answer is not "buyer" or "seller"
                System.out.println("Invalid input.");
            }
        }
    }
    public static void signUp(String action, Market market) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("Do you have an account?");
            String answer = scan.nextLine();
            answer = answer.toLowerCase();
            if (answer.equals("yes") || answer.equals("y")) {
                while (true) {
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
                    //TODO: check if given information is correct below
                    if () {
                        if (action.equals("seller")) {
                            Seller seller = market.getSellerByEmail(username);
                            sell(seller, market);
                        } else {
                            //TODO: run buy function w given info
                        }
                        break;
                    } else {
                        // if given information does not match
                        System.out.println("Username and password do not match.");
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
                        sell(seller, market);
                    } else {
                        Buyer buyer = new Buyer(username, password);
                        buy(buyer, market);
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
    public static void sell(Seller seller, Market market) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("Enter a number:");
            System.out.println("1 - Create a store.");
            System.out.println("2 - Edit store.");
            System.out.println("3 - View sales.");
            System.out.println("4 - View dashboard.");
            System.out.println("5 - Export products.");
            System.out.println("6 - Exit.");
            String answer = scan.nextLine();
            if (answer.equals("1")) {
                System.out.println("What is the store name?");
                String storeName = scan.nextLine();
                System.out.println("What is the seller name?");
                String sellerName = scan.nextLine();
                System.out.println("What is the product file name?");
                String productFile = scan.nextLine();
                double sales = 0;
                double revenue = 0;
                boolean num;
                while (true) {
                    try {
                        num = true;
                        System.out.println("What are the sales?");
                        sales = Double.parseDouble(scan.nextLine());
                        System.out.println("What is the revenue?");
                        revenue = Double.parseDouble(scan.nextLine());
                    } catch (NumberFormatException e) {
                        num = false;
                    }
                    if (num) {
                        break;
                    } else {
                        System.out.println("Please enter valid numbers!");
                    }
                }
                seller.addStore(storeName, sellerName, productFile, sales, revenue);
            } else if (answer.equals("2")) {
                System.out.println("What is the store file name?");
                String storeFile = scan.nextLine();
                Store store = new Store(storeFile);
                System.out.println("What is the product name?");
                String name = scan.nextLine();
                System.out.println("What is the store name?");
                String storeName = scan.nextLine();
                System.out.println("What is the description?");
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
                        System.out.println("What is the index?");
                        index = Integer.parseInt(scan.nextLine());
                        if (quantity < 0 || price < 0 || index < 0) {
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
                    System.out.println("1 - Add product.");
                    System.out.println("2 - Delete product.");
                    System.out.println("3 - Edit product.");
                    System.out.println("4 - Import product.");
                    String answerTwo = scan.nextLine();
                    if (answerTwo.equals("1")) {
                        store.addProduct(product);
                    } else if (answerTwo.equals("2")) {
                        store.removeProduct(product);
                    } else if (answerTwo.equals("3")) {
                        store.modifyProduct(product);
                    } else if (answerTwo.equals("4")) {
                        //TODO: import product
                    } else {
                        // if answer is not 1, 2, 3, or 4
                        System.out.println("Invalid input.");
                    }
                }
            } else if (answer.equals("3")) {
                ArrayList<Store> stores = seller.getStores();
                for (int i = 0; i < stores.size(); i++) {
                    System.out.println(stores.get(i).getName());
                }
                System.out.println("Which store would you like to search?");
                //TODO: view sales
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
                System.out.println("Have a nice day!");
                break;
            } else {
                System.out.println("Invalid input.");
            }
        }
    }
    public static void buy(Buyer buyer, Market market) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("Enter a number:");
            System.out.println("1 - View marketplace.");
            System.out.println("2 - View dashboard.");
            System.out.println("3 - Find product.");
            System.out.println("4 - Export shopping history.");
            System.out.println("5 - Checkout shopping cart.");
            System.out.println("6 - Exit.");
            String answer = scan.nextLine();
            if (answer.equals("1")) {
                //TODO: view marketplace
            } else if (answer.equals("2")) {
                while (true) {
                    System.out.println("Enter a number:");
                    System.out.println("1 - No sorting.");
                    System.out.println("2 - Sort alphabetically.");
                    System.out.println("3 - Sort by quantity.");
                    String answerTwo = scan.nextLine();
                    if (answerTwo.equals("1")) {
                        buyer.printDashboard(0);
                        break;
                    } else if (answerTwo.equals("2")) {
                        buyer.printDashboard(1);
                        break;
                    } else if (answerTwo.equals("3")) {
                        buyer.printDashboard(2);
                        break;
                    } else {
                        // if answer is not 1, 2, or 3
                        System.out.println("Invalid input.");
                    }
                }
            } else if (answer.equals("3")) {
                System.out.println("Which store would you like to search?");
                String store = scan.nextLine();
                //TODO: find product
            } else if (answer.equals("4")) {
                System.out.println("What is the file name to export to?");
                String fileName = scan.nextLine();
                buyer.exportToFile(fileName);
            } else if (answer.equals("5")) {
                //TODO: checkout shopping cart
            } else if (answer.equals("6")) {
                System.out.println("Have a nice day!");
                break;
            } else {
                System.out.println("Invalid input.");
            }
        }
    }
}
