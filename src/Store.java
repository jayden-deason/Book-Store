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
    private int index;
    public Store(String storeName, String sellerName, String productFile, int sales, int revenue, int index) {
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
                        Double.parseDouble(splitLine[4]), Integer.parseInt(splitLine[5])), 0);
            }
        } catch (IOException e) {
            System.out.println("File Error"); // Temporary message
        }
        this.sales = sales;
        this.revenue = revenue;
        this.index = index;
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

    // Checks through HashMap to find the same product by name and then changes it to the argument
    public void modifyProduct(Product product) {
        for(Product p : products.keySet()) {
            if(product.getName().equals(p.getName())) {
                p = product;
            }
        }
    }

    public void statisticsForBuyer() {
        System.out.println("Store: " + this.storeName);
        System.out.println("Total Sales: " + this.sales);
    }
    /**
     *
     * Prints the store's statistics sorted based on how the seller wants
     * @param sortType if sortType == 0, then it will not sort
     *                 if sortType == 1, then it will print everything ordered alphabetically
     *                 if sortType == 2, then it will print everything based on the quantity of products being dealt
     *                 wit
     */
    public void statisticsForSeller(int sortType) {
        if(sortType > 2 || sortType < 0) {
            System.out.println("Sort type is invalid, pick a number from 0-2");
            return;
        }
        System.out.println(this.storeName + " Statistics:");
        System.out.println("Total Sales: " + sales);
        System.out.println("Total Revenue: " + revenue);
        if(sortType == 0) {
            System.out.println("Sales by product: ");
            for (Product product : products.keySet()) {
                System.out.println(product.getName() + ": " + products.get(product));
            }
            System.out.println("Sales by customer: ");
            for (Buyer buyer : customerData.keySet()) {
                System.out.println(buyer.getUsername() + ": " + customerData.get(buyer));
            }
        }
        else {
            ArrayList<Product> sortedProducts = new ArrayList<Product>();
            for (Product product : products.keySet()) {
                sortedProducts.add(product);
            }
            if(sortType == 1) {
                sortedProducts.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
            }
            if(sortType == 2) {
                sortedProducts.sort((p1,p2) -> Integer.compare(p1.getQuantity(), p2.getQuantity()));
            }
            System.out.println("Sales by product " + ((sortType == 1) ? "sorted alphabetically:" : "sorted by " +
                    "quantity:"));
            for (Product product : sortedProducts) {
                System.out.println(product.getName() + ": " + products.get(product));
            }
            //ArrayList to track all of the buyers
            ArrayList<Buyer> sortedBuyers = new ArrayList<Buyer>();
            for (Buyer buyer : customerData.keySet()) {
                sortedBuyers.add(buyer);
            }
            if(sortType == 1) {
                sortedBuyers.sort((q1, q2) -> q1.getUsername().compareTo(q2.getUsername()));
                System.out.println("Sales by customer sorted alphabetically:");
                for (Buyer buyer : sortedBuyers) {
                    System.out.println(buyer.getUsername() + ": " + customerData.get(buyer));
                }
            }
            if(sortType == 2) {
                int maxQuantity = 0;
                for (Buyer buyer : sortedBuyers) {
                    if(customerData.get(buyer) > maxQuantity) {
                        maxQuantity = customerData.get(buyer);
                    }
                }
                System.out.println("Sales by customer sorted by quantity:");
                for (int i = maxQuantity; i > 0; i--) {
                    for (Buyer buyer : sortedBuyers) {
                        if(customerData.get(buyer) == i) {
                            System.out.println(buyer.getUsername() + ": " + customerData.get(buyer));
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
