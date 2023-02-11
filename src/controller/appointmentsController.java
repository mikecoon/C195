package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import model.Appointment;
import DAO.appointmentDB;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class appointmentsController implements Initializable {
    @FXML
    RadioButton monthButton;
    @FXML
    RadioButton weekButton;
    @FXML
    RadioButton allButton;
    @FXML
    TableView<Appointment> appointmentTable;



    @Override
    public void initialize(URL location, ResourceBundle resources){
        allButton.setSelected(true);

        ObservableList<Appointment> appointments = null;
        try{
            appointments = appointmentDB.getAllAppointments();
        }
        catch (SQLException e){
            e.printStackTrace();
            //come back to this and add logic incase of connection error
        }


    }
}
