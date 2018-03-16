package sample;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import sample.Model.SimulatedDistributedNode;

import java.util.*;
import java.util.stream.Collectors;
/*
Controls the GUI for our animations. The min & max speed is used to determine how fast the nodes (given in a list) move.

 */
public class GuiController {
    private Canvas canvas;
    private HashMap<SimulatedDistributedNode,Node> distributedNodeToDot;
    private HashMap<Node,SimulatedDistributedNode> dotToNode;
    private HashMap<Node,Boolean> nodeIsOnLeftSide;
    private HashMap<PathTransition, Boolean> transitionList;
    private Group root;
    private static boolean invalid;
    public static GuiController instance;
    int min;
    int max;

    public GuiController(Canvas canvas, List<SimulatedDistributedNode> nodes, Group root, int minSpeed, int maxSpeed) {
        this.canvas = canvas;
        this.root = root;
        this.min = minSpeed;
        this.max = maxSpeed;
        distributedNodeToDot = new HashMap<>();
        this.transitionList = new HashMap<>();
        for(SimulatedDistributedNode node : nodes){
            Circle cir = new Circle(10, 10, 10);
            cir.setFill(generateRandomColor());
            distributedNodeToDot.put(node,cir);
        }
    }

    /*
    Gives each node a randomly colored circle to represent it.
     */
    public void setDistributedNodeToDot(List<SimulatedDistributedNode> nodes) {
        distributedNodeToDot = new HashMap<>();
        nodeIsOnLeftSide = new HashMap<>();
        dotToNode = new HashMap<>();
        for(SimulatedDistributedNode node : nodes){
            Circle cir = new Circle(10, 10, 10);
            cir.setFill(generateRandomColor());
            distributedNodeToDot.put(node,cir);
            dotToNode.put(cir,node);
        }
    }

    public static GuiController getInstance(Canvas canvas, List<SimulatedDistributedNode> nodes, Group root, int min, int max){
        if(instance == null){
            instance = new GuiController(canvas, nodes, root, min, max);
        }
        return instance;
    }
    /*
    begins the simulation
     */
    public static void start(){
        invalid = false;
        instance.run();
    }
    public static void stop(){
        invalid=true;
    }
/*
Initializes each node to run on a randomly chosen side of the system.
 */
    private void run(){

        distributedNodeToDot.values().forEach(it ->{
            Random rand = new Random();
            switch (rand.nextInt(2)){
                case 0:
                    animateNodeInLeftTriangle(it);
                    break;
                case 1:
                    animateNodeInRightTriangle(it);
            }
        });
        distributedNodeToDot.values().forEach(it -> root.getChildren().add(it));
    }
/*
handles animation for entering the critical section.
 */
    public void EnterCriticalSection(SimulatedDistributedNode node){
        if(nodeIsOnLeftSide.get(distributedNodeToDot.get(node))){
            animateNodeThroughCenterLeft(distributedNodeToDot.get(node));
        }else{
            animateNodeThroughCenterRight(distributedNodeToDot.get(node));

        }
    }

    /*
    Uses our hashmap of transitional animations to pause each one. State logic is based on animation completion, so this pauses our simulation.
     */
    public void pause()
    {
        transitionList.keySet().forEach(anim -> anim.pause());
    }
    /*
    Uses our hashmap of transitional animations to resume each one. State logic is based on animation completion, so this resumes our simulation.
     */
    public void resume()
    {
        transitionList.keySet().forEach(anim -> anim.play());
    }

    private Color generateRandomColor(){
        //https://stackoverflow.com/questions/4246351/creating-random-colour-in-java
        Random rand = new Random();
        Color[] colors = {Color.BLUE,Color.RED,Color.YELLOW,Color.GREEN,Color.PINK,Color.BLACK,Color.PURPLE};
        return colors[rand.nextInt(colors.length)];
    }
/*
sets the node on a path along the left triangle.
The animation itself is added to a hashmap of all playing animations when it begins & is removed on completion.
 */
    private PathTransition animateNodeInLeftTriangle(Node node){
        nodeIsOnLeftSide.put(node,true);
        Random rand = new Random();
        PathElement[] rightTriangle =
                {
                        new MoveTo(200, 125),
                        new LineTo(270, 50),
                        new LineTo(270, 175),
                        new ClosePath()
                };
        Path path = new Path();
        path.getElements().addAll(rightTriangle);
        PathTransition pathTransition = new PathTransition();
        transitionList.put(pathTransition, Boolean.TRUE);
        pathTransition.setDuration(Duration.millis(rand.nextFloat()*(max-min + 1)+min));
        pathTransition.setPath(path);
        pathTransition.setNode(node);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setAutoReverse(false);
        pathTransition.setOnFinished(it -> {if(!invalid){
            dotToNode.get(node).requestPermissionToEnterCS();
            transitionList.remove(pathTransition);
        }});
        pathTransition.play();

        return pathTransition;
    }
    /*
sets the node on a path along the right triangle.
The animation itself is added to a hashmap of all playing animations when it begins & is removed on completion.
 */
    private PathTransition animateNodeInRightTriangle(Node node){
        nodeIsOnLeftSide.put(node,false);
        Random rand = new Random();
        PathElement[] leftTriangle =
                {
                        new MoveTo(100, 125),
                        new LineTo(30, 50),
                        new LineTo(30, 175),
                        new ClosePath()
                };
        Path path = new Path();
        path.getElements().addAll(leftTriangle);
        PathTransition pathTransition = new PathTransition();
        transitionList.put(pathTransition, Boolean.TRUE);
        pathTransition.setDuration(Duration.millis(rand.nextFloat()*(max-min + 1)+min));
        pathTransition.setPath(path);
        pathTransition.setNode(node);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setOnFinished(it ->{
            if(!invalid){dotToNode.get(node).requestPermissionToEnterCS();
                transitionList.remove(pathTransition);
            }});
        pathTransition.setAutoReverse(false);
        pathTransition.play();

        return pathTransition;
    }
    /*
    sets the node on a path along the Critical Region going left.
    The animation itself is added to a hashmap of all playing animations when it begins & is removed on completion.
     */
    private PathTransition animateNodeThroughCenterRight(Node node){
        PathElement[] leftLine =
                {
                        new MoveTo(100, 125),
                        new LineTo(200, 125),
                };
        Path path = new Path();
        Random rand = new Random();
        path.getElements().addAll(leftLine);
        PathTransition pathTransition = new PathTransition();
        transitionList.put(pathTransition, Boolean.TRUE);
        pathTransition.setDuration(Duration.millis((rand.nextFloat()*((max-min + 1)+min))/(max/min)));
        pathTransition.setPath(path);
        pathTransition.setNode(node);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setOnFinished(ev -> {
            if(!invalid){
                dotToNode.get(node).leaveCriticalSection();
                animateNodeInLeftTriangle(node);
                transitionList.remove(pathTransition);
            }
        });
        pathTransition.play();
        return pathTransition;
    }
    /*
sets the node on a path along the Critical Region going right.
The animation itself is added to a hashmap of all playing animations when it begins & is removed on completion.
 */
    private PathTransition animateNodeThroughCenterLeft(Node node){
        PathElement[] rightLine =
                {
                        new MoveTo(200, 125),
                        new LineTo(100, 125),
                };
        Path path = new Path();
        Random rand = new Random();
        path.getElements().addAll(rightLine);
        PathTransition pathTransition = new PathTransition();
        transitionList.put(pathTransition, Boolean.TRUE);
        pathTransition.setDuration(Duration.millis((rand.nextFloat()*((max-min + 1)+min))/(max/min)));
        pathTransition.setPath(path);
        pathTransition.setNode(node);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setOnFinished(ev -> {
            if(!invalid){
                dotToNode.get(node).leaveCriticalSection();
                animateNodeInRightTriangle(node);
                transitionList.remove(pathTransition);
            }
        });
        pathTransition.play();
        return pathTransition;
    }
}
