package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    ArrayList<City> cityList;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        primaryStage.setTitle("Antenna Genetic Algorithm 1.0 by Boutos Apostolos");
        primaryStage.setScene(new Scene(root, 1350, 600));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
