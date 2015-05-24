import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class chat {
    static int flag=0;
    static ArrayList<Room> rooms=new ArrayList<Room>();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8103), 0);
        server.createContext("/home", new home());
        server.createContext("/chatroom", new chatroom());
        server.setExecutor(null); // creates a default executor
        server.start();
    }
    public static void displayhome(HttpExchange t,int f)throws IOException
    {
        Headers responseHeaders=t.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/html");
        BufferedInputStream bis=new BufferedInputStream (new FileInputStream("C:\\Html\\home.html"));
        int len=bis.available();
        byte[] ba = new byte[len];
        bis.read(ba, 0, len);
       // t.sendResponseHeaders(200, len+30);
        OutputStream os = t.getResponseBody();
        String h="";
        if(f==0)
            h="Room already exists!";
        else if(f==1)
            h="Room created!";
        else if(f==3)
            h="Room doesn't exist!";

        t.sendResponseHeaders(200, len+h.length());
        os.write(h.getBytes());
        os.write(ba);
        os.close();
    }
    static class chatroom implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            displayhome(t,2);
        }
    }
    static class home implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            if(flag==0){
                displayhome(t,2);
                flag=1;
            }
            Map <String,String>parms = queryToMap(t.getRequestURI().getQuery());

            if(parms.get("ret").equals("create"))
            {
                String r= parms.get("roomname");
                if(Room.check(r,rooms))
                    displayhome(t, 0);
                else
                {
                    rooms.add(new Room(r));
                    displayhome(t,1);
                }
            }
            else if(parms.get("ret").equals("enter"))
            {
                String r= parms.get("roomname");

                if(Room.check(r,rooms))
                {
                    Headers responseHeaders=t.getResponseHeaders();
                    responseHeaders.set("Content-Type", "text/html");
                    BufferedInputStream bis=new BufferedInputStream (new FileInputStream("C:\\Html\\chatroom.html"));
                    int len=bis.available();
                    byte[] ba = new byte[len];
                    bis.read(ba, 0, len);
                    String q="Members Present: ";
                    OutputStream os = t.getResponseBody();
                    t.sendResponseHeaders(200, len);
                    os.write(ba);
                    os.close();
                }
                else
                    displayhome(t,3);

            }
        }
    }


    public static Map<String, String> queryToMap(String query){
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length>1) {
                result.put(pair[0], pair[1]);
            }else{
                result.put(pair[0], "");
            }
        }
        return result;
    }

}
