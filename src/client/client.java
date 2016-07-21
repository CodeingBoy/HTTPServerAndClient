package client;

import http.HTTPRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Created by CodeingBoy on 2016-7-13-0013.
 */
public class client {
    int port;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: URL [port]");
            System.out.println("Example: http://127.0.0.1:8080/index.html");
            return;
        }

        URL url = null;
        try {
            url = new URL(args[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println("Request URL: " + url);

        URI requestURI = null;
        try {
            requestURI = new URI(url.getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        System.out.println("Request URI: " + requestURI);

        Socket socket = null;
        try {
            socket = new Socket(url.getHost(), url.getPort() == -1 ? 80 : url.getPort());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HTTPRequest request = null;
        try {
            request = new HTTPRequest("GET", requestURI, "HTTP/1.1", InetAddress.getByName(null), socket.getLocalPort(), null);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        showContent("request", request.compose());

        request.send(socket);
        System.out.println("request sent.");

        showContent("response", getContent(socket));

        // HTTPResponse response = new HTTPResponse()
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showContent(String title, String content) {
        String head = "----------" + title + "----------";
        System.out.println(head);
        System.out.println(content);
        for (int i = 0; i < head.length(); i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    public static String getContent(Socket socket) {
        String content = new String("");

        // read request content
        try {
            InputStream inputStream = socket.getInputStream();
            while (inputStream.available() == 0)
                Thread.sleep(100);  // wait until got content

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            while (reader.ready()) {
                content += reader.readLine() + "\r\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return content;
    }
}
