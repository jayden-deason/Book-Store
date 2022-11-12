import java.util.Scanner;

/**
 * Main
 *
 * The main class used to launch a dashboard for a user to log in and perform actions in the marketplace
 *
 * @author Megan Long, 001
 *
 * @version 11/11/2022
 */
public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("Are you a buyer or a seller?");
            String answer = scan.nextLine();
            answer = answer.toLowerCase();
            if (answer.equals("buyer")) {
                signUp("buyer");
                break;
            } else if (answer.equals("seller")) {
                signUp("seller");
                break;
            } else {
                // if answer is not "buyer" or "seller"
                System.out.println("Invalid input.");
            }
        }
    }
    public static void signUp(String action) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("Do you have an account?");
            String answer = scan.nextLine();
            answer = answer.toLowerCase();
            if (answer.equals("yes") || answer.equals("y")) {
                while (true) {
                    System.out.println("Enter your username.");
                    String username = scan.nextLine();
                    System.out.println("Enter your password.");
                    String password = scan.nextLine();
                    //TODO: check if given information is correct below
                    if () {
                        if (action.equals("seller")) {
                            //TODO: run sell function w given info
                        } else {
                            //TODO: run buy function w given info
                        }
                        break;
                    } else {
                        // if given information does not match
                        System.out.println("Invalid information.");
                    }
                }
                break;
            } else if (answer.equals("no") || answer.equals("n")) {
                try {
                    System.out.println("Enter your username.");
                    String username = scan.nextLine();
                    System.out.println("Enter your password.");
                    String password = scan.nextLine();
                    if (action.equals("seller")) {
                        Seller seller = new Seller(username, password);
                        sell(seller);
                    } else {
                        Buyer buyer = new Buyer(username, password);
                        buy(buyer);
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
    public static void sell(Seller seller) {
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
                String store = scan.nextLine();
                //TODO: create store
            } else if (answer.equals("2")) {
                System.out.println("What is the store name?");
                String store = scan.nextLine();
                while (true) {
                    System.out.println("Enter a number:");
                    System.out.println("1 - Add product.");
                    System.out.println("2 - Delete product.");
                    System.out.println("3 - Edit product.");
                    System.out.println("4 - Import product.");
                    String answerTwo = scan.nextLine();
                    if (answerTwo.equals("1")) {
                        //TODO: add product
                        break;
                    } else if (answerTwo.equals("2")) {
                        //TODO: delete product
                        break;
                    } else if (answerTwo.equals("3")) {
                        //TODO: edit product
                        break;
                    } else if (answerTwo.equals("4")) {
                        //TODO: import product
                        break;
                    } else {
                        // if answer is not 1, 2, 3, or 4
                        System.out.println("Invalid input.");
                    }
                }
            } else if (answer.equals("3")) {
                //TODO: print stores
                System.out.println("Which store would you like to search?");
                String store = scan.nextLine();
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
            } else if (answer.equals("6")) {
                System.out.println("Have a nice day!");
                break;
            } else {
                System.out.println("Invalid input.");
            }
        }
    }
    public static void buy(Buyer buyer) {
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
                    if (answerTwo.equals("0")) {
                        buyer.printDashboard(1);
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
