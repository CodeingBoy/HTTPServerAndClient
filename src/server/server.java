package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by CodeingBoy on 2016-7-13-0013.
 */
public class Server {
    int port;
    ServerSocket serverSocket;

    public Server(int port) {

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

        server.start();
        System.out.println("HTTP server listening on " + server.getPort());
    }

    public void start() {
        try  {
            serverSocket = new ServerSocket(port);
            while (true) {
                try {
                    final Socket requestSocket = serverSocket.accept();
                    System.out.println("Got new request from " + requestSocket.getInetAddress() + ":" + requestSocket.getPort());
                    processRequest(requestSocket);
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

    public void processRequest(Socket requestSocket) {

    }

    public int getPort() {
        return port;
    }
}
