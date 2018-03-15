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
import java.util.Random;

public class GuiController {
    private Canvas canvas;
    private HashMap<SimulatedDistributedNode,Node> distributedNodeToDot;
    private Group root;
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
        for(SimulatedDistributedNode node : nodes){
            Circle cir = new Circle(10, 10, 10);
            cir.setFill(generateRandomColor());
            distributedNodeToDot.put(node,cir);
        }
    }

    public static GuiController getInstance(Canvas canvas, List<SimulatedDistributedNode> nodes, Group root){
        if(instance == null){
            instance = new GuiController(canvas, nodes, root);
        }
        return instance;
    }
    public static void start(){
        instance.run();
    }

    private void run(){
        distributedNodeToDot.values().forEach(this::animateNodeInLeftTriangle);
        distributedNodeToDot.values().forEach(it -> root.getChildren().add(it));
    }

    public void EnterCriticalSection(SimulatedDistributedNode node){

    }

    private Color generateRandomColor(){
        //https://stackoverflow.com/questions/4246351/creating-random-colour-in-java
        Random rand = new Random();
        return Color.hsb(rand.nextFloat(), 0.9f,1.0f);
    }

    private PathTransition animateNodeInLeftTriangle(Node node){
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
        pathTransition.setDuration(Duration.millis(4000));
        pathTransition.setPath(path);
        pathTransition.setNode(node);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(false);
        pathTransition.setOnFinished(it ->{int i = 1;});
        pathTransition.play();

        return pathTransition;
    }
    private PathTransition animateNodeInRightTriangle(Node node){
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
        pathTransition.setDuration(Duration.millis(4000));
        pathTransition.setPath(path);
        pathTransition.setNode(node);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(false);
        pathTransition.play();

        return pathTransition;
    }

    private PathTransition animateNodeThroughCenterRight(Node node){
        PathElement[] leftTriangle =
                {
                        new MoveTo(100, 125),
                        new LineTo(200, 125),
                        new ClosePath()
                };
        Path path = new Path();
        path.getElements().addAll(leftTriangle);
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(4000));
        pathTransition.setPath(path);
        pathTransition.setNode(node);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);
        pathTransition.play();
        return pathTransition;
    }
    private PathTransition animateNodeThroughCenterLeft(Node node){
        PathElement[] leftTriangle =
                {
                        new MoveTo(200, 125),
                        new LineTo(100, 125),
                        new ClosePath()
                };
        Path path = new Path();
        path.getElements().addAll(leftTriangle);
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(4000));
        pathTransition.setPath(path);
        pathTransition.setNode(node);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);
        pathTransition.play();
        return pathTransition;
    }
}
