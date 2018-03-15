package sample.Model;

import sample.GuiController;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import java.util.Hashtable;

public class SimulatedDistributedNode {
    private List<SimulatedDistributedNode> connectedNodes;
    private PriorityQueue<Request> pendingJobQueue;
    private Hashtable<Integer, Boolean> acknowledgements;
    private GuiController controller;
    private int timeStamp;
    private int processID;
    private boolean inCrit;

    /*
    Constructor
    The priorityqueue is expected to come from the priorityqueue(int initialCapacity, Comparator<Request> RequestCompare) constructor

     */
    public SimulatedDistributedNode(int pid, GuiController c) {
        this.controller = c;
        this.connectedNodes = new ArrayList<>();
        this.pendingJobQueue = new PriorityQueue<>();
        this.timeStamp = 0;
        this.processID = pid;
        this.acknowledgements = new Hashtable<>();
        this.inCrit = false;
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
    }
    /*
    get a request. If the timestamp attached to that request is greater than this node's timestamp,
    Then set this timestamp equal to the incoming one.
     */
    public void receiveRequest(Request req){
        if (req.getTimestamp() > this.timeStamp)
        {
            setTimeStamp(req.getTimestamp()+1);
        }
        switch (req.getType()){
            case Request:
                pendingJobQueue.add(req);
                // if requesting node is at the top of the queue, send an ack
                if(pendingJobQueue.peek().getRequestingNode().equals(req.getRequestingNode())){
                    sendAck(req.getRequestingNode());
                }
                break;
            case Reply:
                //add acknowledgements to a hashmap so we can enter CR when it's filled
                acknowledgements.put(req.getRequestingNode().getProcessID(), true);
                break;
            case Release:
                pendingJobQueue.poll();
                //send an ack to the next in line if there is one such node.
                if(!pendingJobQueue.isEmpty()) {
                    sendAck(pendingJobQueue.peek().getRequestingNode());
                }
                break;
        }

    }
    private void enterCriticalSection(){
        acknowledgements = new Hashtable<>();
        setInCrit(true);
        controller.EnterCriticalSection(this);
    }
    /*
        Set state to not be in Critical Region & send release to all connected nodes.
     */
   private void leaveCriticalSection() {
     setInCrit(false);
     connectedNodes.forEach(node -> node.receiveRequest(new Request(this, reqType.Release)));
   }

    //send Acknowledgement
    private void sendAck(SimulatedDistributedNode node)
    {
        node.receiveRequest(new Request(this, reqType.Reply));

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
    //determine if the node is in CR.
    private boolean getInCrit()
    {
        return inCrit;
    }
    //change state of node to/from CR.
    private void setInCrit(boolean x)
    {
        this.inCrit = x;
    }
}
