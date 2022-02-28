import java.net.*;

public class WebServer {
    public static final int PORT = 2222;

    public static void main(String[] args){

        try{
            ServerSocket serverSock = new ServerSocket(PORT);
            System.out.println("Server is running...\n");

            while (true){
                Socket connectionSock = serverSock.accept();
                HttpRequest request = new HttpRequest(connectionSock);
                Thread thread = new Thread(request);
                thread.start();
            }
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
}

