import javax.print.attribute.HashPrintJobAttributeSet;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends Thread {
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
            if (userDetails[0].equals("0")) {
                if (userDetails[1].equals("0")) {
                    Buyer b = market.getBuyerByEmail(userDetails[2]);
                    if (b == null) {
                        //Sends Client "N" to signify an error (Username is wrong)
                        writer.writeObject("N");
                    } else if (b.getPassword().equals(userDetails[3])) {
                        //Sends Client "Y" to signify logged in correctly
                        writer.writeObject("Y");
                        runBuyer(b);
                    } else {
                        //Sends Client "N" to signify an error (Password is wrong)
                        writer.writeObject("N");
                    }
                }
                if (userDetails[1].equals("1")) {
                    //checks if email already exists in marketplace
                    Buyer b = market.getBuyerByEmail(userDetails[2]);
                    if (b == null) {
                        Buyer buyer = new Buyer(userDetails[2], userDetails[3]);
                        market.addBuyer(buyer);
                        writer.writeObject("Y");
                        runBuyer(buyer);
                    } else {
                        //Sends Client "N" to signify an error (Username already exists)
                        writer.writeObject("N");
                    }
                }
            }
            //Buyer
            else if (userDetails[0].equals("1")) {
                if (userDetails[1].equals("0")) {
                    Seller s = market.getSellerByEmail(userDetails[2]);
                    if (s == null) {
                        //Sends Client "N" to signify an error (Username is wrong)
                        writer.writeObject("N");
                    } else if (s.getPassword().equals(userDetails[3])) {
                        //Sends Client "Y" to signify logged in correctly
                        writer.writeObject("Y");
                    } else {
                        //Sends Client "N" to signify an error
                        writer.writeObject("N");
                    }
                }
                if (userDetails[1].equals("1")) {
                    //checks if email already exists in marketplace
                    Seller s = market.getSellerByEmail(userDetails[2]);
                    if (s == null) {
                        Seller seller = new Seller(userDetails[2], userDetails[3]);
                        market.addSeller(seller);
                        writer.writeObject("Y");
                        runSeller(seller);
                    } else {
                        //Sends Client "N" to signify an error (Username already exists)
                        writer.writeObject("N");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        //Port Number is 1001 and host is "localhost"
        ServerSocket serverSocket = new ServerSocket(1001);
        while (true) {
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

    public void runBuyer(Buyer buyer) {
        while (true) {
            try {
                String userChoice = (String) reader.readObject();
                String[] answer = userChoice.split(",");
                if (answer[0].equals("1")) {
                    this.sendAllProducts();

                } else if (answer[0].equals("2")) {
                    this.sendSearch(userChoice.substring(1));

                } else if (answer[0].equals("3")) {
                    this.viewProduct(Integer.parseInt(answer[1]));

                } else if (answer[0].equals("4")) {
                    this.exportToFile(answer[1], buyer);

                } else if (answer.equals("5")) {
                    this.makePurchase(buyer);

                } else if (answer.equals("6")) {
                    this.makePurchase(buyer);

                } else if (answer.equals("7")) {
                    this.sendShoppingCart(buyer);

                } else if (answer.equals("8")) {
                    this.changeShoppingCartQuantity(Integer.parseInt(answer[1]), Integer.parseInt(answer[2]), buyer);

                } else if (answer.equals("9")) {
                    this.sendPurchaseHistory(buyer);
                } else {
                    //Sends Client "!" to signify a special error (Invalid choice at high level of program)
                    writer.writeObject((String) "!");
                }
            } catch (Exception e) {
                //Sends Client "!" to signify special error (Invalid choice at high level of program)
                try {
                    this.writer.writeObject((String) "!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    }

    public void runSeller(Seller seller) {
        while (true) {
            try {
                String userChoice = (String) reader.readObject();
                String[] answer = userChoice.split(",");
            } catch (Exception e) {
                try {
                    this.writer.writeObject((String) "!");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void sendAllProducts() {
        try {
            this.writer.writeObject(market.getAllProducts(true));
        } catch (Exception e) {
            try {
                this.writer.writeObject((ArrayList<Product>) null);
            } catch (Exception ex) {
                System.out.println(ex.getStackTrace());
            }
        }
    }

    private void sendSearch(String search) {
        try {
            //Format for search should be productName,storeName,Description
            String[] searchContents = search.split(",");
            if (searchContents[0].equals("")) searchContents[0] = null;
            if (searchContents[1].equals("")) searchContents[1] = null;
            if (searchContents[2].equals("")) searchContents[2] = null;
            this.writer.writeObject(market.matchConditions(searchContents[0], searchContents[1],
                    searchContents[2]));
        } catch (Exception e) {
            try {
                this.writer.writeObject((ArrayList<Product>) null);
            } catch (Exception ex) {
                System.out.println(ex.getStackTrace());
            }
        }
    }

    private void viewProduct(int indexOfProduct) {
        try {
            this.writer.writeObject(this.market.getAllProducts(true).get(indexOfProduct));
        } catch (Exception e) {
            try {
                this.writer.writeObject((Product) null);
            } catch (Exception ex) {
                System.out.println(ex.getStackTrace());
            }
        }
    }

    private void addToCart(Buyer buyer, int indexOfProduct, int quantity) {
        try {
            Product p = this.market.getAllProducts(true).get(indexOfProduct);
            if (p.getQuantity() > quantity) {
                //Error: quantity trying to add to cart is more than there are of that product
                this.writer.writeObject((String) "N");
                return;
            }
            buyer.addProductToCart(p.getIndex(), quantity);
            market.updateAllFiles();
            this.writer.writeObject((String) "Y");
            //Sends new shopping cart
        } catch (Exception e) {
            try {
                this.writer.writeObject((String) "N");
            } catch (Exception ex) {
                System.out.println(ex.getStackTrace());
            }
        }
    }

    private void exportToFile(String fileName, Buyer buyer) {
        //TODO: change so client can export the file themselves by sending different info to client
        buyer.exportToFile(fileName, this.market);
    }

    private void makePurchase(Buyer buyer) {
        try {
            market.makePurchase(buyer);
            this.writer.writeObject((String) "Y");
            ;
        } catch (Exception e) {
            try {
                this.writer.writeObject((String) "N");
            } catch (Exception ex) {
                System.out.println(ex.getStackTrace());
            }
        }
    }

    private void sendShoppingCart(Buyer buyer) {
        try {
            ArrayList<String> shoppingCart = buyer.getShoppingCart();
            HashMap<Product, Integer> shoppingCartProducts = new HashMap<Product, Integer>();
            for (int i = 0; i < shoppingCart.size(); i++) {
                Product p = market.getProductByIndex(Integer.parseInt(shoppingCart.get(i).split(":")[0]));
                int quantity = Integer.parseInt(shoppingCart.get(i).split(":")[1]);
                shoppingCartProducts.put(p, quantity);
            }
            this.writer.writeObject((HashMap<Product, Integer>) shoppingCartProducts);
            ;
        } catch (Exception e) {
            try {
                this.writer.writeObject((HashMap<Product, Integer>) null);
            } catch (Exception ex) {
                System.out.println(ex.getStackTrace());
            }
        }
    }

    private void changeShoppingCartQuantity(int indexOfProduct, int newQuantity, Buyer buyer) {
        try {
            ArrayList<String> products = buyer.getShoppingCart();
            boolean exists = false;
            for (int i = 0; i < products.size(); i++) {
                if (indexOfProduct == Integer.parseInt(products.get(i).split(":")[0])) {
                    exists = true;
                    Product p = market.getProductByIndex(Integer.parseInt(products.get(i).split(":")[0]));
                    //Sends Client "N" to signify an error (Invalid Quantity)
                    if (p.getQuantity() < newQuantity) {
                        writer.writeObject((String) "N");
                        return;
                    }
                    buyer.editProductQuantity(indexOfProduct, newQuantity);
                    market.updateAllFiles();
                    //Success
                    writer.writeObject((String) "Y");
                }
            }
            if (!(exists)) {
                //Sends Client "N" to signify an error (Book did not exist within cart)
                writer.writeObject((String) "N");
            }
        } catch (Exception e) {
            try {
                this.writer.writeObject((String) "N");
            } catch (Exception ex) {
                System.out.println(ex.getStackTrace());
            }
        }
    }

    private void sendPurchaseHistory(Buyer buyer) {
        //TODO: Change so it returns the previous purchases and does not use a scanner
        buyer.printPreviousPurchases();
    }


}
