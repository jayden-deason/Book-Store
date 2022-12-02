import java.io.*;
import java.net.*;
import java.util.ArrayList;
public class Server extends Thread{
    private final Socket socket;
    public void run(Socket s) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
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
}
