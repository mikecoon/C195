package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import DAO.userDB;


public class loginController implements Initializable {
    @FXML private TextField loginUsername;
    @FXML private TextField loginPassword;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Log In initialized!");
    }

    public void loginButton(ActionEvent event) throws Exception, IOException, SQLException {
        String userName = loginUsername.getText();
        String password = loginPassword.getText();

        boolean login = userDB.AttemptLogin(userName, password);

        if (login) {
            Parent parent = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
            Scene scene = new Scene(parent);
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setTitle("Appointments");
            window.setScene(scene);
            window.show();
        }

    }

    public void exitProgram(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();


    }
}
