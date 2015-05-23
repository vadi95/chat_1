import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;

/**
 * Created by Vadiraja on 23-May-15.
 */
public class Room {
    static ArrayList<String> rooms=new ArrayList<String>();
    static void addroom(String name)
    {
        rooms.add(name);
    }
    static boolean check(String name)
    {
        System.out.println("Test");
       if(rooms.contains(name))
            return true;
        else
            return false;
    }


}
