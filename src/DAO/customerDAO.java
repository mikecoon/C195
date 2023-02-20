package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class customerDAO {
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String sql = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, customers.Phone, customers.Division_ID, first_level_divisions.Division from customers INNER JOIN  first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            Integer customerID = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String customerAddress = rs.getString("Address");
            String customerPhone = rs.getString("Phone");
            String customerPostalCode = rs.getString("Postal_Code");

            Customer customer = new Customer(customerID,customerName,customerAddress,customerPhone,customerPostalCode);
            customers.add(customer);
        }
        return customers;


    }
}
