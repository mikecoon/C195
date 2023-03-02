package controller;

import DAO.appointmentDAO;
import DAO.customerDAO;
import DAO.userDAO;
import helper.JDBC;
import javafx.collections.FXCollections;
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

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/** Class to display appointments and customers as well as allow for adding, updating, deleting, and viewing records.*/
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
    @FXML private TableColumn<?,?> customerDivision;
    @FXML private TableColumn<?, ?> customerPostalCode;
    @FXML private TableColumn<?,?> customerDivisionID;

    /**
     * Initializes appointment and customer tables
     * lambda function populates the appointment tables without requiring a for loop to iterate over all appointments.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
        //set appointment table
        allButton.setSelected(true);
        ToggleGroup toggle = new ToggleGroup();
        weekButton.setToggleGroup(toggle);
        monthButton.setToggleGroup(toggle);
        allButton.setToggleGroup(toggle);

        ObservableList<Appointment> allAppts = FXCollections.observableArrayList();

        try{
            //lambda function
            ObservableList<Appointment> appointments = appointmentDAO.getAllAppointments();


            appointments.forEach(appointment -> allAppts.add(appointment));
            //appointments = appointmentDAO.getAllAppointments();
            //System.out.println(appointments);
        }
        catch (SQLException e){
            System.out.println("error");

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
        appointmentTable.setItems(allAppts);

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
        customerDivision.setCellValueFactory(new PropertyValueFactory<>("division"));
        customerDivisionID.setCellValueFactory(new PropertyValueFactory<>("divisionID"));
        customerTable.setItems(customers);

    }
    /**
     * redirects to the addAppointmentView
     * @param event
     * @throws Exception
     */
    public void addAppointmentButton(ActionEvent event) throws Exception{
        Parent parent = FXMLLoader.load(getClass().getResource("/view/addAppointment.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Add Appointments");
        window.setScene(scene);
        window.show();


    }

    /**
     * redirects to the updateAppointmentView
     * lets user know if they didnt select an appointment
     * @param event
     * @throws Exception
     */
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

    /**
     * deletes selected appointment from database
     * lets user know if no appointment is selected
     * @param event
     * @throws Exception
     */
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

    /**
     * deletes selected customer from database
     * lets user know if no customer is selected
     * @param event
     * @throws Exception
     */
    public void deleteCustomerButton(ActionEvent event) throws Exception{
        ObservableList<Appointment> appointments = appointmentDAO.getAllAppointments();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer and all associated appointments? ");
        Optional<ButtonType> confirm = alert.showAndWait();
        if (confirm.isPresent() && confirm.get()== ButtonType.OK){
            int customerID = customerTable.getSelectionModel().getSelectedItem().getId();
            appointmentDAO.deleteAppointment(customerID);

            String sql = "DELETE FROM customers WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);

            String sql2 = "DELETE FROM appointments WHERE Appointment_ID = ?";
            PreparedStatement ps2 = JDBC.connection.prepareStatement(sql2);

            int selectedCustomer = customerTable.getSelectionModel().getSelectedItem().getId();

            for(Appointment appointment: appointments){
                int customerAppt = appointment.getCustomerID();
                if(selectedCustomer == customerAppt){
                    ps2.setInt(1,appointment.getAppointmentID());
                    ps2.execute();
                }
            }
            ps.setInt(1, selectedCustomer);
            ps.execute();
            ObservableList<Customer> newCustomers = customerDAO.getAllCustomers();
            customerTable.setItems(newCustomers);

            //Added this code to reload the page after a customer is deleted
            //solved error with having to reload for associated appointments to show deleted
            Parent parent = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
            Scene scene = new Scene(parent);
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setTitle("Appointments");
            window.setScene(scene);
            window.show();

        }

    }

    /**
     * redirects to addCustomerView
     * @param event
     * @throws Exception
     */
    public void addCustomerButton(ActionEvent event) throws Exception{
        Parent parent = FXMLLoader.load(getClass().getResource("/view/addCustomer.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Add Customer");
        window.setScene(scene);
        window.show();

    }

    /**
     * redirects to updateCustomerView
     * lets user know if no customer is selected
     * @param event
     * @throws Exception
     */
    public void updateCustomerButton(ActionEvent event) throws Exception{

        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, "You must select a customer to edit it.", clickOkay);
            invalidInput.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/updateCustomer.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);

        //populate updateAppoinment page with selectedAppointment values
        updateCustomerController UCcontroller = loader.getController();
        UCcontroller.populateFields(selectedCustomer);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Update Customer");
        window.setScene(scene);
        window.show();


    }

    /**
     * displays all appointments in the appointment table
     * @param event
     * @throws SQLException
     */
    public void allButtonView(ActionEvent event) throws SQLException{
        ObservableList<Appointment> appointments = appointmentDAO.getAllAppointments();

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

    /**
     * displays all appointments within the current week in the appointment table
     * @param event
     * @throws SQLException
     */
    public void weekButtonView(ActionEvent event) throws SQLException{
        System.out.println("weekButtonview Called");
        ZonedDateTime start = ZonedDateTime.now(userDAO.getTimeZone()).withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime end = start.plusWeeks(1).withZoneSameInstant(ZoneOffset.UTC);
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointments as a LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID WHERE Start between ? AND ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String start_ = start.format(dtformat);
        String end_ = end.format(dtformat);
        ps.setString(1, start_);
        //ps.setString(2, endDate.toString());
        ps.setString(2, end_);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String type = rs.getString("Type");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            Timestamp startDateTime = rs.getTimestamp("Start");
            Timestamp endDateTime = rs.getTimestamp("End");
            int contactID = rs.getInt("Contact_ID");
            int _customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            Timestamp createDate = rs.getTimestamp("Create_Date");
            Timestamp lastUpdateDateTime = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");

            Appointment appointment = new Appointment(appointmentID, title, type, description, location, startDateTime, endDateTime, contactID,
                    _customerID, userID, createDate, lastUpdateDateTime, lastUpdatedBy);

            appointments.add(appointment);

        }
        System.out.println(appointments);
        ps.close();

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

        //DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("yyyy-MM-dd H:MM");

    }

    /**
     * displays all appointments within the current month in the appointment table
     * @param event
     * @throws SQLException
     */
    public void monthButtonView(ActionEvent event) throws SQLException {
        System.out.println("monthViewButton Called");
        ZonedDateTime start = ZonedDateTime.now(userDAO.getTimeZone()).withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime end = start.plusMonths(1).withZoneSameInstant(ZoneOffset.UTC);
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        String sql = "SELECT * FROM appointments as a LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID WHERE Start between ? AND ?";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String start_ = start.format(dtformat);
        String end_ = end.format(dtformat);
        ps.setString(1, start_);
        //ps.setString(2, endDate.toString());
        ps.setString(2, end_);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String type = rs.getString("Type");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            Timestamp startDateTime = rs.getTimestamp("Start");
            Timestamp endDateTime = rs.getTimestamp("End");
            int contactID = rs.getInt("Contact_ID");
            int _customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            Timestamp createDate = rs.getTimestamp("Create_Date");
            Timestamp lastUpdateDateTime = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");

            Appointment appointment = new Appointment(appointmentID, title, type, description, location, startDateTime, endDateTime, contactID,
                    _customerID, userID, createDate, lastUpdateDateTime, lastUpdatedBy);

            appointments.add(appointment);

        }
        System.out.println(appointments);
        ps.close();

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

        //DateTimeFormatter dtformat = DateTimeFormatter.ofPattern("yyyy-MM-dd H:MM");

    }

    /**
     * redirects to the reportsView
     * @param event
     * @throws IOException
     */
    public void reportButton(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/reports.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Reports");
        window.setScene(scene);
        window.show();
    }

}
