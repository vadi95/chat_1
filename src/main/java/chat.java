import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/*
 * a simple static http server
*/
public class chat {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8102), 0);
        server.createContext("/test", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
        //    String response = "Welcome Real's HowTo test page";
            Headers responseHeaders=t.getResponseHeaders();
            responseHeaders.set("Content-Type", "text/html");
            BufferedInputStream bis=new BufferedInputStream (new FileInputStream("C:\\Html\\Chatpage.html"));
            int len=bis.available();
            byte[] ba=new byte[len];
            bis.read(ba,0,len);
            t.sendResponseHeaders(200, len);
            OutputStream os = t.getResponseBody();
            os.write(ba);
            os.close();
        }
    }
}