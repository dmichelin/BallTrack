package sample.Model;

public class Request implements Comparable<Request> {
    private int timestamp;
    private SimulatedDistributedNode requestingNode;
    private reqType type;

    public Request(SimulatedDistributedNode requestingNode, reqType msgType) {
        this.type = msgType;
        this.requestingNode = requestingNode;
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

}
