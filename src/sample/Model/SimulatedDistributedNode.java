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
        connectedNodes.parallelStream().forEach(node -> node.getPendingJobQueue().add(new Request()));
    }

    public void recieveRequest(SimulatedDistributedNode node){
        
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
}
