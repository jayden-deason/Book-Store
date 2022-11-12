import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Store {
    private int index;
    private String storeName;
    private String sellerName;
    private ArrayList<Product> products;
    private ArrayList<Integer> productsByIndex;
    private HashMap<Buyer, Integer> customerData; // This way the sales for each buyer can be tracked
    private int sales;
    private double revenue;
    public Store(int index, String storeName, String sellerName, int sales, double revenue, String productIndices) {
        this.storeName = storeName;
        this.sellerName = sellerName;
        this.productsByIndex = new ArrayList<>();
        this.products = new ArrayList<>();
        this.customerData = new HashMap<>();
        String[] splitProducts = productIndices.split("/");
        for (String productIndex : splitProducts) {
            productsByIndex.add(Integer.parseInt(productIndex.split(":")[0]));
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
        } catch (IOException e) {
            System.out.println("File Error"); // Temporary message
        }
        this.sales = sales;
        this.revenue = revenue;
        this.index = index;
    }

    public Store(String fileLine) {
        String[] split = fileLine.split(",");
        this.index = Integer.parseInt(split[0]);
        this.storeName = split[1];
        this.sellerName = split[2];
        this.sales = Integer.parseInt(split[3]);
        this.revenue = Double.parseDouble(split[4]);
        String[] products = split[5].split("/");
        for (String productIndex : products) {
            productsByIndex.add(Integer.parseInt(productIndex.split(":")[0]));
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
        } catch (IOException e) {
            System.out.println("File Error"); // Temporary message
        }
    }

    public ArrayList<Integer> getProductsByIndex() {
        return productsByIndex;
    }

    public void setProductsByIndex(ArrayList<Integer> productsByIndex) {
        this.productsByIndex = productsByIndex;
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
            product.setQuantity(product.getQuantity() - quantity);
        }
    }

    public void addProduct(Product product) {
        if (!products.contains(product)) {
            products.add(product);
        } else {
            System.out.println("Store already sells " + product.getName());
        }
    }

    public void removeProduct(Product product) {
        if (products.contains(product)) {
            products.remove(product);
        } else {
            System.out.println("Store does not sell " + product.getName());
        }
    }

    public void modifyProduct(Product product) {
        for(Product p : products) {
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
            for (Product product : products) {
                System.out.println(product.getName() + ": " + products.get(products.indexOf(product)));
            }
            System.out.println("Sales by customer: ");
            for (Buyer buyer : customerData.keySet()) {
                System.out.println(buyer.getUsername() + ": " + customerData.get(buyer));
            }
        }
        else {
            ArrayList<Product> sortedProducts = new ArrayList<Product>();
            for (Product product : products) {
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
                System.out.println(product.getName() + ": " + products.get(products.indexOf(product)));
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
}
