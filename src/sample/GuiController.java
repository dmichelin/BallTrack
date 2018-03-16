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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class GuiController {
    private Canvas canvas;
    private HashMap<SimulatedDistributedNode,Node> distributedNodeToDot;
    private HashMap<Node,SimulatedDistributedNode> dotToNode;
    private HashMap<Node,Boolean> nodeIsOnLeftSide;
    private Group root;
    private static boolean invalid;
    public static GuiController instance;

    public GuiController(Canvas canvas, List<SimulatedDistributedNode> nodes, Group root) {
        this.canvas = canvas;
        this.root = root;
        distributedNodeToDot = new HashMap<>();
        for(SimulatedDistributedNode node : nodes){
            Circle cir = new Circle(10, 10, 10);
            cir.setFill(generateRandomColor());
            distributedNodeToDot.put(node,cir);
        }
    }

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

    public static GuiController getInstance(Canvas canvas, List<SimulatedDistributedNode> nodes, Group root){
        if(instance == null){
            instance = new GuiController(canvas, nodes, root);
        }
        return instance;
    }
    public static void start(){
        invalid = false;
        instance.run();
    }
    public static void stop(){
        invalid=true;
    }

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

    public void EnterCriticalSection(SimulatedDistributedNode node){
        if(nodeIsOnLeftSide.get(distributedNodeToDot.get(node))){
            animateNodeThroughCenterLeft(distributedNodeToDot.get(node));
        }else{
            animateNodeThroughCenterRight(distributedNodeToDot.get(node));

        }
    }

    private Color generateRandomColor(){
        //https://stackoverflow.com/questions/4246351/creating-random-colour-in-java
        Random rand = new Random();
        Color[] colors = {Color.BLUE,Color.RED,Color.YELLOW,Color.GREEN,Color.PINK,Color.BLACK,Color.PURPLE};
        return colors[rand.nextInt(colors.length)];
    }

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
        pathTransition.setDuration(Duration.millis(rand.nextFloat()*3000+1000));
        pathTransition.setPath(path);
        pathTransition.setNode(node);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setAutoReverse(false);
        pathTransition.setOnFinished(it -> {if(!invalid){
            dotToNode.get(node).requestPermissionToEnterCS();
        }});
        pathTransition.play();

        return pathTransition;
    }
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
        pathTransition.setDuration(Duration.millis(rand.nextFloat()*3000+1000));
        pathTransition.setPath(path);
        pathTransition.setNode(node);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setOnFinished(it ->{
            if(!invalid){dotToNode.get(node).requestPermissionToEnterCS();
            }});
        pathTransition.setAutoReverse(false);
        pathTransition.play();

        return pathTransition;
    }

    private PathTransition animateNodeThroughCenterRight(Node node){
        PathElement[] leftTriangle =
                {
                        new MoveTo(100, 125),
                        new LineTo(200, 125),
                };
        Path path = new Path();
        path.getElements().addAll(leftTriangle);
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(50));
        pathTransition.setPath(path);
        pathTransition.setNode(node);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setOnFinished(ev -> {
            if(!invalid){
                dotToNode.get(node).leaveCriticalSection();
                animateNodeInLeftTriangle(node);
            }
        });
        pathTransition.play();
        return pathTransition;
    }
    private PathTransition animateNodeThroughCenterLeft(Node node){
        PathElement[] leftTriangle =
                {
                        new MoveTo(200, 125),
                        new LineTo(100, 125),
                };
        Path path = new Path();
        path.getElements().addAll(leftTriangle);
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(50));
        pathTransition.setPath(path);
        pathTransition.setNode(node);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setOnFinished(ev -> {
            if(!invalid){
                dotToNode.get(node).leaveCriticalSection();
                animateNodeInRightTriangle(node);
            }
        });
        pathTransition.play();
        return pathTransition;
    }
}
