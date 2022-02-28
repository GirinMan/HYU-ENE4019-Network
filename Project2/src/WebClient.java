import java.io.IOException;
import java.util.Scanner;

public class WebClient {

    public static final String CHARSET = "UTF-8";
    public static final int TIMEOUT = 5000;

    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);
        String method = null;
        String url = null;
        String data = null;
        String responseMessage = null;

        while (true) {
            System.out.print("method : ");

            method = keyboard.nextLine();

            if (method.equalsIgnoreCase("Q") || method.equalsIgnoreCase("QUIT")
                    || method.equalsIgnoreCase("E") || method.equalsIgnoreCase("EXIT")) {
                System.out.println("Exit...");
                keyboard.close();
                return;
            }
            else if (method.equalsIgnoreCase("GET")) {
                System.out.print("url : ");
                url = keyboard.nextLine();

                try {
                    responseMessage = ClientHttpRequest.getWebContentByGet(url, CHARSET, TIMEOUT);
                } catch (IOException e) {
                    e.printStackTrace();
                    keyboard.close();
                    return;
                }

                System.out.println(responseMessage);
            }
            else if (method.equalsIgnoreCase("POST")) {
                System.out.print("url : ");
                url = keyboard.nextLine();
                System.out.print("data : ");
                data = keyboard.nextLine();

                try {
                    responseMessage = ClientHttpRequest.getWebContentByPost(url, data, CHARSET, TIMEOUT);
                } catch (IOException e) {
                    e.printStackTrace();
                    keyboard.close();
                    return;
                }

                System.out.println(responseMessage);
            }
            else {
                System.out.println("Try again.");
            }
        }
    }
}
