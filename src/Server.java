import java.io.*;
import java.net.*;
import java.util.ArrayList;
public class Server extends Thread{
    private final Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;

    public static ArrayList<Socket> sockets = new ArrayList<Socket>();
    public static Market market;
    public void run() {
        try {
            writer = new ObjectOutputStream(this.socket.getOutputStream());
            reader = new ObjectInputStream(this.socket.getInputStream());
            //loginString format: (0 for seller or 1 for buyer),(0 for sign in or 1 for sign up),email,password
            //Example: "0,1,test123@t.com,123" should be sign up for seller with email:test123@t.com and password 123
            String loginString = (String) reader.readObject();
            String[] userDetails = loginString.split(",");
            //Seller
            if(userDetails[0].equals("0")) {
                if(userDetails[1].equals("0")) {
                    Buyer b = market.getBuyerByEmail(userDetails[2]);
                    if(b == null) {
                        //Sends Client "N" to signify an error (Username is wrong)
                        writer.writeObject("N");
                    }
                    else if(b.getPassword().equals(userDetails[3])) {
                        //Sends Client "Y" to signify logged in correctly
                        writer.writeObject("Y");
                        runBuyer(b);
                    }
                    else{
                        //Sends Client "N" to signify an error (Password is wrong)
                        writer.writeObject("N");
                    }
                }
                if(userDetails[1].equals("1")) {
                    //checks if email already exists in marketplace
                    Buyer b = market.getBuyerByEmail(userDetails[2]);
                    if(b == null) {
                        Buyer buyer = new Buyer(userDetails[2], userDetails[3]);
                        market.addBuyer(buyer);
                        writer.writeObject("Y");
                        runBuyer(buyer);
                    }
                    else {
                        //Sends Client "N" to signify an error (Username already exists)
                        writer.writeObject("N");
                    }
                }
            }
            //Buyer
            else if(userDetails[0].equals("1")) {
                if(userDetails[1].equals("0")) {
                    Seller s = market.getSellerByEmail(userDetails[2]);
                    if(s == null) {
                        //Sends Client "N" to signify an error (Username is wrong)
                        writer.writeObject("N");
                    }
                    else if(s.getPassword().equals(userDetails[3])) {
                        //Sends Client "Y" to signify logged in correctly
                        writer.writeObject("Y");
                    }
                    else{
                        //Sends Client "N" to signify an error
                        writer.writeObject("N");
                    }
                }
                if(userDetails[1].equals("1")) {
                    //checks if email already exists in marketplace
                    Seller s = market.getSellerByEmail(userDetails[2]);
                    if(s == null) {
                        Seller seller = new Seller(userDetails[2], userDetails[3]);
                        market.addBuyer(seller);
                        writer.writeObject("Y");
                        runSeller(seller);
                    }
                    else {
                        //Sends Client "N" to signify an error (Username already exists)
                        writer.writeObject("N");
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //log in or sign up
        while(true){
            //infinitely waits for user to make requests to access data and sends data back to user
        }
    }
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException{
        //Port Number is 1001 and host is "localhost"
        ServerSocket serverSocket = new ServerSocket(1001);
        while(true) {
            //infinite loop to create a new thread for each connection
            //creates a new socket for each connection
            final Socket socket = serverSocket.accept();
            //creates a user using the socket
            Server user = new Server(socket);
            user.start();
        }
    }
    public Server(Socket socket) {
        this.socket = socket;
        sockets.add(this.socket);
    }
    public void runBuyer(BufferedReader reader, PrintWriter writer, Buyer buyer) {
        while(true) {
            try {
                String userChoice = reader.readLine();
                String answer = (userChoice.substring(0, 1);
                userChoice = userChoice.substring(1);
                if (answer.equals("1")) {
                    this.sendAllProducts();
                    writer.println(market.getAllProducts(true));
                    int nextChoice = reader.readLine();
                    while (nextChoice != 3) {
                        if (nextChoice == 1) {
                            //Pick a product
                            int productIndex = reader.readLine();
                            Product p = market.getProductByIndex(productIndex);
                            writer.println(p);
                            //Add to Cart?
                            String atc = reader.readLine();

                            if (atc.equalsIgnoreCase("y") || atc.equalsIgnoreCase("yes")) {
                                //Quantity
                                int quantity = reader.readLine();
                                buyer.addProductToCart(productIndex, quantity);
                                market.updateAllFiles();
                                //Success
                                writer.println("Y");

                            }
                        } else if (nextChoice == 2) {
                            //Format for search should be productName,storeName,Description
                            String search = reader.readLine();
                            String[] searchContents = search.split(",");
                            if (searchContents[0].equals("")) searchContents[0] = null;

                            if (searchContents[1].equals("")) searchContents[1] = null;

                            if (searchContents[2].equals("")) searchContents[2] = null;

                            //TODO: Change so it returns the matched listings
                            writer.println(market.matchConditions(searchContents[0], searchContents[1],
                                    searchContents[2]));
                        } else {
                            break;
                        }
                        nextChoice = reader.readLine();
                    }
                } else if (answer.equals("2")) {
                    //TODO: Change so it returns the shopping cart of products (get this method from main)
                    printShoppingCart(buyer, market);

                } else if (answer.equals("3")) {
                    //Sends all store names
                    writer.println(market.getStoreNames());
                    //gets back one store name
                    String storeName = reader.readLine();
                    Store store = market.getStoreByName(storeName);
                    if (store == null) {
                        //Sends Client "N" to signify an error (No such store exists)
                        writer.println("N");
                    } else {
                        String productName = reader.readLine();
                        Product product = store.getProductByName(productName);

                        if (product == null) {
                            //Sends Client "N" to signify an error (No such book exists)
                            writer.println("N");
                        } else {
                            //TODO: Change so it returns the details of product (Get this method from main)
                            printProductPage(product);
                            System.out.println("Would you like to add to cart? (y/n)");
                            String answerTwo = reader.readLine();
                            answerTwo = answerTwo.toLowerCase();
                            if (answerTwo.equals("yes") || answerTwo.equals("y")) {
                                boolean num;
                                int quantity = -1;
                                while (true) {
                                    num = true;
                                    try {
                                        quantity = Integer.parseInt(reader.readLine());
                                    } catch (NumberFormatException e) {
                                        num = false;
                                    }
                                    if (quantity < 1) {
                                        num = false;
                                    }
                                    if (num) {
                                        break;
                                    } else {
                                        //Sends Client "N" to signify an error (Invalid Qunatity)
                                        writer.println("N");
                                    }
                                }
                                buyer.addProductToCart(product.getIndex(), quantity);
                                market.updateAllFiles();
                                //Sucess
                                writer.println("Y");
                                //Sends new shopping cart
                                //TODO: Change so it returns the shopping cart of products (get this method from main)
                                printShoppingCart(buyer, market);
                            }
                        }
                    }
                } else if (answer.equals("4")) {
                    String fileName = reader.readLine();
                    buyer.exportToFile(fileName, market);
                    writer.println("Y");
                } else if (answer.equals("5")) {
                    market.makePurchase(buyer);
                    writer.println("Y");
                } else if (answer.equals("6")) {
                    //TODO: Change so it returns the shopping cart of products (get this method from main)
                    printShoppingCart(buyer, market);
                    boolean num;
                    int index = -1;
                    int quantity = -1;
                    while (true) {
                        num = true;
                        try {
                            //get index of product
                            index = Integer.parseInt(reader.readLine());
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
                                        try {
                                            //new quantity (0 for delete)
                                            quantity = Integer.parseInt(reader.readLine());
                                        } catch (NumberFormatException e) {
                                            num = false;
                                        }
                                        if (quantity < 1) {
                                            num = false;
                                        }
                                        if (num) {
                                            break;
                                        } else {
                                            //Sends Client "N" to signify an error (Invalid Qunatity)
                                            writer.println("N");
                                        }
                                    }
                                    buyer.editProductQuantity(index, quantity);
                                    market.updateAllFiles();
                                    //Success
                                    writer.println("Y");
                                }
                            }
                            if (!(exists)) {
                                //Sends Client "N" to signify an error (Book did not exist within cart)
                                writer.println("N");
                            }
                        } else {
                            //Sends Client "N" to signify an error (Invalid index)
                            writer.println("N");
                        }
                        break;
                    }
                } else if (answer.equals("7")) {
                    //TODO: Change so it returns the dashboard and does not use a scanner
                    buyer.printDashboard(scan, market);
                } else if (answer.equals("8")) {
                    //TODO: Change so it returns the previous purchases and does not use a scanner
                    buyer.printPreviousPurchases();
                } else if (answer.equals("9")) {
                    //Success
                    writer.println("Y");
                    return;
                } else {
                    //Sends Client "N" to signify an error (Invalid choice)
                    writer.println("N");
                }
            }
            catch (Exception e) {
                //Sends Client "N" to signify an error
                writer.println("N");
            }

        }
    }
    public static void runSeller(BufferedReader reader, PrintWriter writer) {
        while(true) {

        }
    }
    private void sendAllProducts() {
        writer.println(market.getAllProducts(true));
    }
    private void sendSearch() {
        try {
            //Format for search should be productName,storeName,Description
            String search = reader.readLine();
            String[] searchContents = search.split(",");
            if (searchContents[0].equals("")) searchContents[0] = null;

            if (searchContents[1].equals("")) searchContents[1] = null;

            if (searchContents[2].equals("")) searchContents[2] = null;

            //TODO: Change so it returns the matched listings
            writer.println(market.matchConditions(searchContents[0], searchContents[1],
                    searchContents[2]));
        }
        catch(Exception e) {
            writer.println((ArrayList<Product>) null);

        }
    }
}
