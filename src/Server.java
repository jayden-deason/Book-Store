import java.io.*;
import java.net.*;
import java.util.ArrayList;
public class Server extends Thread{
    private final Socket socket;
    public static Market market;
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            PrintWriter writer = new PrintWriter(this.socket.getOutputStream());
            //loginString format: (0 for seller or 1 for buyer),(0 for sign in or 1 for sign up),email,password
            //Example: "0,1,test123@t.com,123" should be sign up for seller with email:test123@t.com and password 123
            String loginString = reader.readLine();
            String[] userDetails = loginString.split(",");
            //Seller
            if(userDetails[0].equals("0")) {
                if(userDetails[1].equals("0")) {
                    Buyer b = market.getBuyerByEmail(userDetails[2]);
                    if(b == null) {
                        //Sends Client "N" to signify an error (Username is wrong)
                        writer.println("N");
                    }
                    else if(b.getPassword().equals(userDetails[3])) {
                        //Sends Client "Y" to signify logged in correctly
                        writer.println("Y");
                        runBuyer(reader, writer);
                    }
                    else{
                        //Sends Client "N" to signify an error (Password is wrong)
                        writer.println("N");
                    }
                }
                if(userDetails[1].equals("1")) {
                    //checks if email already exists in marketplace
                    Buyer b = market.getBuyerByEmail(userDetails[2]);
                    if(b == null) {
                        Buyer buyer = new Buyer(userDetails[2], userDetails[3]);
                        market.addBuyer(buyer);
                        writer.println("Y");
                        runBuyer(reader, writer);
                    }
                    else {
                        //Sends Client "N" to signify an error (Username already exists)
                        writer.println("N");
                    }
                }
            }
            //Buyer
            else if(userDetails[0].equals("1")) {
                if(userDetails[1].equals("0")) {
                    Seller s = market.getSellerByEmail(userDetails[2]);
                    if(s == null) {
                        //Sends Client "N" to signify an error (Username is wrong)
                        writer.println("N");
                    }
                    else if(s.getPassword().equals(userDetails[3])) {
                        //Sends Client "Y" to signify logged in correctly
                        writer.println("Y");
                    }
                    else{
                        //Sends Client "N" to signify an error
                        writer.println("N");
                    }
                }
                if(userDetails[1].equals("1")) {
                    //checks if email already exists in marketplace
                    Seller s = market.getSellerByEmail(userDetails[2]);
                    if(s == null) {
                        Seller seller = new Seller(userDetails[2], userDetails[3]);
                        market.addBuyer(seller);
                        writer.println("Y");
                        runSeller(reader, writer);
                    }
                    else {
                        //Sends Client "N" to signify an error (Username already exists)
                        writer.println("N");
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //log in or sign up
        while(true){
            //infinitely waits for user to make requests to access data and sends data back to user
        }
    }
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException{
        //Port Number is 1001 and host is "localhost"
        ServerSocket serverSocket = new ServerSocket(1001);
        while(true) {
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
    }
    public static void runBuyer(BufferedReader reader, PrintWriter writer) {
        while(true) {

        }
    }
    public static void runSeller(BufferedReader reader, PrintWriter writer) {
        while(true) {

        }
    }
}
