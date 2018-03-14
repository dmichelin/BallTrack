package sample;

import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

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
            Canvas canvas = (Canvas) loader.getNamespace().get("canvas");
            GridPane gp = (GridPane) loader.getNamespace().get("pane");
            drawShapes(gp, canvas.getGraphicsContext2D());
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }catch (IOException ioe){
            // do nothing
        }
    }

    private void drawShapes(GridPane gp, GraphicsContext gc) {
        final Circle rect1 = new Circle(10, 10, 100);

        rect1.setFill(Color.RED);
        PathElement[] elements =
                {
                        new MoveTo(0, 300),
                        new LineTo(300, 400),
                        new LineTo(400, 100),
                        new LineTo(100, 0),
                        new LineTo(0, 300),
                        new ClosePath()
                };
        Path path = new Path();
        path.getElements().addAll(elements);
        PathTransition pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(4000));
        pathTransition.setPath(path);
        pathTransition.setNode(rect1);
        pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
        pathTransition.setCycleCount(Timeline.INDEFINITE);
        pathTransition.setAutoReverse(true);
        gp.add(rect1,1,1);
        pathTransition.play();

        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        gc.strokeLine(100, 125, 200, 125);
        gc.strokePolygon(new double[]{100,30,30}, new double[]{125,50,175},3);
        gc.strokePolygon(new double[]{200,270,270}, new double[]{125,50,175},3);
    }

}