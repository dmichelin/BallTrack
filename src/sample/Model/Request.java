package sample.Model;

public class Request {
    private int timestamp;
    private SimulatedDistributedNode requestingNode;


    public Request(SimulatedDistributedNode requestingNode) {
        this.requestingNode = requestingNode;
        timestamp = requestingNode.getTimeStamp();
    }

    public SimulatedDistributedNode getRequestingNode() {
        return requestingNode;
    }

    public int getTimestamp() {
        return timestamp;
    }

}
