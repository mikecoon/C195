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
    public static Integer getDivisionID(String division) throws SQLException {
        String sql = "SELECT Division, Division_ID FROM first_level_divisions WHERE Division = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1, division);
        ResultSet rs = ps.executeQuery();
        Integer divisionID = 0;

        while (rs.next()) {
            divisionID = rs.getInt("Division_ID");
        }
        ps.close();
        return divisionID;
    }

    //check if theres another way to do this...
    public static ObservableList<String> getDivisionByCountry(String country) throws SQLException {
        ObservableList<String> divisions = FXCollections.observableArrayList();
        String sql = "SELECT c.Country, c.Country_ID,  d.Division_ID, d.Division FROM countries as c RIGHT OUTER JOIN first_level_divisions AS d ON c.Country_ID = d.Country_ID WHERE c.Country = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1, country);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            divisions.add(rs.getString("Division"));
        }
        ps.close();
        return divisions;

    }
}
