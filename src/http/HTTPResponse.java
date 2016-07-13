package http;

import java.io.*;
import java.net.Socket;

/**
 * Created by CodeingBoy on 2016-7-13-0013.
 */
public class HTTPResponse {
    private String httpVersion;
    private int responseCode;
    private String responseCodeDescription;
    private String contentType;
    private InputStream stream;

    public HTTPResponse(String httpVersion, int responseCode, String responseCodeDescription, String contentType, InputStream stream) {
        this.httpVersion = httpVersion;
        this.responseCode = responseCode;
        this.responseCodeDescription = responseCodeDescription;
        this.contentType = contentType;
        if (stream != null)
            this.stream = stream;
        else
            try {
                this.stream = new FileInputStream(new File("root\\404.html"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
    }

    public InputStream getStream() {
        return stream;
    }

    public String getHeader() {
        final StringBuffer sb = new StringBuffer();
        sb.append(httpVersion + " " + responseCode + " " + responseCodeDescription + "\r\n");
        sb.append("Content-Type:" + contentType + "\r\n\r\n");
        return sb.toString();
    }

    public void send(Socket socket) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(getHeader().getBytes());

            while (stream.available() != 0)
                outputStream.write(stream.read());

            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return getHeader();
    }
}


