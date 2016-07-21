package server;

import http.HTTPRequest;
import http.HTTPResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by CodeingBoy on 2016-7-13-0013.
 */
public class AdvancedServer {
    private static Map serveletCache = new HashMap();
    int port;
    ServerSocket serverSocket;

    public AdvancedServer(int port) {
        this.port = port;
    }

    public AdvancedServer() {
        this(8080);
    }

    public static void main(String[] args) {
        AdvancedServer server;
        if (args.length > 0)
            server = new AdvancedServer(Integer.parseInt(args[0]));
        else
            server = new AdvancedServer();

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

                    HTTPResponse response = null;
                    FileInputStream requestRes = null;

                    if (request.getRequestUri().toString().indexOf("servlet") != -1) {
                        response = new HTTPResponse(request.getHttpVersion(), 200, "OK",
                                "text/html;charset=utf-8", handleServlet(request).getBytes());
                    }else {
                        try {
                            requestRes = new FileInputStream(new File("root" + request.getRequestUri()));
                            byte[] content = new byte[requestRes.available()];
                            requestRes.read(content);
                            response = new HTTPResponse(request.getHttpVersion(), 200, "OK",
                                    "text/html", content);
                        } catch (FileNotFoundException e) {
                            try {
                                FileInputStream inputStream = new FileInputStream(new File("root\\404.html"));
                                byte[] content = new byte[inputStream.available()];
                                inputStream.read(content);
                                response = new HTTPResponse(request.getHttpVersion(), 404, "Not Found",
                                        "text/html", content);
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }

                        }
                    }
                    showContent("response", response.toString());
                    response.send(requestSocket);

                    requestSocket.close();
                    System.out.println("Disconnected.");
                    System.out.println();
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

    private String handleServlet(HTTPRequest request) {
        String servletName = null;
        final String requestURI = request.getRequestUri().toString();

        if (requestURI.indexOf("?") != -1) {
            servletName = requestURI.substring(requestURI.indexOf("servlet/") + 8, requestURI.indexOf("?"));
        } else {
            servletName = requestURI.substring(requestURI.indexOf("servlet/") + 8, requestURI.length());
        }

        Servlet servlet = (Servlet) serveletCache.get(servletName);

        if (servlet == null) {
            try {
                servlet = (Servlet) Class.forName("server." + servletName).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            try {
                servlet.init();
            } catch (Exception e) {
                e.printStackTrace();
            }
            serveletCache.put(servletName, servlet);
        }

        return servlet.service(request);
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

    public void sendBinary(Socket socket, InputStream binary) {
        try {
            OutputStream outputStream = socket.getOutputStream();

            while (binary.available() != 0)
                outputStream.write(binary.read());

            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return port;
    }
}
