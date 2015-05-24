/**
 * Created by Vadiraja on 22-May-15.
 */
public class message {
    private String body;
    private String from;

    public message(String body,String from)
    {
        this.body=body;
        this.from=from;
    }
    public String getbody( )
    {
        return this.body;
    }
    public String getfrom( )
    {
        return this.from;
    }

}
