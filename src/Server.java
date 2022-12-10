import project4.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Server
 * <p>
 * A class that creates new threads for each client connection and sends and updates data based on user requests.
 *
 * @author Visv Shah & Katya Teodorovich
 * @version 12/10/22
 */
public class Server extends Thread {
    private final Socket socket; // socket connection for a particular server thread
    private ObjectOutputStream writer; // writer to send objects to the client
    private BufferedReader reader; // reader for requests from the client

    public static ArrayList<Socket> sockets = new ArrayList<Socket>(); // list of sockets for all current threads
    public static Market market; // overall marketplace
    public static final Object obj = new Object(); // concurrency gatekeeper

    /**
     * The main method creates a new thread and Server object for each new User connection
     */
    public static void main(String[] args) {
        //Port Number is 1001 and host is "localhost"
        try (ServerSocket serverSocket = new ServerSocket(1001)) {
            market = Market.getInstance();
            while (true) {
                //infinite loop to create a new thread for each connection
                //creates a new socket for each connection
                final Socket socket = serverSocket.accept();
                //creates a user using the socket
                Server user = new Server(socket);
                System.out.println("Connection Established Number: " + sockets.size());
                user.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * The run method does the authentication for both sellers and buyers and either logs them in or signs them up.
     * Then it redirects the user to the runBuyer or runSeller loops which wait for read/edit requests and carry them
     * out
     */
    public void run() {
        System.out.println("Connection Running Number: " + sockets.size());
        while (true) {
            try {
                if (socket.isClosed()) return;

                //loginString format: (0 for seller or 1 for buyer),(0 for sign in or 1 for sign up),email,password
                //Example: "0,1,test123@t.com,123" should be sign up for seller with email:test123@t.com and password 123
                String loginString = reader.readLine();
                String[] userDetails = loginString.split(",");
                if (userDetails[0].equals("-1")) { // extra logout handler
                    break;
                } else if (userDetails[0].equals("0")) { // buyer
                    if (userDetails[1].equals("0")) { // login to existing account
                        System.out.println("In Buyer Login");
                        Buyer b = null;
                        synchronized (obj) {
                            b = market.getBuyerByEmail(userDetails[2]);
                        }
                        if (b == null) {
                            //Sends Client "N" to signify an error (Username is wrong)
                            writer.writeObject("N");
                        } else if (b.getPassword().equals(userDetails[3])) {
                            //Sends Client "Y" to signify logged in correctly
                            writer.writeObject("Y");
                            runBuyer(b);
                            break;
                        } else {
                            //Sends Client "N" to signify an error (Password is wrong)
                            writer.writeObject("N");
                        }
                    }
                    if (userDetails[1].equals("1")) { // sign up with new account
                        System.out.println("In Buyer Sign up");
                        //checks if email already exists in marketplace
                        Buyer b = market.getBuyerByEmail(userDetails[2]);
                        if (b == null) {
                            Buyer buyer = null;
                            synchronized (obj) {
                                buyer = new Buyer(userDetails[2], userDetails[3]);
                                this.market.addBuyer(buyer);
                                this.market.updateAllFiles();
                            }
                            System.out.println("Created new Buyer");
                            writer.writeObject("Y");
                            runBuyer(buyer);
                            break;
                        } else {
                            //Sends Client "N" to signify an error (Username already exists)
                            writer.writeObject("N");
                        }
                    }
                }
                //Seller
                else if (userDetails[0].equals("1")) {
                    if (userDetails[1].equals("0")) { // login to existing account
                        System.out.println("In seller login");
                        Seller s = null;
                        synchronized (obj) {
                            s = market.getSellerByEmail(userDetails[2]);
                        }
                        if (s == null) {
                            //Sends Client "N" to signify an error (Username is wrong)
                            writer.writeObject("N");
                        } else if (s.getPassword().equals(userDetails[3])) {
                            //Sends Client "Y" to signify logged in correctly
                            writer.writeObject("Y");
                            runSeller(s);
                            return;
                        } else {
                            //Sends Client "N" to signify an error
                            writer.writeObject("N");
                        }
                    }
                    if (userDetails[1].equals("1")) { // sign up with new account
                        System.out.println("In seller sign up");
                        //checks if email already exists in marketplace
                        Seller s = market.getSellerByEmail(userDetails[2]);
                        if (s == null) {
                            Seller seller = null;
                            synchronized (obj) {
                                seller = new Seller(userDetails[2], userDetails[3]);
                                market.addSeller(seller);
                                market.updateAllFiles();
                            }
                            writer.writeObject("Y");
                            runSeller(seller);
                        } else {
                            //Sends Client "N" to signify an error (Username already exists)
                            writer.writeObject("N");
                        }
                    }
                }
            } catch (SocketException | NullPointerException e) {
                // if client program ends abruptly without sending "-1", it will throw socketexception
                System.out.println("Socket Exception! Closing connection.");
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        closeSocket();

    }

    /**
     * Infinite loop handling client requests for all buyer operations
     *
     * @param buyer the buyer user selected/created during the login menu
     * @throws SocketException if the socket abruptly disconnects, throw that to be handled in the run() method
     */
    public void runBuyer(Buyer buyer) throws SocketException {
        while (true) {
            System.out.println("*****************");
            try {
                writer.reset();
                String userChoice = reader.readLine().strip();
                String[] answer = userChoice.split(",");
                System.out.println(userChoice);

                switch (answer[0]) {
                    case "-1" -> {  // logout
                        closeSocket();
                        return;
                    }
                    case "1" ->  // view entire marketplace
                            this.sendAllBuyerProducts(answer[1], buyer);
                    case "2" ->  // view search results
                            this.sendSearch(userChoice.substring(2));
                    case "3" ->  // view product info
                            this.viewProduct(Integer.parseInt(answer[1]));
                    case "4" ->  // add product to cart
                            this.addToCart(buyer, Integer.parseInt(answer[1]), Integer.parseInt(answer[2]));
                    case "5" ->  // export purchase history to file
                            this.exportToFile(buyer);
                    case "6" ->  // checkout shopping cart
                            this.makePurchase(buyer);
                    case "7" ->  // view shopping cart contents
                            this.sendShoppingCart(buyer);
                    case "8" ->  // edit quantity in cart
                            this.changeShoppingCartQuantity(Integer.parseInt(answer[1]), Integer.parseInt(answer[2]), buyer);
                    case "9" ->  // view purchase history
                            this.sendPurchaseHistory(buyer);
                    case "10" ->  // view dashboard
                            this.sendBuyerDashboard(buyer, answer[1]);
                    default ->
                        //Sends Client "!" to signify a special error (Invalid choice at high level of program)
                            writer.writeObject((String) "!");
                }
            } catch (SocketException e) {
                throw e;
            } catch (Exception e) {
                //Sends Client "!" to signify special error (Invalid choice at high level of program)
                try {
                    this.writer.writeObject((String) "!");
                } catch (SocketException ex) {
                    throw ex;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * The infinite loop handling the client's requests, for all options in the seller menu
     *
     * @param seller the seller user that is currently logged in
     * @throws SocketException if the socket abruptly disconnects
     */
    public void runSeller(Seller seller) throws SocketException {
        while (true) {
            try {
                System.out.println("*****************");
                writer.reset();
                String userChoice = reader.readLine();
                String[] answer = userChoice.split(",");
                System.out.println(userChoice);

                switch (answer[0]) {
                    case "-1" -> {  // logout
                        closeSocket();
                        return;
                    }
                    case "1" ->  // view marketplace
                            this.sendAllSellerProducts(answer[1]);
                    case "2" ->  // view this seller's products
                            this.sendSellerProducts(seller, answer[1]);
                    case "3" ->  // add new product
                            this.addSellerProduct(seller, userChoice.substring(2));
                    case "4" ->  // edit product
                            this.editSellerProduct(seller, answer[1], answer[2], answer[3], answer[4], answer[5]);
                    case "5" ->   // add new store
                            this.addSellerStore(seller, answer[1]);
                    case "6" ->  // view dashboard
                            this.seeBuyerCarts(seller);
                    case "7" ->  // show store names
                            this.sendStores(seller, answer[1]);
                    case "8" ->  // export products to file
                            this.sendProductStringsForFile(seller, answer[1]);
                    case "9" ->  // import from file
                            this.importProductsFromFile(seller, userChoice.substring(2).split(",,,"));
                    case "10" ->  // show all store stats
                            this.sendAllStoresInfo(seller);
                    case "11" ->  // send one product
                            this.sendProduct(answer[1]);
                    case "12" ->  // remove product from market
                            this.removeSellerProduct(answer[1]);
                    case "13" ->  // send stats for one store (within dashboard)
                            this.sendStoreStats(answer[1]);
                    case "14" ->  // send search results
                            this.sendSearch(userChoice.substring(3));
                    default ->
                        //Sends Client "!" to signify a special error (Invalid choice at high level of program)
                            writer.writeObject((String) "!");
                }
            } catch (SocketException e) {
                throw e;
            } catch (Exception e) {
                try {
                    this.writer.writeObject((String) "!");
                } catch (SocketException ex) {
                    throw ex;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    /**
     * This function sends all the products in the marketplace and sorts them either by quantity available, the price,
     * the sales, or the purchase history of that buyer
     *
     * @param sortType either "quantity", "price", "sales", or "history"
     * @param buyer    the specific buyer if sorting by purchase history
     */
    private void sendAllBuyerProducts(String sortType, Buyer buyer) {
        try {
            ArrayList<Product> products;
            if (sortType.equals("quantity")) {
                synchronized (obj) {
                    products = this.market.sortByQuantity(true);
                }
            } else if (sortType.equals("price")) {
                synchronized (obj) {
                    products = this.market.sortByPrice(true);
                }
            } else if (sortType.equals("sales")) {
                //TODO: fix
                synchronized (obj) {
                    products = this.market.sortByPrice(true);
                }
            } else {
                synchronized (obj) {
                    products = this.market.getAllProducts(true);
                }
            }
            if (sortType.equals("history")) {
                this.sendPurchaseHistory(buyer);
            } else {
                this.writer.writeObject((ArrayList<Product>) products);
            }
            System.out.println("Sent products, sort type = " + sortType);
        } catch (Exception e) {
            try {
                this.writer.writeObject((ArrayList<Product>) null);
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends the client an ArrayList of Products that align with a search the user made
     *
     * @param search the search query
     */
    private void sendSearch(String search) {
        try {
            //Format for search should be productName,storeName,Description
            System.out.println("searching...");
            String[] searchContents = search.split(",");
            System.out.println(Arrays.toString(searchContents));
            if (searchContents[0].equals("n/a")) searchContents[0] = null;
            if (searchContents[1].equals("n/a")) searchContents[1] = null;
            if (searchContents[2].equals("n/a")) searchContents[2] = null;
            ArrayList<Product> searchResults = null;
            synchronized (obj) {
                searchResults = market.matchConditions(searchContents[0], searchContents[1],
                        searchContents[2]);
            }
            this.writer.writeObject((ArrayList<Product>) searchResults);
            System.out.println("Sent search results");
        } catch (Exception e) {
            try {
                this.writer.writeObject((ArrayList<Product>) null);

            } catch (Exception ex) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends client up to date details of a specific product
     *
     * @param indexOfProduct index of product to get
     */
    private void viewProduct(int indexOfProduct) {
        try {
            Product product = null;
            synchronized (obj) {
                product = this.market.getProductByIndex(indexOfProduct);
            }
            this.writer.writeObject(product);
            System.out.println("Sent Product with q: " + product.getQuantity());
            System.out.println(product);
        } catch (Exception e) {
            try {
                this.writer.writeObject((Product) null);
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds a product to a buyer's cart
     *
     * @param buyer          the buyer
     * @param indexOfProduct index of product to add
     * @param quantity       quantity to add
     */
    private void addToCart(Buyer buyer, int indexOfProduct, int quantity) {
        try {
            Product p;
            synchronized (obj) {
                p = market.getProductByIndex(indexOfProduct);
            }

            if (p.getQuantity() < quantity + buyer.quantityInCart(indexOfProduct)) {
                //Error: quantity trying to add to cart is more than there are of that product
                this.writer.writeObject((String) "N");
                return;
            }
            synchronized (obj) {
                buyer.addProductToCart(p.getIndex(), quantity);
                System.out.println(buyer.getShoppingCart());
                market.updateAllFiles();
            }
            this.writer.writeObject((String) "Y");
            System.out.printf("Added product %d to cart\n", indexOfProduct);
        } catch (Exception e) {
            try {
                this.writer.writeObject((String) "N");
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This function sends back an ArrayList of all the strings that need to be in the exported purchase history file
     * and sends it
     *
     * @param buyer buyer to read purchase history from
     */
    private void exportToFile(Buyer buyer) {
        try {
            ArrayList<String> purchaseHistory = null;
            synchronized (obj) {
                purchaseHistory = buyer.getPurchaseHistory();
            }
            ArrayList<String> fileInfo = new ArrayList<String>();
            ;
            for (String item : purchaseHistory) {
                int idx = Integer.parseInt(item.split(":")[0]);
                int quantity = Integer.parseInt(item.split(":")[1]);
                Product p = null;
                synchronized (obj) {
                    p = market.getProductByIndex(idx);
                }
                String s = String.format("Name: %s | Store: %s | Quantity: %d | Price: $%.2f\n",
                        p.getName(), p.getStoreName(), quantity, p.getSalePrice() * quantity);
                fileInfo.add(s);
            }
            this.writer.writeObject((ArrayList<String>) fileInfo);
            System.out.println("Wrote to file");

        } catch (Exception e) {
            try {
                this.writer.writeObject((ArrayList<String>) null);
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Checks out all of the products in a buyer's cart. Changes the buyer's purchase history, the store's inventory,
     * and clears their cart
     *
     * @param buyer the buyer making the purchase
     */
    private void makePurchase(Buyer buyer) {
        try {
            synchronized (obj) {
                market.makePurchase(buyer);
                market.updateAllFiles();
            }
            this.writer.writeObject((String) "Y");
            System.out.println("Made purchase");
        } catch (Exception e) {
            try {
                this.writer.writeObject((String) "N");
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a current list of all the products in a buyer's shopping cart
     *
     * @param buyer the buyer
     */
    private void sendShoppingCart(Buyer buyer) {
        try {
            ArrayList<String> shoppingCart = null;
            synchronized (obj) {
                shoppingCart = buyer.getShoppingCart();
            }
            HashMap<Product, Integer> shoppingCartProducts = new HashMap<Product, Integer>();
            for (String s : shoppingCart) {
                Product p = null;
                synchronized (obj) {
                    p = market.getProductByIndex(Integer.parseInt(s.split(":")[0]));
                }
                int quantity = Integer.parseInt(s.split(":")[1]);
                if (shoppingCartProducts.containsKey(p)) {
                    shoppingCartProducts.put(p, shoppingCartProducts.get(p) + quantity);
                }
                shoppingCartProducts.put(p, quantity);
            }
            this.writer.writeObject((HashMap<Product, Integer>) shoppingCartProducts);
            System.out.println("Sent shopping cart");
        } catch (Exception e) {
            try {
                this.writer.writeObject((HashMap<Product, Integer>) null);
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Changes the quantity of products in a buyer's shopping cart. If this value is zero, it removes it.
     *
     * @param indexOfProduct index of product whose quantity is being changed
     * @param newQuantity    new quantity
     * @param buyer          buyer that is changing cart
     */
    private void changeShoppingCartQuantity(int indexOfProduct, int newQuantity, Buyer buyer) {
        try {
            ArrayList<String> products = null;
            synchronized (obj) {
                products = buyer.getShoppingCart();
            }
            boolean exists = false;
            for (String product : products) {
                if (indexOfProduct == Integer.parseInt(product.split(":")[0])) {
                    exists = true;
                    Product p = null;
                    synchronized (obj) {
                        p = market.getProductByIndex(Integer.parseInt(product.split(":")[0]));
                    }
                    //Sends Client "N" to signify an error (Invalid Quantity)
                    if (p.getQuantity() < newQuantity) {
                        System.out.println("tried adding too much to cart");
                        writer.writeObject((String) "N");
                        return;
                    }
                    synchronized (obj) {
                        buyer.editProductQuantity(indexOfProduct, newQuantity);
                        market.updateAllFiles();
                    }
                    //Success
                    writer.writeObject((String) "Y");
                    System.out.println("Changed cart quantity");
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
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends the buyer a HashMap of their purchase history with key: product and value: quantity
     *
     * @param buyer the buyer to read purchase history from
     */
    private void sendPurchaseHistory(Buyer buyer) {
        try {
            ArrayList<String> purchaseHistory = null;
            synchronized (obj) {
                purchaseHistory = buyer.getPurchaseHistory();
            }
            HashMap<Product, Integer> previousProducts = new HashMap<Product, Integer>();
            for (String item : purchaseHistory) {
                int idx = Integer.parseInt(item.split(":")[0]);
                int quantity = Integer.parseInt(item.split(":")[1]);
                Product p = null;
                synchronized (obj) {
                    p = market.getProductByIndex(idx);
                }
                if (previousProducts.containsKey(p)) {
                    previousProducts.put(p, previousProducts.get(p) + quantity);
                } else {
                    previousProducts.put(p, quantity);
                }

            }
            this.writer.writeObject((HashMap<Product, Integer>) previousProducts);
            System.out.println("Wrote purchase history");
        } catch (Exception e) {
            try {
                this.writer.writeObject((HashMap<Product, Integer>) null);
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends the buyer dashboard based on several sorting factors
     *
     * @param buyer    the buyer
     * @param sortType either "sales" or "history"
     */
    private void sendBuyerDashboard(Buyer buyer, String sortType) {
        try {
            ArrayList<String> out = null;
            if (sortType.equals("sales")) {
                ArrayList<String> dashboard = null;
                synchronized (obj) {
                    dashboard = buyerDashboardForStoreList(market.sortStoresByProductsSold());
                }
                writer.writeObject(dashboard);
            } else if (sortType.equals("history")) {
                ArrayList<String> dashboard = null;
                synchronized (obj) {
                    dashboard = buyerDashboardForStoreList(buyer.sortStoresByPurchaseHistory(market));
                }
                writer.writeObject(dashboard);
            } else {
                writer.writeObject(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a list of all product availabilities in a given store
     *
     * @param stores stores to read products for
     * @return a list of strings for each product
     */
    private ArrayList<String> buyerDashboardForStoreList(ArrayList<Store> stores) {
        ArrayList<String> out = new ArrayList<>();

        for (Store store : stores) {
            String string = "";
            string += store.getName() + ":";
            for (Product p : store.getProducts()) {
                string += p.getName() + ",";
                string += p.getQuantity() + ";";
            }
            out.add(string.substring(0, string.length() - 1));
        }


        return out;

    }


    /**
     * Sends a seller a list of all the products in the marketplace to view them
     *
     * @param sortType either "quantity", "price", "sales", or "none"
     */
    private void sendAllSellerProducts(String sortType) {
        try {
            ArrayList<Product> products;
            if (sortType.equals("quantity")) {
                synchronized (obj) {
                    products = market.sortByQuantity(true);
                }
            } else if (sortType.equals("price")) {
                synchronized (obj) {
                    products = market.sortByPrice(true);
                }
            } else if (sortType.equals("sales")) {
                synchronized (obj) {
                    products = market.sortBySales(true);
                }
            } else {
                synchronized (obj) {
                    products = market.getAllProducts(true);
                }
            }
            this.writer.writeObject(products);
            System.out.println("Sent products, sort type = " + sortType);
//            System.out.println(products);
        } catch (Exception e) {
            try {
                this.writer.writeObject((ArrayList<Product>) null);
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends the seller a list of all of their products across all of their stores and allows them to sort by sales or
     * customers
     *
     * @param seller   the seller requesting their product
     * @param sortType either "sales" or "customers"
     */
    private void sendSellerProducts(Seller seller, String sortType) {
        try {
            ArrayList<Product> products = null;
            synchronized (obj) {
                products = seller.getProducts(true);
            }
            if (sortType.equals("sales")) {
                synchronized (obj) {
                    products.sort(Comparator.comparingInt(s -> market.getSalesForProduct(s)));
                }
            } else if (sortType.equals("customers")) {
                synchronized (obj) {
                    products.sort(Comparator.comparingInt(s -> market.getCustomersForProduct(s)));
                }
            }
            this.writer.writeObject(products);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Allows the seller to add a product to one of their stores
     *
     * @param seller        the seller adding a product
     * @param productString the string with a product description
     */
    private void addSellerProduct(Seller seller, String productString) {
        try {
            String[] arr = productString.split(",");
            String productName = arr[0].strip();
            String storeName = arr[1].strip();
            String description = arr[2].strip();
            double price = Double.parseDouble(arr[3].strip());
            int quantity = Integer.parseInt(arr[4].strip());
            System.out.println("adding product " + productName);
            synchronized (obj) {
                Product product = new Product(productName, storeName, description, quantity, price, price, -1);
                if (seller.getStoreNames().contains(storeName)) {
                    if (seller.getStoreByName(storeName).getProductNames().contains(productName)) {
                        this.writer.writeObject("N");
                        return;
                    }
                    market.addProduct(product);
                    market.updateAllFiles();
                    this.writer.writeObject("Y");
                } else {
                    this.writer.writeObject("N");
                }
            }

        } catch (Exception e) {
            try {
                this.writer.writeObject("N");
            } catch (IOException ex) {
                e.printStackTrace();
            }
        }
    }

    /**
     * allows the seller to edit one of the products in their store
     *
     * @param seller         the logged-in seller
     * @param productIndex   the index of the product to change
     * @param newName        the new name for the product
     * @param newDescription the new description for the product
     * @param newPrice       the new price for the product
     * @param newQuantity    the new quantity for the product
     */
    private void editSellerProduct(Seller seller, String productIndex, String newName,
                                   String newDescription, String newPrice, String newQuantity) {

        try {
            double price = Double.parseDouble(newPrice);
            int quantity = Integer.parseInt(newQuantity);

            synchronized (obj) {
                Product product = market.getProductByIndex(Integer.parseInt(productIndex));
                product.setName(newName);
                product.setDescription(newDescription);
                product.setSalePrice(price);
                product.setOriginalPrice(price);
                product.setQuantity(quantity);

                market.editProduct(product);
                market.updateAllFiles();
            }

            this.writer.writeObject("Y");
            System.out.println("edited product");

        } catch (Exception e) {
            e.printStackTrace();
            try {
                this.writer.writeObject("N");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Allows the seller to add a store
     *
     * @param seller    the logged-in seller
     * @param storeName the name for the new store
     */
    private void addSellerStore(Seller seller, String storeName) {
        try {
            synchronized (obj) {
//                if (market.getStoreByName(storeName) == null) {
                market.addStore(new Store(-1, storeName, seller.getEmail()));
                market.updateAllFiles();
            }
            this.writer.writeObject("Y");
        } catch (Exception e) {
            try {
                this.writer.writeObject("N");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * Allows the seller to remove one of their products
     *
     * @param productIndex the index of the product to remove from the market
     */
    private void removeSellerProduct(String productIndex) {
        try {
            synchronized (obj) {
                market.removeProduct(market.getProductByIndex(Integer.parseInt(productIndex)));
            }
            writer.writeObject("Y");
            System.out.println("removed product: " + productIndex);
        } catch (Exception e) {
            try {
                e.printStackTrace();
                writer.writeObject("N");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    /**
     * Shows the seller which of their products are in customer shopping carts. Shows the seller the product, the
     * quantity, and the customer email
     *
     * @param seller the seller requesting data for products in carts
     */
    private void seeBuyerCarts(Seller seller) {
        try {
            HashMap<Product, String> productsInCart = null;
            synchronized (obj) {
                productsInCart = seller.sendProductsInCart(market);
            }
            this.writer.writeObject((HashMap<Product, String>) productsInCart);
            System.out.println("Wrote purchase history");
        } catch (Exception e) {
            try {
                this.writer.writeObject((HashMap<Product, String>) null);
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends the seller a list of their stores
     *
     * @param seller   the logged-in seller
     * @param sortType either sorting by "sales" or "alphabet"
     */
    private void sendStores(Seller seller, String sortType) {
        try {
            ArrayList<String> stores;
            synchronized (obj) {
                stores = seller.getStoreNamesSorted(sortType);
            }
            writer.writeObject(stores);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the seller a list of their products in a store to export a file
     *
     * @param seller    the seller making the request
     * @param storeName the name of the store to export
     */
    private void sendProductStringsForFile(Seller seller, String storeName) {
        try {
            synchronized (obj) {
                if (!seller.getStoreNames().contains(storeName)) {
                    writer.writeObject(null);
                    return;
                }
            }
            Store store = null;
            synchronized (obj) {
                store = seller.getStoreByName(storeName);
            }
            ArrayList<String> out = new ArrayList<>();
            ArrayList<Product> storeProducts = null;
            synchronized (obj) {
                storeProducts = store.getProducts();
            }
            for (Product p : storeProducts) {
                out.add(p.toString());
            }

            writer.writeObject(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a list of products to one of the seller's store. These products are imported from a file by the seller.
     *
     * @param seller the seller making the request
     * @param lines  a list of lines from the imported file, in the product.toString() format
     */
    private void importProductsFromFile(Seller seller, String[] lines) {
        System.out.println("got here");
        try {
            for (String line : lines) {
                synchronized (obj) {
                    Product newProduct = new Product(line);
                    newProduct.setIndex(-1);
                    System.out.println(newProduct);
                    if (seller.getStoreNames().contains(newProduct.getStoreName())) {
                        market.addProduct(newProduct);
                    }
                }
            }
            synchronized (obj) {
                market.updateAllFiles();
            }
            writer.writeObject("Y");
            System.out.println("sent y");

        } catch (Exception e) {
            try {
                writer.writeObject("N");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Sends the information regarding how all of their stores are doing including sales and revenue
     *
     * @param seller the seller making the request
     */
    private void sendAllStoresInfo(Seller seller) {
        ArrayList<Store> stores;
        synchronized (obj) {
            stores = seller.getStores();
        }
        String[][] out = null;

        if (stores.size() == 0) {
            out = new String[1][4];
            out[0] = new String[]{"Name", "Sales", "Revenue", "Products in Carts"};

        } else {

            out = new String[stores.size() + 1][4];
            out[0] = new String[]{"Name", "Sales", "Revenue", "Products in Carts"};
            for (int i = 0; i < stores.size(); i++) {
                Store s = stores.get(i);
                out[i + 1] = new String[]{s.getName(), String.valueOf(s.getSales(market)),
                        String.format("$%.2f", s.getRevenue()), s.getProductsInCarts()};
            }
        }

        try {
            writer.writeObject(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends stats for a specific store for the seller
     *
     * @param storeName the store to get stats for
     */
    private void sendStoreStats(String storeName) {
        String info;
        try {
            synchronized (obj) {
                Store store = market.getStoreByName(storeName);
                info = String.join("\n", store.statisticsForSeller(0, market));

            }
            writer.writeObject(info);


        } catch (Exception e) {
            e.printStackTrace();
            try {
                writer.writeObject("No statistics available!");
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Lets the seller a specific product in the marketplace
     *
     * @param productIndex the index of the selected product
     */
    private void sendProduct(String productIndex) {
        Product p;
        synchronized (obj) {
            p = market.getProductByIndex(Integer.parseInt(productIndex));
        }
        System.out.println(p);

        if (p == null) {
            System.out.printf("failed to get '%s'\n", productIndex);
        }

        try {
            writer.writeObject(p);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * The constructor creates a new Server object for each connected client and gives it a unique socket and a
     * writer/reader to communicate with that client.
     *
     * @param socket the socket connecting the server and client
     */
    public Server(Socket socket) {
        this.socket = socket;
        sockets.add(this.socket);
        try {
            writer = new ObjectOutputStream(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Streams created");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes all the writers/readers and the overall socket connection
     */
    private void closeSocket() {
        try {
            System.out.println("Closing socket!!");
            reader.close();
            writer.close();
            //Make concurrent
            Server.sockets.remove(this.socket);
            this.socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
