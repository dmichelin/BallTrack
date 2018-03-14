package sample.Model;

public class Request {

    private SimulatedDistributedNode requestingNode;


    public Request(SimulatedDistributedNode requestingNode) {
        this.requestingNode = requestingNode;

    }

    public SimulatedDistributedNode getRequestingNode() {
        return requestingNode;
    }


}
