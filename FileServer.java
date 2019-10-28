import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;
public class FileServer {

    private static ServerSocket serverSocket;
    private static Socket clientSocket = null;

    public static void main(String[] args) throws IOException {

        try {
            serverSocket = new ServerSocket(2020);
            System.out.println("server basladi");
        } catch (Exception e) {
            System.err.println("bu port kullanimda");
            System.exit(1);
        }
   
  
        while (true) {
            try {
                clientSocket = serverSocket.accept();
               System.out.println("Accepted connection : " + clientSocket);

                Thread t = new Thread(new ServiceClient(clientSocket));

                t.start();

            } catch (Exception e) {
                System.err.println("baglanti kapandi");
            }
        }
    }
}
