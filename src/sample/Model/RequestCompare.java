package sample.Model;

import java.util.Comparator;
/*
Allows us to safely compare requests.

 */
public class RequestCompare implements Comparator<Request>{
    public int compare(Request r1, Request r2)
    {
        return r1.compareTo(r2);
    }
}
