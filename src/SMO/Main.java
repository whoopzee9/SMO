package SMO;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application {

    private static ArrayList<Source> sources;
    private static ArrayList<Device> devices;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainScreen.fxml"));
        primaryStage.setTitle("SMO");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 700, 550));
        primaryStage.show();

    }

    public static void main(String[] args){

        launch(args);

    }

}
