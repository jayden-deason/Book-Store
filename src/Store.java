import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Store
 * <p>
 * A class describing a store that a seller owns and manages, which a buyer can purchase from.
 *
 * @author Griffin Chittenden, section 001
 * @version 11-09-2022
 */
public class Store implements java.io.Serializable {
    private int index; // Index of the store in the csv file
    private String storeName; // Name of the store
    private String sellerName; // Name of the seller who owns the store
    private ArrayList<Product> products; // Products that the store sells in Product form
    private ArrayList<Integer> productsByIndex; // The indices of the products the store sells
    private ArrayList<Product> productsForSales; // Sister list of salesForProducts to track by number of sales
    private ArrayList<Integer> salesForProducts; // Sister list of productsForSales to track by number of sales
    private int sales; // Total sales the store has done
    private double revenue; // Total revenue the store received
    private String productIndices; // The indices of the store's products as a string


    /**
     * Create a new empty Store with a name and seller
     *
     * @param index      index of the store in the csv
     * @param storeName  name of the store
     * @param sellerName name of the seller
     */
    public Store(int index, String storeName, String sellerName) {
        this(index, storeName, sellerName, 0, 0, "", "");
    }

    /**
     * Creates a new Store object with the given parameters
     *
     * @param index          the index of the given store in the Stores.csv file
     * @param storeName      the name of the store
     * @param sellerName     the name of the seller who owns the store
     * @param sales          the total number of sales the store has done
     * @param revenue        the total revenue from sales the store has done
     * @param productIndices the indices of the product file of the products the store has
     * @param productSales   the index of the product by the number of sales of the product
     */
    public Store(int index, String storeName, String sellerName, int sales, double revenue, String productIndices,
                 String productSales) {
        this.productIndices = productIndices;
        this.storeName = storeName;
        this.sellerName = sellerName;
        this.productsByIndex = new ArrayList<>();
        this.products = new ArrayList<>();
        this.productsForSales = new ArrayList<>();
        this.salesForProducts = new ArrayList<>();
        this.sales = sales;
        this.revenue = revenue;
        this.index = index;
    }

    /**
     * Creates a new store object given a line of the Stores.csv file
     *
     * @param fileLine the line of the Stores.csv file
     */
    public Store(String fileLine) {
        this.products = new ArrayList<>();
        this.productsByIndex = new ArrayList<>();
        this.productsForSales = new ArrayList<>();
        this.salesForProducts = new ArrayList<>();
        String[] split = fileLine.split(",");
        this.index = Integer.parseInt(split[0]);
        this.storeName = split[1];
        this.sellerName = split[2];
        this.productIndices = split[5];
        this.sales = Integer.parseInt(split[3]);
        this.revenue = Double.parseDouble(split[4]);
        String[] products = split[5].replace("<", "").replace(">", "").split("/");
        if (products.length == 1 && products[0].length() == 0) {
            products = new String[0];
        }
        for (String productIndex : products) {
            productsByIndex.add(Integer.parseInt(productIndex));
        }
    }

    /**
     * Imports a product file and adds its contents to the products sold by this store
     *
     * @param fileName the name of the file to import from
     */
    public void importProducts(String fileName) {
        try {
            File file = new File(fileName);
            BufferedReader bfr = new BufferedReader(new FileReader(file));
            for (String line = bfr.readLine(); line != null; line = bfr.readLine()) {
                String[] splitLine = line.split(",");
                Product product = new Product(line);
                products.add(product);
                productsByIndex.add(Integer.parseInt(splitLine[0]));
                productsForSales.add(product);
                salesForProducts.add(0);
            }
            bfr.close();
//            this.updateProducts();
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist.");
        } catch (IOException e) {
            System.out.println("Error reading from file.");
        }
    }

    /**
     * Get the list of product indices belonging to the store
     *
     * @return list of product indices
     */
    public ArrayList<Integer> getProductsByIndex() {
        return productsByIndex;
    }

    /**
     * Set the list of product indices
     *
     * @param productsByIndex a list of products by index
     */
    public void setProductsByIndex(ArrayList<Integer> productsByIndex) {
        this.productsByIndex = productsByIndex;
    }

    /**
     * Checks to see if the product the buyer wants to purchase has enough stock then updates the number of
     * purchases that buyer has made at this store and updates the quantity of the purchase product.
     *
     * @param buyer    the buyer who is making the purchase
     * @param quantity the amount of product being sold
     * @param product  the product being sold
     */
    public void makePurchase(Buyer buyer, int quantity, Product product) {
        if (product.getQuantity() < quantity) {
            throw new RuntimeException(String.format("Store only have %d %s left in stock\n", product.getQuantity(),
                    product.getName()));
//            System.out.printf("Store only have %d %s left in stock\n", product.getQuantity(), product.getName());
        } else {
            this.reReadProducts();
            if (productsForSales.indexOf(product) != -1) {
                salesForProducts.add(productsForSales.indexOf(product),
                        salesForProducts.get(productsForSales.indexOf(product)) + quantity);
            }
            sales += quantity;
            revenue += quantity * product.getSalePrice();
            product.setQuantity(product.getQuantity() - quantity);
//            this.updateProducts();
//            this.updateStores();
        }
    }

    /**
     * Adds a product to the list of products being sold by the store
     *
     * @param product the product being added to the store
     */
    public void addProduct(Product product, Market market) {
        if (!products.contains(product)) {
            products.add(product);
            productsByIndex.add(product.getIndex());
            productsForSales.add(product);
            if (market != null) {
                salesForProducts.add(market.getSalesForProduct(product));
            }
//            this.updateProducts();
//            this.updateStores();
//            this.reReadProducts();
        } else {
            System.out.println("Store already sells " + product.getName());
        }
    }

    /**
     * Check if duplicate product exists in local list
     *
     * @param p product
     * @return duplicate product
     */
    private Product findMatchingProduct(Product p) {
        for (Product product : products) {
            if (p.getName().equals(product.getName()) && p.getDescription().equals(product.getDescription())) {
                return product;
            }
        }

        return null;
    }

    /**
     * Removes a product from the list of products being sold by the store
     *
     * @param product the product being removed from the store
     */
    public void removeProduct(Product product) {
        Product p = findMatchingProduct(product);

        if (p != null && products.contains(p)) {
            products.remove(p);
            productsByIndex.remove(productsByIndex.indexOf(p.getIndex()));
//            salesForProducts.remove(productsForSales.indexOf(p));
            productsForSales.remove(p);
//            this.updateProducts();
//            this.updateStores();
//            this.reReadProducts();
        } else {
            System.out.println("Store does not sell " + product.getName());
        }
    }

    /**
     * Modifies a product that a store sells
     *
     * @param product the product being modified
     */
    public void modifyProduct(Product product) {
        for (Product p : products) {
            if (product.getIndex() == p.getIndex()) {
                products.set(products.indexOf(p), product);
                break;
            }
        }
//        this.updateProducts();
//        this.updateStores();
//        this.reReadProducts();
    }

    /**
     * Updates the products arraylist of this store to reflect changes made to the products.csv file
     */
    public void reReadProducts() {
        products.clear();
        try {
            File file = new File("Products.csv");
            BufferedReader bfr = new BufferedReader(new FileReader(file));
            for (String line = bfr.readLine(); line != null; line = bfr.readLine()) {
                String[] splitLine = line.split(",");
                if (productsByIndex.contains(Integer.parseInt(splitLine[0]))) {
                    products.add(new Product(line));
                }
            }
            bfr.close();
        } catch (IOException e) {
            System.out.println("File Error"); // Temporary message
        }
    }

    /**
     * Generates a string that contains the name of each buyer that has purchased a product from
     * this store and the quantity of products purchased.
     *
     * @return a string containing buyer sales information
     */
    public String generateCustomerData(Market market) {
        String retString = "";

        for (Buyer buyer : market.getBuyers()) { // Loop over the buyer data
            int purchases = 0;
            for (String purchase : buyer.getPurchaseHistory()) {
                Product p = market.getProductByIndex(Integer.parseInt(purchase.split(":")[0]));
                if (p.getStoreName().equals(this.getName())) {
//                    purchases += Integer.parseInt(purchase.split(":")[1]);
                    purchases++;
                }
            }
            if (purchases > 0) { // Checking if the buyer purchased anything from this store
                retString += buyer.getEmail() + ":" + purchases + "/";
            }

        }
        if (!retString.equals("")) {
            retString = retString.substring(0, retString.length() - 1);
        }
        return retString;
    }

    /**
     * Prints the store's statistics sorted based on how the seller wants
     *
     * @param sortType if sortType == 0, then it will not sort
     *                 if sortType == 1, then it will print everything ordered alphabetically
     *                 if sortType == 2, then it will print everything based on the quantity of products being dealt
     *                 wit
     */
    public ArrayList<String> statisticsForSeller(int sortType, Market market) {
        ArrayList<String> out = new ArrayList<>();
        String[] customerData = generateCustomerData(market).split("/");
        if (sortType > 2 || sortType < 0) {
            System.out.println("Sort type is invalid, pick a number from 0-2");
            return out;
        }
        // General statistics
        out.add(this.storeName + " Statistics:");
        out.add("--Total Sales: " + getSales(market));
        out.add(String.format("--Total Revenue: $%.2f", getRevenue()));
        if (sortType == 0) { // No sort condition
            out.add("--Products by sales: ");
            if (productsForSales.size() == 0) {
                out.add("----No products!");
            }
            for (Product product : productsForSales) {
                out.add("----" + product.getName() + ": " + market.getSalesForProduct(product));
            }
            out.add("--Sales by customer: ");
            if (customerData.length == 0 || (customerData.length == 1 && customerData[0].equals(""))) {
                out.add("----No customer purchase data!");
            }
            for (String data : customerData) {
                try {
                    out.add("----" + data.split(":")[0] + ": " + data.split(":")[1]);
                } catch (ArrayIndexOutOfBoundsException ignored) {

                }
            }
        } else {
            ArrayList<Product> sortedProducts = new ArrayList<Product>();
            for (Product product : products) {
                sortedProducts.add(product);
            }
            if (sortType == 1) { // Sorting alphabetically
                sortedProducts.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
            }
            if (sortType == 2) { // Sorting by number of sales
                sortedProducts.sort((p1, p2) -> Integer.compare(salesForProducts.get(productsForSales.indexOf(p1)),
                        salesForProducts.get(productsForSales.indexOf(p2))));
            }
            out.add("--Sales by product " + ((sortType == 1) ? "sorted alphabetically:" : "sorted by " +
                    "quantity:"));
            for (Product product : sortedProducts) {
                out.add("----" + product.getName() + ": " + products.get(products.indexOf(product)));
            }
            //ArrayList to track all of the buyers
            ArrayList<String> sortedBuyers = new ArrayList<>();
            for (String data : customerData) {
                sortedBuyers.add(data);
            }
            if (sortType == 1) {
                sortedBuyers.sort(Comparator.comparing(q -> q.substring(0, 1)));
                out.add("--Sales by customer sorted alphabetically:");
                for (String buyer : sortedBuyers) {
                    out.add("----" + buyer.split(":")[0] + ": " + buyer.split(":")[1]);
                }
            }
            if (sortType == 2) {
                int maxQuantity = 0;
                for (String buyer : sortedBuyers) {
                    if (Integer.parseInt(buyer.split(":")[1]) > maxQuantity) {
                        maxQuantity = Integer.parseInt(buyer.split(":")[1]);
                    }
                }
                System.out.println("--Sales by customer sorted by quantity:");
                for (int i = maxQuantity; i > 0; i--) {
                    for (String buyer : sortedBuyers) {
                        if (Integer.parseInt(buyer.split(":")[1]) == i) {
                            out.add("----" + buyer.split(":")[0] + ": " + buyer.split(":")[1]);
                        }
                    }

                }

            }

        }

        return out;

    }

    /**
     * Get the store's name
     *
     * @return name
     */
    public String getName() {
        return this.storeName;
    }

    /**
     * Set the store's name
     *
     * @param name new name
     */
    public void setName(String name) {
        this.storeName = name;
    }

    /**
     * Get the store owner (seller)'s name
     *
     * @return seller name
     */
    public String getSellerName() {
        return sellerName;
    }

    /**
     * Set the seller name
     *
     * @param sellerName new seller name
     */
    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    /**
     * Get the list of products in the store
     *
     * @return store products
     */
    public ArrayList<Product> getProducts() {
        return products;
    }

    /**
     * Get the total number of sales for products from this store
     *
     * @param market market to read buyer data from
     * @return number of sales for this store
     */
    public int getSales(Market market) {
        int out = 0;
        for (Product p : products) {
            out += market.getSalesForProduct(p);
        }
        return out;
    }

    /**
     * Set the number of sales for this store
     *
     * @param sales number of sales
     */
    public void setSales(int sales) {
        this.sales = sales;
    }

    /**
     * Get the revenue generated by this store
     *
     * @return revenue
     */
    public double getRevenue() {
        double revenue = 0;
        Market market = Market.getInstance();
        for (Buyer buyer : market.getBuyers()) {
            for (String item : buyer.getPurchaseHistory()) {
                Product product = market.getProductByIndex(Integer.parseInt(item.split(":")[0]));
                if (product.getStoreName().equals(this.getName())) {
                    revenue += product.getSalePrice() * Integer.parseInt(item.split(":")[1]);
                }
            }
        }
        return revenue;
    }

    /**
     * Set the store revenue
     *
     * @param revenue revenue
     */
    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
    // Need some way to track customer data

    /**
     * Get the index of the store in the csv
     *
     * @return index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Set the index of the store in the csv
     *
     * @param index new index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Return the string of product indices
     *
     * @return product indices
     */
    public String getProductIndices() {
        return productIndices;
    }

    /**
     * Used by the toString method to output in the format of the csv file
     *
     * @return productsForSales and salesForProduct in the format of the csv file
     */
    public String productsBySalesToString(Market market) {
        String retString = "<";
        for (Product product : productsForSales) {
            retString = retString + product.getIndex() + ":" + market.getSalesForProduct(product)
                    + "/";
        }
        if (productsForSales.size() == 0) {
            return retString + ">";
        }
        return retString.substring(0, retString.length() - 1) + ">";
    }

    /**
     * Used by the ToString method to return a string in the format of the csv file
     *
     * @return productsByIndex as a string in the format of the csv file
     */
    public String productsByIndexToString() {
        String retString = "<";

        for (int i = 0; i < products.size(); i++) {
            retString += products.get(i).getIndex();
            if (i < products.size() - 1) {
                retString += "/";
            }
        }

        return retString + ">";
    }

    /**
     * Get a string list of all product names
     *
     * @return a list of all product names
     */
    public ArrayList<String> getProductNames() {
        ArrayList<String> out = new ArrayList<>();

        for (Product p : products) {
            out.add(p.getName());
        }

        return out;
    }

    /**
     * Find a product in the product list by a given name
     *
     * @param name target name to match
     * @return product with that name if it exists, otherwise null
     */
    public Product getProductByName(String name) {
        for (Product p : products) {
            if (p.getName().equals(name)) {
                return p;
            }
        }

        return null;
    }

    /**
     * Get a string of products in user's carts from this user's stores
     *
     * @return a string of products in user's carts
     */
    public String getProductsInCarts() {
        Market market = Market.getInstance();
        String out = "";
        for (Buyer buyer : market.getBuyers()) {
            String buyerStr = "";
            for (String item : buyer.getShoppingCart()) {
                int idx = Integer.parseInt(item.split(":")[0]);
                if (sellingItem(idx)) {
                    buyerStr += market.getProductByIndex(idx).getName() + ": " + item.split(":")[1] + "<br />";

                }
            }

            if (!buyerStr.equals("")) {
                out += buyer.getEmail() + ":<br />" + buyerStr;
            }
        }

        return "<html>" + out + "</html>";
    }

    /**
     * Whether this seller is selling a specific product in one of their stores
     *
     * @param productIndex the index of the product
     * @return true if this seller sells the product, false if not
     */
    private boolean sellingItem(int productIndex) {
        for (Product p : getProducts()) {
            if (p.getIndex() == productIndex) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return a string representation of the store
     *
     * @return string for the store, use in csv
     */
    public String toString() {
        return String.format("%d,%s,%s,%d,%.2f,%s,%s", index, storeName, sellerName, sales,
                revenue, this.productsByIndexToString(), this.productsBySalesToString(Market.getInstance()));
    }
}