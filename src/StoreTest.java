//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class StoreTest {
//    public static void main(String[] args) {
//        ArrayList<Store> stores = new ArrayList<>();
//        try {
//
//            File file = new File("Stores.csv");
//            BufferedReader bfr = new BufferedReader(new FileReader(file));
//            for (String line = bfr.readLine(); line != null; line = bfr.readLine()) {
//                String[] splitLine = line.split(",");
//                stores.add(new Store(Integer.parseInt(splitLine[0]), splitLine[1], splitLine[2], "Products.csv",
//                        Integer.parseInt(splitLine[3]), Integer.parseInt(splitLine[4])));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        for (Store store : stores) {
//            System.out.println(store);
//        }
//    }
//}
