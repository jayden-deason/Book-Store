import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
/**
 * Server
 *
 * A class that creates new threads for each client connection and sends and updates data based on user requests.
 *
 * @author Visv Shah
 * @version 12/9/22
 */
public class Server extends Thread {
    private final Socket socket;
    private ObjectOutputStream writer;
    private BufferedReader reader;

    public static ArrayList<Socket> sockets = new ArrayList<Socket>();
    public static Market market;
    public static Object obj = new Object();
    //The main method creates a new thread and Server object for each new User connection
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        //Port Number is 1001 and host is "localhost"
        ServerSocket serverSocket = new ServerSocket(1001);
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
    }
    //The run method does the authenciation for both sellers and buyers and either logs them in or signs them up.
    // Then it redirects the user to the runBuyer or runSeller loops which wait for read/edit requests and carry them
    // out
    public void run() {
        System.out.println("Connection Running Number: " + sockets.size());
        while (true) {
            try {
                //loginString format: (0 for seller or 1 for buyer),(0 for sign in or 1 for sign up),email,password
                //Example: "0,1,test123@t.com,123" should be sign up for seller with email:test123@t.com and password 123
                String loginString = reader.readLine();
                String[] userDetails = loginString.split(",");
                //Seller
                if (userDetails[0].equals("-1")) {
                    System.out.println("Closing socket!");
                    reader.close();
                    writer.close();
                    //Make concurrent
                    Server.sockets.remove(this.socket);
                    this.socket.close();
                    return;
                } else if (userDetails[0].equals("0")) {
                    if (userDetails[1].equals("0")) {
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
                    if (userDetails[1].equals("1")) {
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
                    if (userDetails[1].equals("0")) {
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
                    if (userDetails[1].equals("1")) {
                        System.out.println("In seller sign up");
                        //checks if email already exists in marketplace
                        Seller s = market.getSellerByEmail(userDetails[2]);
                        if (s == null) {
                            Seller seller = null;
                            synchronized (obj) {
                                seller = new Seller(userDetails[2], userDetails[3]);
                                this.market.addSeller(seller);
                                this.market.updateAllFiles();
                            }
                            writer.writeObject("Y");
                            runSeller(seller);
                        } else {
                            //Sends Client "N" to signify an error (Username already exists)
                            writer.writeObject("N");
                        }
                    }
                }
            } catch (SocketException e) {
                System.out.println("Socket Exception! Closing connection.");
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void runBuyer(Buyer buyer) throws SocketException {
        while (true) {
            System.out.println("Loop ran");
            try {
                writer.reset();
                String userChoice = reader.readLine().strip();
                String[] answer = userChoice.split(",");
                System.out.println(userChoice);
                if (answer[0].equals("-1")) {
                    System.out.println("Closing socket!");
                    reader.close();
                    writer.close();
                    //Make concurrent
                    Server.sockets.remove(this.socket);
                    this.socket.close();
                    return;
                } else if (answer[0].equals("1")) {
                    this.sendAllBuyerProducts(answer[1], buyer);

                } else if (answer[0].equals("2")) {
                    this.sendSearch(userChoice.substring(2));

                } else if (answer[0].equals("3")) {
                    this.viewProduct(Integer.parseInt(answer[1]));

                } else if (answer[0].equals("4")) {
                    this.addToCart(buyer, Integer.parseInt(answer[1]), Integer.parseInt(answer[2]));

                } else if (answer[0].equals("5")) {
                    this.exportToFile(buyer);

                } else if (answer[0].equals("6")) {
                    this.makePurchase(buyer);

                } else if (answer[0].equals("7")) {
                    this.sendShoppingCart(buyer);

                } else if (answer[0].equals("8")) {
                    this.changeShoppingCartQuantity(Integer.parseInt(answer[1]), Integer.parseInt(answer[2]), buyer);

                } else if (answer[0].equals("9")) {
                    this.sendPurchaseHistory(buyer);

                } else if (answer[0].equals("10")) {
                    this.sendBuyerDashboard(buyer, answer[1]);

                } else {
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
            System.out.println("End of loop");
        }
    }

    public void runSeller(Seller seller) throws SocketException {
        while (true) {
            try {
                writer.reset();
                String userChoice = reader.readLine();
                String[] answer = userChoice.split(",");
                System.out.println(userChoice);
                if (answer[0].equals("-1")) {
                    System.out.println("Closing socket!");
                    reader.close();
                    writer.close();
                    //Make concurrent
                    this.sockets.remove(this.socket);
                    this.socket.close();
                    return;
                } else if (answer[0].equals("1")) {
                    // send all products
                    this.sendAllSellerProducts(answer[1], seller);
                } else if (answer[0].equals("2")) {
                    // send this seller's products
                    this.sendSellerProducts(seller, answer[1]);
                } else if (answer[0].equals("3")) {
                    // add product
                    this.addSellerProduct(seller, userChoice.substring(2));

                } else if (answer[0].equals("4")) {
                    // edit product
                    this.editSellerProduct(seller,
                            answer[1], // current name
                            answer[2], // new name
                            answer[3], // new description
                            answer[4], // new price
                            answer[5] // new quantity
                    );
                } else if (answer[0].equals("5")) {
                    // add store
                    this.addSellerStore(seller, answer[1]);
                } else if (answer[0].equals("6")) {
                    // view dashboard
                    this.seeBuyerCarts(seller);
                } else if (answer[0].equals("7")) {
                    // show seller store stats
                    this.sendStores(seller, answer[1]);
                } else if (answer[0].equals("8")) {
                    // export products to file
                    this.sendProductStringsForFile(seller, answer[1]);
                } else if (answer[0].equals("9")) {
                    // import from file
                    this.importProductsFromFile(seller, userChoice.substring(2).split("\n"));
                } else if (answer[0].equals("10")) {
                    this.sendAllStoresInfo(seller);
                } else if (answer[0].equals("11")) {
                    this.sendProduct(answer[1]);
                } else if (answer[0].equals("12")) {
                    this.removeSellerProduct(answer[1]);
                } else if (answer[0].equals("13")) {
                    this.sendStoreStats(answer[1]);
                }
                else {
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
    //This function sends all the products in the marketplace and sorts them either by quantity available, the price,
    // the sales
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
    private void sendSellerProducts(Seller seller, String sortType) throws IOException {
        ArrayList<Product> products = null;
        synchronized (obj) {
            products = seller.getProducts();
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
    }

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
                    market.addProduct(product);
                    market.updateAllFiles();
                    this.writer.writeObject("Y");
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

    private void editSellerProduct(Seller seller, String productName, String newName,
                                   String newDescription, String newPrice, String newQuantity) {

        try {
            double price = Double.parseDouble(newPrice);
            int quantity = Integer.parseInt(newQuantity);

            synchronized (obj) {
                Product product = market.getProductByName(productName);
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
            try {
                this.writer.writeObject("N");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void addSellerStore(Seller seller, String storeName) {
        try {
            synchronized (obj) {
                System.out.println("adding store");
//                if (market.getStoreByName(storeName) == null) {
                market.addStore(new Store(-1, storeName, seller.getEmail()));
                market.updateAllFiles();
                this.writer.writeObject("Y");


            }
        } catch (Exception e) {
            try {
                this.writer.writeObject("N");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void removeSellerProduct(String productName) {
        try {
            market.removeProduct(market.getProductByName(productName));
            writer.writeObject("Y");
            System.out.println("removed product: " + productName);
        } catch (Exception e) {
            try {
                e.printStackTrace();
                writer.writeObject("N");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }




    private void sendAllSellerProducts(String sortType, Seller seller) {
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
                synchronized (obj) {
                    products = this.market.sortBySales(true);
                }
            } else {
                synchronized (obj) {
                    products = this.market.getAllProducts(true);
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
//            System.out.println("blah");
            synchronized (obj) {
//                System.out.println("got here 1");
                searchResults = market.matchConditions(searchContents[0], searchContents[1],
                        searchContents[2]);
                System.out.println(searchResults);
            }
//            System.out.println("blah2");
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

    private void addToCart(Buyer buyer, int indexOfProduct, int quantity) {
        System.out.println("1");
        try {
            Product p;
            synchronized (obj) {
                p = this.market.getAllProducts(false).get(indexOfProduct);
            }
            if (p.getQuantity() < quantity + buyer.quantityInCart(indexOfProduct)) {
                //Error: quantity trying to add to cart is more than there are of that product
                this.writer.writeObject((String) "N");
                return;
            }
            synchronized (obj) {
                buyer.addProductToCart(p.getIndex(), quantity);
                market.updateAllFiles();
            }
            this.writer.writeObject((String) "Y");
            System.out.printf("Added product %d to cart\n", indexOfProduct);
            //Sends new shopping cart
        } catch (Exception e) {
            try {
                this.writer.writeObject((String) "N");
            } catch (Exception ex) {
                e.printStackTrace();
            }
        }
    }

    //This function sends back an ArrayList of all the strings the need to be in the exported file and sends it
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

    private void makePurchase(Buyer buyer) {
        try {
            synchronized (obj) {
                this.market.makePurchase(buyer);
                this.market.updateAllFiles();
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

    private void sendShoppingCart(Buyer buyer) {
        try {
            ArrayList<String> shoppingCart = null;
            synchronized (obj) {
                shoppingCart = buyer.getShoppingCart();
            }
            HashMap<Product, Integer> shoppingCartProducts = new HashMap<Product, Integer>();
            for (int i = 0; i < shoppingCart.size(); i++) {
                Product p = null;
                synchronized (obj) {
                    p = market.getProductByIndex(Integer.parseInt(shoppingCart.get(i).split(":")[0]));
                }
                int quantity = Integer.parseInt(shoppingCart.get(i).split(":")[1]);
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

    private void changeShoppingCartQuantity(int indexOfProduct, int newQuantity, Buyer buyer) {
        try {
            ArrayList<String> products = null;
            synchronized (obj) {
                products = buyer.getShoppingCart();
            }
            boolean exists = false;
            for (int i = 0; i < products.size(); i++) {
                if (indexOfProduct == Integer.parseInt(products.get(i).split(":")[0])) {
                    exists = true;
                    Product p = null;
                    synchronized (obj) {
                        p = market.getProductByIndex(Integer.parseInt(products.get(i).split(":")[0]));
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
                    p = this.market.getProductByIndex(idx);
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

    private void sendBuyerDashboard(Buyer buyer, String sortType) throws IOException {
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
    }

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

    private void seeBuyerCarts(Seller seller) {
        try {
            HashMap<Product, String> productsInCart = seller.sendProductsInCart(market);
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

    private void sendStores(Seller seller, String sortType) {
        try {
            ArrayList<String> stores = seller.getStoreNamesSorted(sortType);
            writer.writeObject(stores);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendProductStringsForFile(Seller seller, String storeName) throws IOException {
        if (!seller.getStoreNames().contains(storeName)) {
            writer.writeObject(null);
            return;
        }
        Store store = null;
        synchronized (obj) {
            store = seller.getStoreByName(storeName);
        }
        ArrayList<String> out = new ArrayList<>();
        for (Product p : store.getProducts()) {
            out.add(p.toString());
        }

        writer.writeObject(out);
    }

    private void importProductsFromFile(Seller seller, String[] lines) throws IOException {
        try {
            for (String line : lines) {
                synchronized (obj) {
                    Product newProduct = new Product(line);
                    if (seller.getStoreNames().contains(newProduct.getStoreName())) {
                        market.addProduct(newProduct);
                    }
                }
            }
            synchronized (obj) {
                market.updateAllFiles();
            }
            writer.writeObject("Y");
        } catch (Exception e) {
            writer.writeObject("N");
        }
    }

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

    private void sendProduct(String productName) {
        Product p;
        synchronized (obj) {
            p = market.getProductByName(productName);
        }
        System.out.println(p);

        if (p == null) {
            System.out.printf("failed to get '%s'\n", productName);
        }

        try {
            writer.writeObject(p);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //The constructor creates a new Server object for each connected client and gives it a unique socket and a
    // writer/reader to communicate with that client.
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
}
