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

    static ArrayList<Room> rooms=new ArrayList<Room>();
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8103), 0);
        server.createContext("/home", new home());
        server.createContext("/home1", new home1());
        server.createContext("/chat", new Chat());
        server.createContext("/chatroom", new chatroom());
        server.setExecutor(null); // creates a default executor
        server.start();
    }
    public static void displayhome(HttpExchange t,int f)throws IOException
    {
        Map <String,String>parms = queryToMap(t.getRequestURI().getQuery());
        String h2="Hey "+parms.get("handle")+",<br>";
        Headers responseHeaders=t.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/html");
        BufferedInputStream bis=new BufferedInputStream (new FileInputStream("C:\\Html\\home.html"));
        int len=bis.available();
        byte[] ba = new byte[len];
        bis.read(ba, 0, len);
       // t.sendResponseHeaders(200, len+30);
        OutputStream os = t.getResponseBody();
        String h="";
        String s=" <form name=\"hidform\"> <input type=\"hidden\" name=\"handle\" value='"+parms.get("handle")+"'> </form> ";
        if(f==0)
            h="Room already exists!";
        else if(f==1)
            h="Room created!";
        else if(f==3)
            h="Room doesn't exist!";

        t.sendResponseHeaders(200, s.length()+h2.length()+len + h.length());
        os.write(h2.getBytes());
        os.write(h.getBytes());
        os.write(ba);
        os.write(s.getBytes());
        os.close();
    }


    static class Chat implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            Headers responseHeaders=t.getResponseHeaders();
            responseHeaders.set("Content-Type", "text/html");
            BufferedInputStream bis=new BufferedInputStream (new FileInputStream("C:\\Html\\name.html"));
            int len=bis.available();
            byte[] ba = new byte[len];
            bis.read(ba, 0, len);
            OutputStream os = t.getResponseBody();
            t.sendResponseHeaders(200, len);
            os.write(ba);
            os.close();
        }
    }
    public static void displaychatroom(HttpExchange t,String handle,String room,String extra)throws IOException
    {
        String h = "Hey " + handle + ",<br>";
        String h1 = "Room : " + room + "<br>";
        String members="Members : ";
        for(int i=0;i<rooms.size();i++)
                if(rooms.get(i).name.equals(room)) {
                    for (int j = 0; j < rooms.get(i).members.size(); j++)
                    {
                        members += rooms.get(i).members.get(j);
                        members += " ";
                    }
                }


        String s = " <form name=\"hidform\"> <input type=\"hidden\" name=\"handle\" value='" + handle + "'> </form> ";
        String s1 = " <form name=\"hidform1\"> <input type=\"hidden\" name=\"room\" value='" + room + "'> </form> ";
        OutputStream os = t.getResponseBody();

        Headers responseHeaders = t.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/html");
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream("C:\\Html\\chatroom.html"));
        int len = bis.available();
        byte[] ba = new byte[len];
        bis.read(ba, 0, len);
        String s2="";

        t.sendResponseHeaders(200, extra.length() + s1.length() + s.length() + len + h.length() + h1.length() + members.length());
        os.write(h.getBytes());
        os.write(h1.getBytes());
        os.write(members.getBytes());
        os.write(ba);
        os.write(extra.getBytes());
        os.write(s.getBytes());
        os.write(s1.getBytes());
        os.close();
    }

    static class chatroom implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {

            Map <String,String>parms = queryToMap(t.getRequestURI().getQuery());
            if(parms.get("ret").equals("exit"))
            {
                for(int i=0;i<rooms.size();i++)
                    if(rooms.get(i).name.equals(parms.get("room")))
                        rooms.get(i).members.remove(parms.get("handle"));
                displayhome(t, 2);
            }
            else if(parms.get("ret").equals("submit"))
            {
                message m=new message(parms.get("message"),parms.get("handle"));
                for(int i=0;i<rooms.size();i++)
                    if(rooms.get(i).name.equals(parms.get("room")))
                        rooms.get(i).messages.add(m);
                displaychatroom(t,parms.get("handle"),parms.get("room"),"Message submitted!");
            }
            else if(parms.get("ret").equals("fetch"))
            {
                String list="";
                for(int i=0;i<rooms.size();i++)
                    if(rooms.get(i).name.equals(parms.get("room")))
                        for(int j=rooms.get(i).messages.size()-1;j>=Math.max(0, rooms.get(i).messages.size() - 10);j--)
                        {
                            String message=rooms.get(i).messages.get(j).getfrom();
                            message+=" : ";
                            message+=rooms.get(i).messages.get(j).getbody();
                            message+="<br>";
                            list=message+list;
                        }
                displaychatroom(t,parms.get("handle"),parms.get("room"),list);
            }
        }
    }

    static class home1 implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            Map <String,String>parms = queryToMap(t.getRequestURI().getQuery());
            String handle=parms.get("handle");
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
                    for(int i=0;i<rooms.size();i++)
                        if(rooms.get(i).name.equals(parms.get("roomname")))
                            rooms.get(i).members.add(parms.get("handle"));
                    displaychatroom(t,handle,parms.get("roomname"),"");
                }
                else
                    displayhome(t,3);

            }
        }
    }
    static class home implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            displayhome(t,2);
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
