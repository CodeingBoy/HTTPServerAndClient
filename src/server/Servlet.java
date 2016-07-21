package server;

import http.HTTPRequest;

/**
 * Created by CodeingBoy on 2016-7-21-0021.
 */
public interface Servlet {
    void init() throws Exception;
    String service(HTTPRequest request);
}
