import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Store {
    private String storeName;
    private String sellerName;
    private String productFile;
    private HashMap<Product, Integer> products; // This way the sales can be tracked for each individual product
    private HashMap<Buyer, Integer> customerData; // This way the sales for each buyer can be tracked
    private int sales;
    private int revenue;
    public Store(String storeName, String sellerName, String productFile, int sales, int revenue) {
        this.storeName = storeName;
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
                        Double.parseDouble(splitLine[4])), 0);
            }
        } catch (IOException e) {
            System.out.println("File Error"); // Temporary message
        }
        this.sales = sales;
        this.revenue = revenue;
    }

    public void makePurchase(Buyer buyer, int quantity, Product product) {
        if (product.getQuantity() < quantity) {
            System.out.printf("Store only have %d %s left in stock\n", product.getQuantity(), product.getName());
        } else {
            if (customerData.containsKey(buyer)) {
                customerData.compute(buyer, (k, v) -> v + quantity);
            } else {
                customerData.put(buyer, quantity);
            }
            products.compute(product, (k, v) -> v - quantity);
            product.setQuantity(product.getQuantity() - quantity);
        }
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

    public void statisticsForBuyer() {
        System.out.println("Store: " + name);
        System.out.println("Total Sales: " + sales);
    }
    public void statisticsForSeller() {
        System.out.println(name + " Statistics:");
        System.out.println("Total Sales: " + sales);
        System.out.println("Total Revenue: " + revenue);
        System.out.println("Sales by product: ");
        for (Product product : products.keySet()) {
            System.out.println(product.getName() + ": " + products.get(product));
        }
        System.out.println("Sales by customer: ");
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
