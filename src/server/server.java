package server;

import http.HTTPRequest;
import http.HTTPResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by CodeingBoy on 2016-7-13-0013.
 */
public class Server {
    int port;
    ServerSocket serverSocket;

    public Server(int port) {
        this.port = port;
    }

    public Server() {
        this(8080);
    }

    public static void main(String[] args) {
        Server server;
        if (args.length > 0)
            server = new Server(Integer.parseInt(args[0]));
        else
            server = new Server();

        System.out.println("HTTP server running on " + server.getPort());
        System.out.println("Server have not started yet. Waiting for being started.");

        System.out.println("HTTP server listening on " + server.getPort());
        server.start();
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

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                try {
                    final Socket requestSocket = serverSocket.accept();
                    System.out.println("Got new request from " + requestSocket.getInetAddress() + ":" + requestSocket.getPort());

                    String requestStr = getContent(requestSocket);

                    showContent("origin content", requestStr);

                    HTTPRequest request = new HTTPRequest(requestStr);
                    System.out.println("Request Type: " + request.getRequestType());
                    System.out.println("Request URI: " + request.getRequestUri());
                    System.out.println("Request host's HTTP Ver: " + request.getHttpVersion());
                    System.out.println("Request host: " + request.getHost());
                    System.out.println("Request header: ");
                    for (String line : request.getHeader()) {
                        System.out.println(line);
                    }

                    HTTPResponse response = new HTTPResponse(request.getHttpVersion(), 200, "OK", "text/txt", System.in);
                    showContent("response", response.toString());
                    sendContent(requestSocket, response.toString());

                    requestSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getContent(Socket socket) {
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

    public void sendContent(Socket socket, String content) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(content.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return port;
    }
}
