import java.io.*;
import java.net.*;

public class ClientHttpRequest {

    // GET method
    public static String getWebContentByGet(String urlString, final String charset, int timeout) throws IOException {
        if (urlString == null || urlString.length() == 0) {
            return null;
        }
        urlString = (urlString.startsWith("http://") || urlString.startsWith("https://")) ?
                urlString : ("http://" + urlString).intern();
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        //TODO User-Agent dependent...
        conn.setRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.52727)");

        conn.setRequestProperty("Accept", "text/html");
        conn.setConnectTimeout(timeout);
        conn.connect();
        try {
            //TODO WHYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        InputStream input = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, charset));
        String line=  null;
        StringBuffer sb = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\r\n");
        }
        if (reader != null) {
            reader.close();
        }
        if (conn != null) {
            conn.disconnect();
        }
        return sb.toString();
    }

    // POST method
    public static String getWebContentByPost(String urlString, String data, final String charset, int timeout) throws IOException {
        if (urlString == null || urlString.length() == 0) {
            return null;
        }
        urlString = (urlString.startsWith("http://") || urlString.startsWith("https://")) ?
                urlString : ("http://" + urlString).intern();
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");

        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);

        connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");

        //TODO User-Agent dependent...
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows vista)");

        connection.setRequestProperty("Accept", "text/xml");
        connection.setConnectTimeout(timeout);
        connection.connect();

        // modified(added) by me
/*
		try {
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
*/
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());

        byte[] content = data.getBytes("UTF-8");

        out.write(content);
        out.flush();
        out.close();

        // modified(added) by me
        InputStream input = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, charset));
        String line = null;
        StringBuffer sb = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\r\n");
        }
        if (reader != null) {
            reader.close();
        }
        if (connection != null) {
            connection.disconnect();
        }
        return sb.toString();
    }
}
