package sample.Model;

import java.util.Comparator;

public class RequestCompare implements Comparator<Request>{
    public int compare(Request r1, Request r2)
    {
        return r1.compareTo(r2);
    }
}
