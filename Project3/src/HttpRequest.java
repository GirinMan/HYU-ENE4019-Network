import java.io.*;
import java.net.*;
import java.util.*;

public class HttpRequest {
    /** Help variables */
    final static String CRLF = "\r\n";
    final static int HTTP_PORT = 80;
    /** Store the request parameters */
    String method;
    String URI;
    String version;
    String headers = "";
    /** Server and port */
    private String host;
    private int port;

    /** Create HttpRequest by reading it from the client socket */
    public HttpRequest(BufferedReader from) {
        String firstLine = "";
        String[] tmp;
        try {
            firstLine = from.readLine();
        } catch (IOException e) {
            System.out.println("Error reading request line: " + e);
        }

        if(firstLine != null)
        {
            System.out.println(firstLine);
            tmp = firstLine.split(" ");

            try{
                method = tmp[0];
                URI = tmp[1];
                version = tmp[2];

                System.out.println("URI is: " + URI);
            }
            catch(Exception e){
                //System.out.println("Error reading request line: " + e);
            }

            if (!method.equalsIgnoreCase("GET") && !method.equalsIgnoreCase("POST")) {
                System.out.println("Error: Method not GET or POST");
            }

            try {
                String line = from.readLine();
                if(line.length() != 0)
                {
                    while (line.length() != 0) {
                        headers += line + CRLF;
                        /* We need to find host header to know which server to
                         * contact in case the request URI is not complete. */
                        if (line.startsWith("Host:")) {
                            tmp = line.split(" ");
                            if (tmp[1].indexOf(':') > 0) {
                                String[] tmp2 = tmp[1].split(":");
                                host = tmp2[0];
                                port = Integer.parseInt(tmp2[1]);
                            } else {
                                host = tmp[1];
                                port = HTTP_PORT;
                            }
                        }

                        line = from.readLine();
                    }
                }
                else
                {
                    if (tmp[1].indexOf(':') > 0) {
                        String[] tmp2 = tmp[1].split(":");

                        if(tmp2[1].indexOf(':') > 0)
                        {
                            String[] tmp3 = tmp2[1].split(":");
                            URI = "/";
                            host = tmp3[0].substring(2);
                            port = Integer.parseInt(tmp3[1]);
                        }
                        else
                        {
                            URI = "/";
                            host = tmp2[1].substring(2);
                            port = HTTP_PORT;
                        }
                    } else {
                        URI = "/";
                        host = tmp[1];
                        port = HTTP_PORT;
                    }
                    headers += "Host: " + host + CRLF;
                    headers += "Port: " + port + CRLF;
                }

            } catch (IOException e) {
                System.out.println("Error reading from socket: " + e);
                return;
            }

            host = "192.168.21.248";
            port = 2222;
            System.out.println("Host to contact is: " + host + " at port " + port);
        }
    }

    /** Return host for which this request is intended */
    public String getHost() {
        return host;
    }

    /** Return port for server */
    public int getPort() {
        return port;
    }

    /**
     * Convert request into a string for easy re-sending.
     */
    public String toString() {
        String req = "";

        req = method + " " + URI + " " + version + CRLF;
        req += headers;
        /* This proxy does not support persistent connections */
        req += "Connection: close" + CRLF;
        req += CRLF;

        return req;
    }
}