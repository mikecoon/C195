package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class updateCustomerController {
    public void updateCustomerCancelButton(ActionEvent event) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Appointments");
        window.setScene(scene);
        window.show();
    }

    public void updateCustomerSaveButton(ActionEvent actionEvent) throws Exception{
    }
}
