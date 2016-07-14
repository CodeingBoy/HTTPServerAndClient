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
    private byte[] content;

    public HTTPResponse(String httpVersion, int responseCode, String responseCodeDescription, String contentType, byte[] content) {
        this.httpVersion = httpVersion;
        this.responseCode = responseCode;
        this.responseCodeDescription = responseCodeDescription;
        this.contentType = contentType;
        if (content != null)
            this.content = content;
        else
            try {
                FileInputStream inputStream = new FileInputStream(new File("root\\404.html"));
                content = new byte[inputStream.available()];
                inputStream.read(content);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    // public static HTTPResponse parse(String content){
    //     String[] lines = content.split("\r\n");
    //     String[] firstLine = lines[0].split(" ");
    //     return new HTTPResponse(firstLine[2],firstLine[0],firstLine[1],)
    // }

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
            outputStream.write(content);

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


