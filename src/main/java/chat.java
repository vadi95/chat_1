/**
 * Created by Vadiraja on 21-May-15.
 */

import java.io.*;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class chat {

    public static void main(String[] args) throws Exception {

        try {

            HttpServer server = HttpServer.create(new InetSocketAddress(8097), 0);
            server.createContext("/chat", new MyHandler());
            server.setExecutor(null); // creates a default executor
            server.start();
        }
        catch(Exception e){
            System.out.println(e+"test");
        }
        finally {
            System.out.println("Done");
        }
        PrintStream p=null;
        try
        {
            BufferedInputStream bis=new BufferedInputStream (new FileInputStream("C:\\Html\\Chatpage.html"));
            int len=bis.available();
            byte[] ba=new byte[len];
            p= new PrintStream(new FileOutputStream("C:\\Html\\error.txt"));
            System.out.println("len= " + len);
            bis.read(ba,0,len);
            String response = "Welcome Real's HowTo test page";
            //    t.sendResponseHeaders(200, response.length());


          //  OutputStream os = t.getResponseBody();
          //  t.sendResponseHeaders(200, len);
            //  os.write(response.getBytes());
            System.out.write(ba);
            p.close();
          //  os.close();
        }
        catch(Exception e){
            p.println(e + "test");
        }
    }

    static class MyHandler implements HttpHandler {
        public void handle(HttpExchange t)  {
            PrintStream p=null;
            try
            {
            BufferedInputStream bis=new BufferedInputStream (new FileInputStream("C:\\Html\\Chatpage.html"));
            int len=bis.available();
                byte[] ba=new byte[len];
                p= new PrintStream(new FileOutputStream("C:\\Html\\error.txt"));
                p.println("len= " + len);
            bis.read(ba,0,len);
            String response = "Welcome Real's HowTo test page";
        //    t.sendResponseHeaders(200, response.length());


            OutputStream os = t.getResponseBody();
            t.sendResponseHeaders(200, len);
            //  os.write(response.getBytes());
            os.write(ba);
            os.close();
            }
            catch(Exception e){
                p.println(e + "test");
            }
        }
    }



}
