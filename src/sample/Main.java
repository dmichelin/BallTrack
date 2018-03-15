package sample;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Model.SimulatedDistributedNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try{
            primaryStage.setTitle("Drawing Operations Test");
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("sample/sample.fxml"));
            Parent root = loader.load();
            Group group = new Group();
            Canvas canvas = new Canvas(300,300);
            group.getChildren().addAll(canvas);
            GridPane gp = (GridPane) loader.getNamespace().get("pane");
            Button button = (Button) loader.getNamespace().get("startButton");
            TextField numberOfNodesField = (TextField) loader.getNamespace().get("numberOfNodes");
            button.setOnMouseClicked((MouseEvent e) -> startSimulation(group, canvas.getGraphicsContext2D(), canvas,Integer.parseInt(numberOfNodesField.getText())));
            gp.setAlignment(Pos.CENTER);
            gp.getChildren().add(group);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }catch (IOException ioe){
            // do nothing
        }
    }
    private void startSimulation(Group gp, GraphicsContext gc, Canvas canvas, int numNodes){
        // create the appropriate number of nodes
        Controller controller = new Controller();
        List<SimulatedDistributedNode> allNodes = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            allNodes.add(new SimulatedDistributedNode(i,controller));
        }
        // have them all connected to each other
        for (int i = 0; i < numNodes; i++) {
            allNodes.get(i).setConnectedNodes(allNodes);
        }
        drawShapes(gp,gc,canvas);
    }

    private void drawShapes(Group gp, GraphicsContext gc, Canvas canvas) {
        final Circle rect1 = new Circle(10, 10, 10);
        final Circle rect2 = new Circle(10, 10, 10);
        final Circle rect3 = new Circle(10, 10, 10);
        final Circle rect4 = new Circle(10,10,10);

        rect1.setFill(Color.RED);
        rect2.setFill(Color.BLUE);
        rect3.setFill(Color.GREEN);
        rect4.setFill(Color.PINK);

        animateNodeInLeftTriangle(rect1);
        animateNodeInRightTriangle(rect2);
        animateNodeThroughCenterRight(rect3);
        animateNodeThroughCenterLeft(rect4);

        gp.getChildren().add(rect1);
        gp.getChildren().add(rect2);
        gp.getChildren().add(rect3);
        gp.getChildren().add(rect4);


        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        gc.strokeLine(100, 125, 200, 125);
        gc.strokePolygon(new double[]{100,30,30}, new double[]{125,50,175},3);
        gc.strokePolygon(new double[]{200,270,270}, new double[]{125,50,175},3);
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