import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Store {
    private String name;
    private String sellerName;
    private String productFile;
    private HashMap<Product, Integer> products; // This way the sales can be tracked for each individual product
    private HashMap<Buyer, Integer> customerData; // This way the sales for each buyer can be tracked
    private int sales;
    private int revenue;

    public Store(String name, String sellerName, String productFile, int sales, int revenue) {
        this.name = name;
        this.sellerName = sellerName;
        this.productFile = productFile;
        products = new HashMap<>();
        customerData = new HashMap<>();
        try {
            File file = new File(productFile);
            BufferedReader bfr = new BufferedReader(new FileReader(file));
            for (String line = bfr.readLine(); line != null; line = bfr.readLine()) {
                String[] splitLine = line.split(",");
                products.put(new Product(splitLine[0], splitLine[1], splitLine[2], Integer.parseInt(splitLine[3]),
                        Double.parseDouble(splitLine[4]), 0), 0);
            }
        } catch (IOException e) {
            System.out.println("File Error"); // Temporary message
        }
        this.sales = sales;
        this.revenue = revenue;
    }

    public Store(String line) {
        // store constructor from line in file
    }

    public void writeToFile(String storeFile) {

    }

    public void addBuyer(Buyer buyer) {
        customerData.put(buyer, 1);
    }

    public void purchaseMade(Buyer buyer, int quantity) {
        customerData.computeIfPresent(buyer, (k, v) -> v + quantity);
    }

    public void addProduct(Product product) {
        if (!products.containsKey(product)) {
            products.put(product, 1);
        } else {
            System.out.println("Store already sells " + product.getName());
        }
    }

    public void removeProduct(Product product) {
        if (products.containsKey(product)) {
            products.remove(product);
        } else {
            System.out.println("Store does not sell " + product.getName());
        }
    }

    // Not sure the best way to handle this case
    public void modifyProduct(Product product) {

    }

    public void displayStatistics() {
        for (Product product : products.keySet()) {
            System.out.println(product.getName() + ": " + products.get(product));
        }
        for (Buyer buyer : customerData.keySet()) {
            System.out.println(buyer.getUsername() + ": " + customerData.get(buyer));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getProductFile() {
        return productFile;
    }

    public void setProductFile(String productFile) {
        this.productFile = productFile;
    }

    public HashMap<Product, Integer> getProducts() {
        return products;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }
    // Need some way to track customer data


}
