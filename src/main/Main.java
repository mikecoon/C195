package main;

import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/logIn.fxml"));
        stage.setTitle("Log In");
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }
    public static void main(String[] args) throws Exception{
        JDBC.makeConnection();
        launch(args);
        JDBC.closeConnection();
    }

}
