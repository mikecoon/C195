package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import DAO.appointmentDAO;

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
    @FXML private TableColumn<?, ?> appointmentID;
    @FXML private TableColumn<?, ?> appointmentTitle;
    @FXML private TableColumn<?, ?> appointmentType;
    @FXML private TableColumn<?, ?> appointmentDescription;
    @FXML private TableColumn<?, ?> appointmentLocation;
    @FXML private TableColumn<?, ?> appointmentStart;
    @FXML private TableColumn<?, ?> appointmentEnd;
    @FXML private TableColumn<?, ?> appointmentContact;
    @FXML private TableColumn<?, ?> appointmentCustomerID;




    @Override
    public void initialize(URL location, ResourceBundle resources){
        allButton.setSelected(true);

        ObservableList<Appointment> appointments = null;
        try{
            appointments = appointmentDB.getAllAppointments();
            //System.out.println(appointments);
        }
        catch (SQLException e){
            System.out.println("error");
            //come back to this and add logic incase of connection error
        }
        appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        appointmentStart.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        appointmentEnd.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        appointmentContact.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        appointmentCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        appointmentTable.setItems(appointments);



    }
    public void addAppointmentButton(ActionEvent event) throws Exception{
        Parent parent = FXMLLoader.load(getClass().getResource("/view/addAppointment.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Add Appointments");
        window.setScene(scene);
        window.show();


    }
    public void updateAppointmentButton(ActionEvent event) throws Exception{
        Parent parent = FXMLLoader.load(getClass().getResource("/view/addAppointment.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Update Appointments");
        window.setScene(scene);
        window.show();


    }
    public void addCustomerButton(ActionEvent event) throws Exception{
        Parent parent = FXMLLoader.load(getClass().getResource("/view/addCustomer.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Add Customer");
        window.setScene(scene);
        window.show();

    }
    public void updateCustomerButton(ActionEvent event) throws Exception{
        Parent parent = FXMLLoader.load(getClass().getResource("/view/updateCustomer.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Update Customer");
        window.setScene(scene);
        window.show();

    }
}
