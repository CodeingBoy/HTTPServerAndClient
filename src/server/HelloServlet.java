package server;

import http.HTTPRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by CodeingBoy on 2016-7-21-0021.
 */
public class HelloServlet implements Servlet {
    @Override
    public void init() throws Exception {
        System.out.println("Hello servlet init.");
    }

    @Override
    public String service(HTTPRequest request) {
        String requestURI = request.getRequestUri().toString();

        if (requestURI.indexOf("?") != -1) {
            String[] params = requestURI.split("\\?")[1].split("&");
            for (String param : params) {
                if (param.indexOf("username") != -1) {
                    try {
                        String userName = param.split("=")[1];
                        return "<h1>Hello " + URLDecoder.decode(userName, "UTF8") + "</h1>";
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }catch (ArrayIndexOutOfBoundsException e){
                        // return error
                    }

                }
            }
        }
        return "<h1>Error!</h1>";
    }
}
