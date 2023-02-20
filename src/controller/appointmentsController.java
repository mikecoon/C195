package controller;

import DAO.appointmentDAO;
import DAO.customerDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
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
    @FXML
    TableView<Customer> customerTable;
    @FXML private TableColumn<?, ?> customerName;
    @FXML private TableColumn<?, ?> customerAddress;
    @FXML private TableColumn<?, ?> customerID;
    @FXML private TableColumn<?, ?> customerPhoneNumber;
    @FXML private TableColumn<?, ?> customerState;
    @FXML private TableColumn<?, ?> customerPostalCode;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        //set appointment table
        allButton.setSelected(true);

        ObservableList<Appointment> appointments = null;
        try{
            appointments = appointmentDAO.getAllAppointments();
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

        //set customer table
        ObservableList<Customer> customers = null;
        try{
            customers = customerDAO.getAllCustomers();
        } catch (SQLException e){
            System.out.println("error");
        }
        //ID not loading in properly.
        customerID.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        customerPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerTable.setItems(customers);

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

        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, "You must select an appointment to edit it.", clickOkay);
            invalidInput.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/updateAppointment.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);

        //populate updateAppoinment page with selectedAppointment values
        updateAppointmentController UAcontroller = loader.getController();
        UAcontroller.populateFields(selectedAppointment);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Update Appointments");
        window.setScene(scene);
        window.show();


    }
    public void deleteAppointmentButton(ActionEvent event) throws Exception{
        try{
            String appointmentType = appointmentTable.getSelectionModel().getSelectedItem().getType();
            Integer appointmentID = appointmentTable.getSelectionModel().getSelectedItem().getAppointmentID();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete Appointment: " + appointmentType + " ID: " + appointmentID);
            Optional<ButtonType> confirm = alert.showAndWait();
            if(confirm.get() == ButtonType.OK && confirm.isPresent()){

                appointmentDAO.deleteAppointment(appointmentID);
                ObservableList<Appointment> allAppointmentsList = appointmentDAO.getAllAppointments();
                appointmentTable.setItems(allAppointmentsList);
            }
        } catch (Exception e){
            System.out.println(e);
        }
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
