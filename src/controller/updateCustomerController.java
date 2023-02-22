package controller;

import DAO.countryDAO;
import DAO.customerDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Customer;

import javafx.scene.control.*;

import java.sql.SQLException;

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
    public void populateFields(Customer selectedCustomer) throws SQLException {
        updateCustomerID.setText(selectedCustomer.getId().toString());
        updateCustomerName.setText(selectedCustomer.getName());
        updateCustomerAddress.setText(selectedCustomer.getAddress());
        updateCustomerPhoneNumber.setText(selectedCustomer.getPhoneNumber());
        updateCustomerZip.setText(selectedCustomer.getPostalCode());
        updateCustomerCountry.setItems(countryDAO.getCountries());
        updateCustomerCountry.getSelectionModel().select(selectedCustomer.getCountry());
        updateCustomerDivision.setItems(customerDAO.getDivisionByCountry(selectedCustomer.getCountry()));
        updateCustomerDivision.getSelectionModel().select(selectedCustomer.getDivision());

    }
}
