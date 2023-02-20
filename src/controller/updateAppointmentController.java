package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class updateAppointmentController {

    @FXML
    TextField updateAppointmentID; //might not need
    @FXML TextField updateAppointmentTitle;
    @FXML TextField updateAppointmentType;
    @FXML TextField updateAppointmentDescription;
    @FXML
    DatePicker updateAppointmentStartDate;
    @FXML TextField updateAppointmentLocation;
    @FXML DatePicker updateAppointmentEndDate;
    @FXML
    ComboBox<String> updateAppointmentStartTime;
    @FXML ComboBox<String> updateAppointmentEndTime;
    @FXML ComboBox<Integer> updateAppointmentCustomerID;
    @FXML ComboBox<String> updateAppointmentContact;
    @FXML ComboBox<Integer> updateAppointmentUserID; //might not need
    @FXML
    Button updateAppointmentSaveButton;
    @FXML Button updateAppointmentCancelButton;

    public void updateAppointmentSaveButton(ActionEvent event) {
    }

    public void updateAppointmentCancelButton(ActionEvent event) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Appointments");
        window.setScene(scene);
        window.show();
    }
}
