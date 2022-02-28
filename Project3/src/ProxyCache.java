import java.net.*;
import java.io.*;
import java.util.*;

public class ProxyCache {
    /** Port for the proxy */
    private static int port;
    /** Socket for client connections */
    private static ServerSocket socket;
    private static Socket client = null;

    public static HashMap<String, HttpResponse> cache = new HashMap<String, HttpResponse>();

    /** Create the ProxyCache object and the socket */
    public static void init(int p) {
        port = p;
        try {
            socket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error creating socket: " + e);
            System.exit(-1);
        }
    }

    /** Read command line arguments and start proxy */
    public static void main(String args[]) {
        int myPort = 0;

        try {
            myPort = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Need port number as argument");
            System.exit(-1);
        } catch (NumberFormatException e) {
            System.out.println("Please give port number as integer.");
            System.exit(-1);
        }

        System.out.println("Proxy server starts at port: " + myPort);
        init(myPort);

        /** Main loop. Listen for incoming connections and spawn a new
         * thread for handling them */

        while (true) {
            try {
                client = socket.accept();
                Thread thread = new Thread(new ThreadHandler(client));
                thread.run();

            } catch (IOException e) {
                System.out.println("Error reading request from client: " + e);
                /* Definitely cannot continue processing this request,
                 * so skip to next iteration of while loop. */
                continue;
            }
        }
    }
}

