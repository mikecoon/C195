package controller;

import DAO.appointmentDAO;
import DAO.contactDAO;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Report;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/** Handles generating various reports for appointments, customers, contacts, etc.*/
public class reportsController implements Initializable {
    @FXML TableView<Appointment> contactTable;
    @FXML TableView typeTable;
    @FXML TableView monthTable;
    @FXML TableView countryTable;
    @FXML TableColumn<?,?> contactApptID;
    @FXML TableColumn<?,?> contactTitle;
    @FXML TableColumn<?,?> contactType;
    @FXML TableColumn<?,?> contactDescription;
    @FXML TableColumn<?,?> contactStart;
    @FXML TableColumn<?,?> contactEnd;
    @FXML TableColumn<?,?> contactCustomerID;
    @FXML TableColumn<?,?> typeType;
    @FXML TableColumn<?,?> typeTotal;
    @FXML TableColumn<?,?> monthMonth;
    @FXML TableColumn<?,?> monthTotal;
    @FXML TableColumn<?,?> countryCountry;
    @FXML TableColumn<?,?> countryTotal;
    @FXML ComboBox<String> contactDropDown;
    @FXML Button backButton;

    /**
     * Populates the country table with total appointments for each country
     * @throws SQLException
     */
    public void setCountryTable() throws SQLException {
        try {
            System.out.println("setCountry called");
            ObservableList<Report> countries = FXCollections.observableArrayList();

            String sql = "select countries.Country, count(*) as total from customers inner join first_level_divisions on customers.Division_ID = first_level_divisions.Division_ID inner join countries on countries.Country_ID = first_level_divisions.Country_ID where  customers.Division_ID = first_level_divisions.Division_ID group by first_level_divisions.Country_ID order by count(*) desc";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("Country");
                int total = rs.getInt("total");
                Report reportCountry = new Report(name, total);
                countries.add(reportCountry);
                //System.out.println(reportCountry);

            }

            countryTable.setItems(countries);
        }catch (SQLException e){
            System.out.println(e);
        }
    }

    /**
     * Populates the month table with total appointments for each month
     * @throws SQLException
     */
    public void setMonthTable() throws SQLException {
        try {
            System.out.println("setMonth called");
            ObservableList<Report> months = FXCollections.observableArrayList();

            String sql = "SELECT MONTHNAME(Start) as \"month\", COUNT(MONTH(Start)) as \"total\" from appointments GROUP BY Month";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("month");
                int total = rs.getInt("total");
                Report reportMonth = new Report(name, total);
                months.add(reportMonth);
                //System.out.println(reportCountry);

            }

            monthTable.setItems(months);
        }catch (SQLException e){
            System.out.println(e);
        }
    }

    /**
     * Populates the type table with total appointments for each type
     * @throws SQLException
     */
    public void setTypeTable() throws SQLException{
        try {
            System.out.println("setType called");
            ObservableList<Report> types = FXCollections.observableArrayList();

            String sql = "SELECT Type, COUNT(Type) as \"total\" FROM appointments GROUP BY Type";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("Type");
                int total = rs.getInt("total");
                Report reportType = new Report(name, total);
                types.add(reportType);
                //System.out.println(reportCountry);

            }

            typeTable.setItems(types);
        }catch (SQLException e){
            System.out.println(e);
        }
    }

    /**
     * Populates the contact table with data regarding a contacts appointments
     * @throws SQLException
     */
    @FXML
    public void setContactTable() throws SQLException{
        System.out.println("setContact called");
        Integer id = 0;
        String selectedContact = contactDropDown.getSelectionModel().getSelectedItem();
        ObservableList<String> allContacts = contactDAO.getContactNames();

        for(String contact: allContacts){
            //System.out.println(contact);
            //System.out.println(selectedContact);
            if(contact.equals(selectedContact)){
                id = contactDAO.getIDbyName(selectedContact);
                System.out.println(id);
            }
        }

        ObservableList<Appointment> appointments = appointmentDAO.getAllAppointments();
        ObservableList<Appointment> populateTable = FXCollections.observableArrayList();

        for(Appointment appointment: appointments){
            //System.out.println(id);
            //System.out.println(appointment.getContactID());
            if(id == appointment.getContactID()){
                System.out.println("were equal");
                populateTable.add(appointment);
            }
        }
        contactTable.setItems(populateTable);


    }

    /**
     * exits reports page, redirects back to appointments page
     * @throws IOException
     */
    public void backButton(ActionEvent event) throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Appointments");
        window.setScene(scene);
        window.show();
    }

    /**
     * Initializes reports tables by calling local methods to do so
     * @param resources
     * @param location
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
        countryCountry.setCellValueFactory(new PropertyValueFactory<>("name"));
        countryTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        monthMonth.setCellValueFactory(new PropertyValueFactory<>("name"));
        monthTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        typeType.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        contactApptID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        contactTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        contactType.setCellValueFactory(new PropertyValueFactory<>("type"));
        contactDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        contactStart.setCellValueFactory(new PropertyValueFactory<>("startDateTime"));
        contactEnd.setCellValueFactory(new PropertyValueFactory<>("endDateTime"));
        contactCustomerID.setCellValueFactory(new PropertyValueFactory<>("total"));


        try {

            ObservableList<String> contacts = contactDAO.getContactNames();
            contactDropDown.setItems(contacts);
            setCountryTable();
            setMonthTable();
            setTypeTable();
            setContactTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
