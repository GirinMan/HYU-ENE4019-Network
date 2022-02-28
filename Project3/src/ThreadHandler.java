import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

// Does work for socket connections in a new thread.
public class ThreadHandler implements Runnable
{
    HashMap<String, HttpResponse> cache = ProxyCache.cache;
    Socket client = null;
    public ThreadHandler(Socket c)
    {
        client = c;
    }
    @Override
    public void run() {
        Socket server = null;
        HttpRequest request = null;
        HttpResponse response = null;

        /* Process request. If there are any exceptions, then simply
         * return and end this request. This unfortunately means the
         * client will hang for a while, until it timeouts. */

        /* Read request */
        try {
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            request = new HttpRequest(fromClient);
        } catch (IOException e) {
            System.out.println("Error reading request from client: " + e);
            return;
        }
        /* Send request to server */
        if(cache.get(request.toString()) != null)
        {
            try{
                response = cache.get(request.toString());

                DataOutputStream toClient = new DataOutputStream(client.getOutputStream());
                /* Fill in */
                /* Write response to client. First headers, then body */
                toClient.write(response.toString().getBytes());
                toClient.write(response.body);

                client.close();
            } catch (IOException e) {
                System.out.println("Error writing response to client: " + e);
            }
        }
        else
        {
            try {
                /* Open socket and write request to socket */
                server = new Socket(request.getHost(), request.getPort());
                DataOutputStream toServer = new DataOutputStream(server.getOutputStream());
                toServer.write(request.toString().getBytes());

            } catch (UnknownHostException e) {
                System.out.println("Unknown host: " + request.getHost());
                System.out.println(e);
                return;
            } catch (IOException e) {
                System.out.println("Error writing request to server: " + e);
                return;
            }

            /* Read response and forward it to client */
            try {
                DataInputStream fromServer = new DataInputStream(server.getInputStream());
                response = new HttpResponse(fromServer);
                DataOutputStream toClient = new DataOutputStream(client.getOutputStream());
                /* Fill in */
                /* Write response to client. First headers, then body */
                System.out.println(response.toString());
                toClient.write(response.toString().getBytes());
                toClient.write(response.bodyString.getBytes());


                client.close();
                server.close();
                /* Insert object into the cache */
                /* Fill in (optional exercise only) */
                // cache.put(request.toString(), response);
            } catch (IOException e) {
                System.out.println("Error writing response to client: " + e);
            }
        }

    }
}