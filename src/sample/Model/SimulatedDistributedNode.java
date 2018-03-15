package sample.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import java.util.Hashtable;

public class SimulatedDistributedNode {
    private List<SimulatedDistributedNode> connectedNodes;
    private PriorityQueue<Request> pendingJobQueue;
    private Hashtable<Integer, Boolean> acknowledgements;
    private int timeStamp;
    private int processID;

    /*
    Constructor
    The priorityqueue is expected to come from the priorityqueue(int initialCapacity, Comparator<Request> RequestCompare) constructor

     */
    public SimulatedDistributedNode(int pid) {
        this.connectedNodes = new ArrayList<>();
        this.pendingJobQueue = new PriorityQueue<>();
        this.timeStamp = 0;
        this.processID = pid;
        this.acknowledgements = new Hashtable<>();
    }


    /*
    increment timestamp,
    push own request to personal list
    send request to all other nodes
     */
    public void requestPermissionToEnterCS(){
       Boolean hashFULL = checkAcknowledgements();
        timeStamp++;
        connectedNodes.forEach(node -> node.receiveRequest(new Request(this, reqType.Request)));

        if(this.getProcessID().equals(pendingJobQueue.peek().getRequestingNode().getProcessID())&& hashFULL)
        {
            //Enter Critical Region
        }
    }
    //get a request. If the timestamp attached to that request is greater than this node's timestamp,
    //then set this timestamp equal to the incoming one.
    public void receiveRequest(Request req){
        if (req.getTimestamp() > this.timeStamp)
        {
            setTimeStamp(req.getTimestamp()+1);
        }
        switch (req.getType()){
            case Request:
                pendingJobQueue.add(req);
                break;
            case Reply:
                //add acknowledgements to a hashmap so we can enter CR when it's filled
                acknowledgements.put(req.getRequestingNode().getProcessID(), true);
            case Release:
                pendingJobQueue.poll();
                break;
        }

    }

    //send Acknowledgement
    private void sendAck(SimulatedDistributedNode node)
    {
        if(node.getProcessID().equals(pendingJobQueue.peek().getRequestingNode().getProcessID())) {
            node.receiveRequest(new Request(this, reqType.Reply));
        }
    }
    //Return list of connectedNodes
    public List<SimulatedDistributedNode> getConnectedNodes() {
        return connectedNodes;
    }

    //set the list of connectedNodes
    public void setConnectedNodes(List<SimulatedDistributedNode> connectedNodes) {
        this.connectedNodes = connectedNodes;
    }

    //get the current jobqueue
    private PriorityQueue<Request> getPendingJobQueue() {
        return pendingJobQueue;
    }

    //set the current jobqueue.
    private void setPendingJobQueue(PriorityQueue<Request> pendingJobQueue) {
        this.pendingJobQueue = pendingJobQueue;
    }
    //Get Timestamp
    public int getTimeStamp() {
        return timeStamp;
    }

    //get Process ID. (pid) in Integer form (not primitive, so hashtables can use it.)
    public Integer getProcessID() {
        return (Integer)processID;
    }

    //Set this timestamp
    private void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    //see if we have all of the acknowledgements
    private Boolean checkAcknowledgements()
    {
        for (SimulatedDistributedNode node: connectedNodes) {
            if (!acknowledgements.containsKey(node.getProcessID()))
            {
                return false;
            }

        }
        return true;
    }
}
