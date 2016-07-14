package http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.Arrays;

/**
 * Created by CodeingBoy on 2016-7-13-0013.
 */
public class HTTPRequest {
    private String[] lines;
    private String requestType;
    private URI requestUri;
    private String httpVersion;
    private InetAddress host;
    private int port;
    private String content;

    public HTTPRequest(String[] contents) {
        this.lines = contents;

        String[] firstlines = lines[0].split(" ");
        requestType = firstlines[0];
        try {
            requestUri = new URI(firstlines[1]);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        httpVersion = firstlines[2];

        try {
            host = InetAddress.getByName(lines[1].split(" ")[1].split(":")[0]);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        port = Integer.parseInt(lines[1].split(" ")[1].split(":")[1]);
    }

    public HTTPRequest(String requestType, URI requestUri, String httpVersion, InetAddress host, int port, String content) {
        this.requestType = requestType;
        this.requestUri = requestUri;
        this.httpVersion = httpVersion;
        this.host = host;
        this.port = port;
        this.content = content;
    }

    public HTTPRequest(String content) {
        this(content.split("\r\n"));
    }

    public URI getRequestUri() {
        return requestUri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public InetAddress getHost() {
        return host;
    }

    public String getRequestType() {
        return requestType;
    }

    public String[] getHeader() {
        int i = 1;
        while (i < lines.length && !lines[i].equals(""))
            i++;

        String[] header = new String[i - 2];

        System.arraycopy(lines, 1, header, 0, i - 2);
        return header;
    }

    public String compose() {
        final StringBuffer stringBuffer = new StringBuffer(requestType + " ");
        stringBuffer.append(requestUri + " ");
        stringBuffer.append(httpVersion + "\r\n");
        stringBuffer.append("Host: " + host.getHostAddress() + ":" + port + "\r\n");
        if (content != null) {
            stringBuffer.append("\r\n");
            stringBuffer.append(content);
        }
        return stringBuffer.toString();
    }

    public void send(Socket socket) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(compose().getBytes());

            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("HTTPRequest{");
        sb.append("lines=").append(lines == null ? "null" : Arrays.asList(lines).toString());
        sb.append(", requestType='").append(requestType).append('\'');
        sb.append(", requestUri=").append(requestUri);
        sb.append(", httpVersion='").append(httpVersion).append('\'');
        sb.append(", host=").append(host);
        sb.append(", port=").append(port);
        sb.append('}');
        return sb.toString();
    }
}
