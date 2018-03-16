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
import javafx.scene.control.Slider;
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
    private Group drawingGroup;
    private GuiController controller;
    private boolean started;
/*
Our main class. Run this.
Builds the canvas and list of nodes. Runs the simulation.
 */
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
            //variable keeps track of whether the animation has started or not.
            started = false;
            Canvas canvas = new Canvas(300,300);
            group.getChildren().addAll(canvas);
            GridPane gp = (GridPane) loader.getNamespace().get("pane");
            Button button = (Button) loader.getNamespace().get("startButton");
            Button endButton = (Button) loader.getNamespace().get("stopButton");
            Slider numberOfNodesField = (Slider) loader.getNamespace().get("numberOfNodes");
            TextField minSpeedField = (TextField) loader.getNamespace().get("MinimumSpeed");
            TextField maxSpeedField = (TextField) loader.getNamespace().get("MaximumSpeed");
            numberOfNodesField.setBlockIncrement(1);
            numberOfNodesField.setMin(1);
            numberOfNodesField.setMax(10);
            button.setOnMouseClicked((MouseEvent e) -> {
                if (!started) {
                    int min = 1000;
                    int max = 2000;
                    try {
                        min = Integer.parseInt(minSpeedField.getText());
                    } catch (Exception ex) {
                        System.out.println("Invalid input. Setting min to default value.");
                        min = 1000;
                    }
                    try {
                        max = Integer.parseInt(maxSpeedField.getText());
                    } catch (Exception ex) {
                        System.out.println("Invalid input. Setting max to default value.");
                        max = min + 1000;
                    }
                    if (max < min) {
                        max = min + 1000;
                        System.out.println("Readjusting max to be greater than min.");
                    }

                    if (max >= min) {
                        button.setDisable(true);
                        endButton.setDisable(false);
                        started = true;
                        startSimulation(gp, (int) numberOfNodesField.getValue(), min, max);
                    }

                }
                else {
                    button.setDisable(true);
                    endButton.setDisable(false);
                    controller.resume();
                }
            });
            endButton.setOnMouseClicked((MouseEvent e) ->{

                    controller.pause();
                    endButton.setDisable(true);
                    button.setDisable(false);
            });
            gp.setAlignment(Pos.CENTER);
            gp.getChildren().add(group);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }catch (IOException ioe){
            int i = 1;
            // do nothing
        }

    }
    private void startSimulation(GridPane gridPane,int numNodes, int minSpeed, int maxSpeed){
        System.out.println("Min: " + minSpeed);
        System.out.println("Max: " + maxSpeed);
        if(drawingGroup!=null){
            gridPane.getChildren().remove(drawingGroup);
            GuiController.stop();
        }
        drawingGroup = new Group();
        Canvas canvas = new Canvas(300,300);
        drawingGroup.getChildren().addAll(canvas);
        gridPane.getChildren().add(drawingGroup);
        // create the appropriate number of nodes
        List<SimulatedDistributedNode> allNodes = new ArrayList<>();

        for (int i = 0; i < numNodes; i++) {
            allNodes.add(new SimulatedDistributedNode(i,GuiController.getInstance(canvas,allNodes,drawingGroup, minSpeed, maxSpeed)));
        }
        // have them all connected to each other
        for (int i = 0; i < numNodes; i++) {
            allNodes.get(i).setConnectedNodes(allNodes);
        }
        controller = GuiController.getInstance(canvas,allNodes,drawingGroup, minSpeed, maxSpeed);
        controller.setDistributedNodeToDot(allNodes);
        GuiController.start();
        drawShapes(drawingGroup,canvas.getGraphicsContext2D(),canvas);
    }

    private void drawShapes(Group gp, GraphicsContext gc, Canvas canvas) {
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        gc.strokeLine(100, 125, 200, 125);
        gc.strokePolygon(new double[]{100,30,30}, new double[]{125,50,175},3);
        gc.strokePolygon(new double[]{200,270,270}, new double[]{125,50,175},3);
    }

}