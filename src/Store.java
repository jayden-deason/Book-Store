import com.sun.jdi.ArrayReference;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Store
 * <p>
 * A class describing a store that a seller owns and manages, which a buyer can purchase from.
 *
 * @author Griffin Chittenden, section 001
 * @version 11-09-2022
 */
public class Store {
    private int index;
    private String storeName;
    private String sellerName;
    private ArrayList<Product> products;
    private ArrayList<Integer> productsByIndex;
    private ArrayList<Product> productsForSales;
    private ArrayList<Integer> salesForProducts;
    private int sales;
    private double revenue;
    private String productIndices;

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
        String[] splitProducts = productIndices.replace("<", "").replace(">", "").split("/");
        for (String productIndex : splitProducts) {
            productsByIndex.add(Integer.parseInt(productIndex));
        }
        try {
            File file = new File("Products.csv");
            BufferedReader bfr = new BufferedReader(new FileReader(file));
            for (String line = bfr.readLine(); line != null; line = bfr.readLine()) {
                String[] splitLine = line.split(",");
                if (productsByIndex.contains(Integer.parseInt(splitLine[0]))) {
                    products.add(new Product(splitLine[1], splitLine[2], splitLine[3], Integer.parseInt(splitLine[4]),
                            Double.parseDouble(splitLine[5]), Integer.parseInt(splitLine[0])));
                }
            }
            bfr.close();
        } catch (IOException e) {
            System.out.println("File Error"); // Temporary message
        }
        String[] splitProductsBySales = productSales.replace("<", "").replace(">", "").split("/");
        for (String productIndex : splitProductsBySales) {
            for (Product product : products) {
                if (product.getIndex() == Integer.parseInt(productIndex.split(":")[0])) {
                    productsForSales.add(product);
                    salesForProducts.add(Integer.parseInt(productIndex.split(":")[1]));
                }
            }
        }
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
        for (String productIndex : products) {
            productsByIndex.add(Integer.parseInt(productIndex));
        }

        try {
            File file = new File("Products.csv");
            BufferedReader bfr = new BufferedReader(new FileReader(file));
            for (String line = bfr.readLine(); line != null; line = bfr.readLine()) {
                String[] splitLine = line.split(",");
                if (productsByIndex.contains(Integer.parseInt(splitLine[0]))) {
                    this.products.add(new Product(splitLine[1], splitLine[2], splitLine[3], Integer.parseInt(splitLine[4]),
                            Double.parseDouble(splitLine[5]), Integer.parseInt(splitLine[0])));
                }
            }
            bfr.close();
        } catch (IOException e) {
            System.out.println("File Error"); // Temporary message
        }
        String[] splitProductsBySales = split[6].replace("<", "").replace(">", "").split("/");
        for (String productIndex : splitProductsBySales) {
            for (Product product : this.products) {
                if (product.getIndex() == Integer.parseInt(productIndex.split(":")[0])) {
                    productsForSales.add(product);
                    salesForProducts.add(Integer.parseInt(productIndex.split(":")[1]));
                }
            }
        }
    }

    public void importProducts(String fileName) {
        try {
            File file = new File(fileName);
            BufferedReader bfr = new BufferedReader(new FileReader(file));
            for (String line = bfr.readLine(); line != null; line = bfr.readLine()) {
                String[] splitLine = line.split(",");
                Product product = new Product(splitLine[1], splitLine[2], splitLine[3], Integer.parseInt(splitLine[4]),
                        Double.parseDouble(splitLine[5]), Integer.parseInt(splitLine[0]));
                products.add(product);
                productsByIndex.add(Integer.parseInt(splitLine[0]));
                productsForSales.add(product);
                salesForProducts.add(0);
            }
            bfr.close();
            this.updateProducts();
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist.");
        } catch (IOException e) {
            System.out.println("Error reading from file.");
        }
    }

    public void updateStores() {
        BufferedReader bfr;
        ArrayList<String> storeFile = new ArrayList<>();
        try {
            bfr = new BufferedReader(new FileReader(new File("Stores.csv")));
            for (String line = bfr.readLine(); line != null; line = bfr.readLine()) {
                if (Integer.parseInt(line.split(",")[0]) == this.index) {
                    storeFile.add(this.toString());
                } else {
                    storeFile.add(line);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Could not find file!");
        } catch (IOException e) {
            System.out.println("Could not read from file!");
        }
        try {
            PrintWriter pw = new PrintWriter("Stores.csv");
            for (String store : storeFile) {
                pw.write(store + "\n");
            }
            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file.");
        } catch (IOException e) {
            System.out.println("Could not write to file.");
        }
    }

    public void updateProducts() {
        BufferedReader bfr;
        ArrayList<String> productFile = new ArrayList<>();
        try {
            bfr = new BufferedReader(new FileReader(new File("Products.csv")));
            for (String line = bfr.readLine(); line != null; line = bfr.readLine()) {
                for (Product product : products) {
                    if (product.getIndex() == Integer.parseInt(line.split(",")[0])) {
                        productFile.add(product.toString());
                    } else {
                        productFile.add(line);
                    }
                    break;
                }
            }
            bfr.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file!");
        } catch (IOException e) {
            System.out.println("Could not read from file!");
        }
        try {
            PrintWriter pw = new PrintWriter("Products.csv");
            for (String product : productFile) {
                pw.write(product + "\n");

            }
            pw.close();
            productFile.clear();
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file.");
        } catch (IOException e) {
            System.out.println("Could not write to file.");
        }
    }

    public ArrayList<Integer> getProductsByIndex() {
        return productsByIndex;
    }

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
            System.out.printf("Store only have %d %s left in stock\n", product.getQuantity(), product.getName());
        } else {
            this.reReadProducts();
            salesForProducts.set(productsForSales.indexOf(product),
                    salesForProducts.get(productsForSales.indexOf(product)) + quantity);
            sales += quantity;
            revenue += quantity * product.getPrice();
            products.get(products.indexOf(product)).setQuantity(product.getQuantity() - quantity);
            this.updateProducts();
            this.updateStores();
        }
    }

    /**
     * Adds a product to the list of products being sold by the store
     *
     * @param product the product being added to the store
     */
    public void addProduct(Product product) {
        if (!products.contains(product)) {
            products.add(product);
            productsByIndex.add(product.getIndex());
            productsForSales.add(product);
            salesForProducts.add(0);
            this.updateProducts();
            this.updateStores();
//            this.reReadProducts();
        } else {
            System.out.println("Store already sells " + product.getName());
        }
    }

    /**
     * Removes a product from the list of products being sold by the store
     *
     * @param product the product being removed from the store
     */
    public void removeProduct(Product product) {
        if (products.contains(product)) {
            products.remove(product);
            productsByIndex.remove(productsByIndex.indexOf(product.getIndex()));
            salesForProducts.remove(productsForSales.indexOf(product));
            productsForSales.remove(product);
            this.updateProducts();
            this.updateStores();
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
        this.updateProducts();
        this.updateStores();
//        this.reReadProducts();
    }

    public void reReadProducts() {
        products.clear();
        try {
            File file = new File("Products.csv");
            BufferedReader bfr = new BufferedReader(new FileReader(file));
            for (String line = bfr.readLine(); line != null; line = bfr.readLine()) {
                String[] splitLine = line.split(",");
                if (productsByIndex.contains(Integer.parseInt(splitLine[0]))) {
                    products.add(new Product(splitLine[1], splitLine[2], splitLine[3], Integer.parseInt(splitLine[4]),
                            Double.parseDouble(splitLine[5]), Integer.parseInt(splitLine[0])));
                }
            }
            bfr.close();
        } catch (IOException e) {
            System.out.println("File Error"); // Temporary message
        }
    }

    public String generateCustomerData() {
        String retString = "";
        ArrayList<String> customerDataList = new ArrayList<>();
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("Customers.csv"));
            for (String line = bfr.readLine(); line != null; line = bfr.readLine()) {
                customerDataList.add(line.split(",")[1] + "=" + line.split(",")[4].replace("<", "").replace(">", "").trim());
            }
            bfr.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error reading file.");
        } catch (IOException e) {
            System.out.println("IO error");
        }
        for (int i = 0; i < customerDataList.size(); i++) {
            String[] firstSplit = customerDataList.get(i).split("=");
            if (firstSplit.length > 1) {
                int purchases = 0;
                String buyerName = firstSplit[0];
                String[] secondSplit = firstSplit[1].split("/");
                for (String split : secondSplit) {
                    String[] thirdSplit = split.split(":");
                    if (!(split.isEmpty()) && productsByIndex.contains(Integer.parseInt(thirdSplit[0]))) {
                        purchases += Integer.parseInt(thirdSplit[1]);
                    }
                }
                if (purchases > 0) {
                    retString += buyerName + ":" + purchases + "/";
                }
            }
        }
        return retString.substring(0, retString.length() - 1);
    }

    /**
     * Prints the store's statistics sorted based on how the seller wants
     *
     * @param sortType if sortType == 0, then it will not sort
     *                 if sortType == 1, then it will print everything ordered alphabetically
     *                 if sortType == 2, then it will print everything based on the quantity of products being dealt
     *                 wit
     */
    public void statisticsForSeller(int sortType) {
        String[] customerData = generateCustomerData().split("/");
        if (sortType > 2 || sortType < 0) {
            System.out.println("Sort type is invalid, pick a number from 0-2");
            return;
        }
        System.out.println(this.storeName + " Statistics:");
        System.out.println("Total Sales: " + sales);
        System.out.println("Total Revenue: " + revenue);
        if (sortType == 0) {
            System.out.println("Products by sales: ");
            for (Product product : productsForSales) {
                System.out.println(product.getName() + ": " + salesForProducts.get(productsForSales.indexOf(product)));
            }
            System.out.println("Sales by customer: ");
            for (String data : customerData) {
                System.out.println(data.split(":")[0] + ": " + data.split(":")[1]);
            }
        } else {
            ArrayList<Product> sortedProducts = new ArrayList<Product>();
            for (Product product : products) {
                sortedProducts.add(product);
            }
            if (sortType == 1) {
                sortedProducts.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
            }
            if (sortType == 2) {
                sortedProducts.sort((p1, p2) -> Integer.compare(salesForProducts.get(productsForSales.indexOf(p1)),
                        salesForProducts.get(productsForSales.indexOf(p2))));
            }
            System.out.println("Sales by product " + ((sortType == 1) ? "sorted alphabetically:" : "sorted by " +
                    "quantity:"));
            for (Product product : sortedProducts) {
                System.out.println(product.getName() + ": " + products.get(products.indexOf(product)));
            }
            //ArrayList to track all of the buyers
            ArrayList<String> sortedBuyers = new ArrayList<>();
            for (String data : customerData) {
                sortedBuyers.add(data);
            }
            if (sortType == 1) {
                sortedBuyers.sort(Comparator.comparing(q -> q.substring(0, 1)));
                System.out.println("Sales by customer sorted alphabetically:");
                for (String buyer : sortedBuyers) {
                    System.out.println(buyer.split(":")[0] + ": " + buyer.split(":")[1]);
                }
            }
            if (sortType == 2) {
                int maxQuantity = 0;
                for (String buyer : sortedBuyers) {
                    if (Integer.parseInt(buyer.split(":")[1]) > maxQuantity) {
                        maxQuantity = Integer.parseInt(buyer.split(":")[1]);
                    }
                }
                System.out.println("Sales by customer sorted by quantity:");
                for (int i = maxQuantity; i > 0; i--) {
                    for (String buyer : sortedBuyers) {
                        if (Integer.parseInt(buyer.split(":")[1]) == i) {
                            System.out.println(buyer.split(":")[0] + ": " + buyer.split(":")[1]);
                        }
                    }

                }

            }

        }

    }


    public String getName() {
        return this.storeName;
    }

    public void setName(String name) {
        this.storeName = name;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
    // Need some way to track customer data

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getProductIndices() {
        return productIndices;
    }

    public String productsBySalesToString() {
        String retString = "<";
        for (Product product : productsForSales) {
            retString = retString + product.getIndex() + ":" + salesForProducts.get(productsForSales.indexOf(product))
                    + "/";
        }
        return retString.substring(0, retString.length() - 1) + ">";
    }

    public String productsByIndexToString() {
        String retString = "<";
        for (Integer index : productsByIndex) {
            retString = retString + index + "/";
        }
        return retString.substring(0, retString.length() - 1) + ">";
    }

    public String toString() {
        return String.format("%d,%s,%s,%d,%.2f,%s,%s", index, storeName, sellerName, sales,
                revenue, this.productsByIndexToString(), this.productsBySalesToString());
    }
}