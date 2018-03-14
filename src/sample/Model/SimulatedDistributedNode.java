package sample.Model;

import java.util.List;

public class SimulatedDistributedNode {
    private List<SimulatedDistributedNode> connectedNodes;
    private List<Request> pendingJobQueue;
    private int timeStamp;

    public SimulatedDistributedNode(List<SimulatedDistributedNode> connectedNodes, List<Request> jobQueue) {
        this.connectedNodes = connectedNodes;
        this.pendingJobQueue = jobQueue;
        this.timeStamp = 0;
    }

    public void requestPermissionToEnterCS(){
        timeStamp++;
        connectedNodes.parallelStream().forEach(node -> node.getPendingJobQueue().add(new Request(this)));
    }
    //get a request. If the timestamp attached to that request is greater than this node's timestamp,
    //then set this timestamp equal to the incoming one.
    public void receiveRequest(Request req){
        if (req.getRequestingNode().getTimeStamp() > this.timeStamp)
        {
            this.timeStamp = req.getRequestingNode().getTimeStamp();
        }
        pendingJobQueue.add(req);
    }

    public List<SimulatedDistributedNode> getConnectedNodes() {
        return connectedNodes;
    }

    private void setConnectedNodes(List<SimulatedDistributedNode> connectedNodes) {
        this.connectedNodes = connectedNodes;
    }

    private List<Request> getPendingJobQueue() {
        return pendingJobQueue;
    }

    private void setPendingJobQueue(List<Request> pendingJobQueue) {
        this.pendingJobQueue = pendingJobQueue;
    }
    //Get Timestamp
    public int getTimeStamp() {
        return timeStamp;
    }
    //Set this timestamp
    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

}
