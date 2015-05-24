import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;

/**
 * Created by Vadiraja on 23-May-15.
 */
public class Room {
    ArrayList<String> members=new ArrayList<String>();
    String name="";
    ArrayList <message> messages=new ArrayList<message>();

    Room(String name)
    {
        this.name=name;
    }
    static boolean check(String name, ArrayList<Room> rooms)
    {
        for(int i=0;i<rooms.size();i++)
            if(rooms.get(i).name.equals(name))
                return true;
        return false;
    }

}
