package sample.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import java.util.Hashtable;

public class SimulatedDistributedNode {
    private List<SimulatedDistributedNode> connectedNodes;
    private PriorityQueue<Request> pendingJobQueue;
    private Hashtable<SimulatedDistributedNode, Boolean> acknowledgements;
    private int timeStamp;

    /*
    Constructor
    The priorityqueue is expected to come from the priorityqueue(int initialCapacity, Comparator<Request> RequestCompare) constructor

     */
    public SimulatedDistributedNode(List<SimulatedDistributedNode> connectedNodes, PriorityQueue<Request> jobQueue) {
        this.connectedNodes = connectedNodes;
        this.pendingJobQueue = jobQueue;
        this.timeStamp = 0;
        this.acknowledgements = new Hashtable<SimulatedDistributedNode, Boolean>();
    }


    /*
    increment timestamp,
    push own request to personal list
    send request to all other nodes
     */
    public void requestPermissionToEnterCS(){
        timeStamp++;
        connectedNodes.parallelStream().forEach(node -> node.receiveRequest(new Request(this, reqType.Request)));


    }
    //get a request. If the timestamp attached to that request is greater than this node's timestamp,
    //then set this timestamp equal to the incoming one.
    public void receiveRequest(Request req){
        if (req.getTimestamp() > this.timeStamp)
        {
            setTimeStamp(req.getTimestamp()+1);
        }
        if(req.getType() == reqType.Request) {
            pendingJobQueue.add(req);
        }
        else
        {
            acknowledgements.put(req.getRequestingNode(), Boolean.TRUE);
        }
    }

    public List<SimulatedDistributedNode> getConnectedNodes() {
        return connectedNodes;
    }

    private void setConnectedNodes(List<SimulatedDistributedNode> connectedNodes) {
        this.connectedNodes = connectedNodes;
    }

    private PriorityQueue<Request> getPendingJobQueue() {
        return pendingJobQueue;
    }

    private void setPendingJobQueue(PriorityQueue<Request> pendingJobQueue) {
        this.pendingJobQueue = pendingJobQueue;
    }
    //Get Timestamp
    public int getTimeStamp() {
        return timeStamp;
    }
    //Set this timestamp
    private void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

}
