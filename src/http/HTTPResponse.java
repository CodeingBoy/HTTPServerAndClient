package http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        this.stream = stream;
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

            while (stream != null && stream.available() != 0)
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


