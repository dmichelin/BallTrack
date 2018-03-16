package sample.Model;

public class Request implements Comparable<Request> {
    private int timestamp;
    private SimulatedDistributedNode requestingNode;
    private reqType type;
    private int processID;

    public Request(SimulatedDistributedNode requestingNode, reqType msgType) {
        this.type = msgType;
        this.requestingNode = requestingNode;
        this.processID = requestingNode.getProcessID();
        timestamp = requestingNode.getTimeStamp();

    }

    public SimulatedDistributedNode getRequestingNode() {
        return requestingNode;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public reqType getType() {
        return type;
    }

    //Allows us to sort the priorityqueue in ascending order.
    public int compareTo(Request otherReq){
        if (this.getTimestamp() > otherReq.getTimestamp())
        {
            return 1;
        }
        else if (this.getTimestamp() == otherReq.getTimestamp())
        {
            return 0;
        }
        else
            return -1;
    }
    public int getReqProcessID()
    {
        return processID;
    }

}
