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
    private boolean sentAck;
    private int requestedTime;
    /*
    Constructor
    The priorityqueue is expected to come from the priorityqueue(int initialCapacity, Comparator<Request> RequestCompare) constructor

     */
    public SimulatedDistributedNode(int pid, GuiController c) {
        this.controller = c;
        this.connectedNodes = new ArrayList<>();
        this.pendingJobQueue = new PriorityQueue<Request>(10, new RequestCompare());
        this.timeStamp = 0;
        this.processID = pid;
        this.acknowledgements = new Hashtable<>();
        this.inCrit = false;
        this.sentAck = false;
        this.requestedTime = 0;
    }


    /*
    increment timestamp,
    push own request to personal list
    send request to all other nodes
     */
    public void requestPermissionToEnterCS(){
        System.out.println(this.getProcessID()+ " requesting CR access at time " + this.getTimeStamp());
       Boolean hashFULL = checkAcknowledgements();
        timeStamp++;
        //send a request to all nodes.
        Request sendThis = new Request(this, reqType.Request);
        connectedNodes.forEach(node -> node.receiveRequest(sendThis));
    }
    /*
    get a request. If the timestamp attached to that request is greater than this node's timestamp,
    Then set this timestamp equal to the incoming one.

    Has 3 cases: Request (add to the queue)
                 Reply   (add the replying process to the acknowledgements hashmap)
                 Release (remove the released node from the queue and send an ack to the one following it.)
     */
    public void receiveRequest(Request req){
        if (req.getTimestamp() > this.getTimeStamp())
        {
            setTimeStamp(req.getTimestamp()+1);
        }
        else
        {
            setTimeStamp(this.getTimeStamp()+1);
        }
        switch (req.getType()){
            case Request:
                pendingJobQueue.add(req);
                // if requesting node is at the top of the queue, you aren't in critical zone, and you haven't sent an ack this pass, send an ack
                if(pendingJobQueue.peek().getRequestingNode().equals(req.getRequestingNode())
                        && !this.getInCrit() && !this.getSentAck()){
                    System.out.println(this.getProcessID() + " sending ack to " + req.getRequestingNode().getProcessID()
                    +" who has time "  + pendingJobQueue.peek().getTimestamp());
                    sendAck(req.getRequestingNode());
                    this.setSentAck(true);
                }

                break;
            case Reply:
                //add acknowledgements to a hashmap so we can enter CR when it's filled. Enter CS when we have filled our hash.
                acknowledgements.put(req.getRequestingNode().getProcessID(), true);
                if(acknowledgements.size()>=connectedNodes.size()){
                    System.out.println(this.getProcessID()+" entering crit region" + " at time " + this.getTimeStamp());
                    enterCriticalSection();
                }
                break;
            case Release:
                //remove the node that has left CR and send an ack to the next node.
                for(Request m : pendingJobQueue)
                {
                    if(req.getReqProcessID() == m.getReqProcessID())
                    {
                        pendingJobQueue.remove(m);
                       break;
                    }
                }
                if(this.getProcessID() == 0)
                {
                    for(Request m : pendingJobQueue)
                    {
                        System.out.println("Process #" + m.getReqProcessID() + " is at time " + m.getTimestamp());

                    }
                }
                this.setSentAck(false);
                //send an ack to the next in line if there is one such node.
                if(!pendingJobQueue.isEmpty()) {
                    System.out.println("CR released, " + this.getProcessID() +  " sending ack to " +
                            pendingJobQueue.peek().getRequestingNode().getProcessID() +" who had time "
                            + pendingJobQueue.peek().getTimestamp());
                    sendAck(pendingJobQueue.poll().getRequestingNode());
                }
                break;
        }

    }
    /*
    Enter critical section & clear hashtable (all acknowledgements)
     */
    private void enterCriticalSection(){
        //RequestedTime = 0 means that we are no longer waiting to get into critical region.
        requestedTime = 0;
        acknowledgements = new Hashtable<>();
        setInCrit(true);
        controller.EnterCriticalSection(this);
    }
    /*
        Set state to not be in Critical Region & send release to all connected nodes.
     */
   public void leaveCriticalSection() {
     setInCrit(false);
     System.out.println(this.getProcessID() + " leaving critical region");
     connectedNodes.forEach(node -> node.receiveRequest(new Request(this, reqType.Release)));
   }

    //send Acknowledgement
    private void sendAck(SimulatedDistributedNode node)
    {
        this.setTimeStamp(this.getTimeStamp()+1);
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
    private boolean getSentAck(){
       return this.sentAck;
    }
    private void setSentAck(boolean x){
       this.sentAck = x;
    }
}
