package controller;

import DAO.customerDAO;
import DAO.userDAO;
import DAO.countryDAO;
import helper.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class addCustomerController {

    @FXML TextField addCustomerID;
    @FXML TextField addCustomerName;
    @FXML TextField addCustomerAddress;
    @FXML TextField addCustomerPhoneNumber;
    @FXML TextField addCustomerZip;
    @FXML ComboBox<String> addCustomerCountry;
    @FXML ComboBox<String> addCustomerDivision;



    public void addCustomerCancelButton(ActionEvent event) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Appointments");
        window.setScene(scene);
        window.show();
    }

    public void addCustomerSaveButton(ActionEvent event) throws Exception{
        String id = addCustomerID.getText();
        String name = addCustomerName.getText();
        String address = addCustomerAddress.getText();
        String phone = addCustomerPhoneNumber.getText();
        String zip = addCustomerZip.getText();
        String country = addCustomerCountry.getValue();
        String division = addCustomerDivision.getValue();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if ( id.isBlank() ||country.isBlank() || division.isBlank() || name.isBlank() || address.isBlank() || zip.isBlank() ||
                phone.isBlank()){

            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, "Make sure all fields are populated correctly.", clickOkay);
            invalidInput.showAndWait();
            return;

        }

        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, zip);
        ps.setString(4, phone);
        ps.setString(5, ZonedDateTime.now(ZoneOffset.UTC).format(formatter));
        ps.setString(6, userDAO.getCurrentUser().getUserName());
        ps.setString(7, ZonedDateTime.now(ZoneOffset.UTC).format(formatter));
        ps.setString(8, userDAO.getCurrentUser().getUserName());
        //implement division finder
        ps.setInt(9, customerDAO.getDivisionID(division));

        try{
            ps.executeUpdate();
            ps.close();
        } catch(SQLException e){
            e.printStackTrace();
            ps.close();
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert alert = new Alert(Alert.AlertType.WARNING, "Unable to add customer, try again.", clickOkay);
            alert.showAndWait();

        }

        ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Added Customer", clickOkay);
        alert.showAndWait();
        Parent parent = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Appointments");
        window.setScene(scene);
        window.show();

    }

    public void initialize() throws SQLException {
        //I need to add a listener so when the country is selected the divsion box auto populates
        //could turn listener to separate function..
        try{
            addCustomerCountry.setItems(countryDAO.getCountries());
        }catch(SQLException e){
            e.printStackTrace();
        }
        addCustomerCountry.valueProperty().addListener((obs, init, updated) -> {
            if (updated != null){
                try{
                    addCustomerDivision.setItems(customerDAO.getDivisionByCountry(addCustomerCountry.getValue()));
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }else {
                addCustomerDivision.getItems().clear();
            }
        });


    }
}
