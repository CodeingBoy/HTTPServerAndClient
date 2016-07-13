package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                try {
                    final Socket requestSocket = serverSocket.accept();
                    System.out.println("Got new request from " + requestSocket.getInetAddress() + ":" + requestSocket.getPort());
                    showContent(getRequestContent(requestSocket));
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

    public String getRequestContent(Socket requestSocket) {
        String content = new String("");

        // read request content
        try (InputStream inputStream = requestSocket.getInputStream()) {
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

    public static void showContent(String content){
        System.out.println("----------content----------");
        System.out.println(content);
        System.out.println("----------------------------");
    }

    public int getPort() {
        return port;
    }
}
