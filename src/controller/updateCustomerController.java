package controller;

import helper.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Customer;
import DAO.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/** Logic for updating a customer within the database*/
public class updateCustomerController {
    @FXML TextField updateCustomerID;
    @FXML TextField updateCustomerName;
    @FXML TextField updateCustomerAddress;
    @FXML TextField updateCustomerPhoneNumber;
    @FXML TextField updateCustomerZip;
    @FXML ComboBox<String> updateCustomerCountry;
    @FXML ComboBox<String> updateCustomerDivision;
    @FXML Button updateCustomerSaveButton;
    @FXML Button updateCustomerCancelButton;

    /**
     * cancels a customer update, redirects back to appointmentView
     * @param event
     * @throws Exception
     */
    public void updateCustomerCancelButton(ActionEvent event) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Appointments");
        window.setScene(scene);
        window.show();
    }

    /**
     * Method saves the new customer to database
     * Lets you know if input is invalid
     * @throws Exception
     * @param event
     */
    public void updateCustomerSaveButton(ActionEvent event) throws Exception{
        String id = updateCustomerID.getText();
        String name = updateCustomerName.getText();
        String address = updateCustomerAddress.getText();
        String phone = updateCustomerPhoneNumber.getText();
        String zip = updateCustomerZip.getText();
        String country = updateCustomerCountry.getValue();
        String division = updateCustomerDivision.getValue();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if ( id.isBlank() ||country.isBlank() || division.isBlank() || name.isBlank() || address.isBlank() || zip.isBlank() ||
                phone.isBlank()){

            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert invalidInput = new Alert(Alert.AlertType.WARNING, "Make sure all fields are populated correctly.", clickOkay);
            invalidInput.showAndWait();
            return;

        }

        String sql = "UPDATE customers SET Customer_Name=?, Address=?, Postal_Code=?, Phone=?, Last_Update=?, Last_Updated_By=?, Division_ID=? WHERE Customer_ID=?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, zip);
        ps.setString(4, phone);
        ps.setString(5, ZonedDateTime.now(ZoneOffset.UTC).format(formatter));
        ps.setString(6, userDAO.getCurrentUser().getUserName());
        //implement division finder
        ps.setInt(7, customerDAO.getDivisionID(division));
        ps.setString(8,id);

        try{
            ps.executeUpdate();
            ps.close();
        } catch(SQLException e){
            e.printStackTrace();
            ps.close();
            ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            Alert alert = new Alert(Alert.AlertType.WARNING, "Unable to update customer, try again.", clickOkay);
            alert.showAndWait();

        }

        ButtonType clickOkay = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Updated Customer", clickOkay);
        alert.showAndWait();
        Parent parent = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setTitle("Appointments");
        window.setScene(scene);
        window.show();

    }

    /**
     * Initializes the addCustomer view by populating necessary combo boxes
     * @throws SQLException
     */
    public void populateFields(Customer selectedCustomer) throws SQLException {
        updateCustomerID.setText(selectedCustomer.getId().toString());
        updateCustomerName.setText(selectedCustomer.getName());
        updateCustomerAddress.setText(selectedCustomer.getAddress());
        updateCustomerPhoneNumber.setText(selectedCustomer.getPhoneNumber());
        updateCustomerZip.setText(selectedCustomer.getPostalCode());
        updateCustomerDivision.setItems(customerDAO.getDivisionByCountry(selectedCustomer.getCountry()));
        updateCustomerDivision.getSelectionModel().select(selectedCustomer.getDivision());
        updateCustomerCountry.setItems(countryDAO.getCountries());
        updateCustomerCountry.getSelectionModel().select(selectedCustomer.getCountry());



        /*
        updateCustomerCountry.setItems(countryDAO.getCountries());
        //not sure how to assign a country to a customer when its not in the DB, so ill
        //updateCustomerCountry.getSelectionModel().select(selectedCustomer.getCountry());

        updateCustomerCountry.valueProperty().addListener((obs, init, updated) -> {
            if (updated != null){
                try{
                    updateCustomerDivision.setItems(customerDAO.getDivisionByCountry(updateCustomerCountry.getValue()));
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }else {
                updateCustomerDivision.getItems().clear();
            }
        });


        //updateCustomerDivision.setItems(customerDAO.getDivisionByCountry(selectedCustomer.getCountry()));
        //updateCustomerDivision.getSelectionModel().select(selectedCustomer.getDivision());
        */
    }

}
