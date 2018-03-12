package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;

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
            drawShapes(canvas.getGraphicsContext2D());
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }catch (IOException ioe){
            // do nothing
        }
    }

    private void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        gc.strokeLine(100, 125, 200, 125);
        gc.strokePolygon(new double[]{100,30,30}, new double[]{125,50,175},3);
        gc.strokePolygon(new double[]{200,270,270}, new double[]{125,50,175},3);
    }

}