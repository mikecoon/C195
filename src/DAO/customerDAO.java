package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Accesses the databse to perform queries and actions related to customers*/
public class customerDAO {

    /**
     * returns an observable list of all customers from databse
     * @throws SQLException
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String sql = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Postal_Code, customers.Phone, customers.Division_ID, first_level_divisions.Division, first_level_divisions.Country_ID, countries.Country from customers INNER JOIN  first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID INNER JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            Integer customerID = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String customerAddress = rs.getString("Address");
            String customerPhone = rs.getString("Phone");
            String customerPostalCode = rs.getString("Postal_Code");
            String divisionID = rs.getString("Division_ID");
            String division = rs.getString("Division");
            String country = rs.getString("Country");

            Customer customer = new Customer(customerID,customerName,customerAddress,customerPhone,customerPostalCode,division,divisionID,country);
            customers.add(customer);
        }
        return customers;


    }

    /**
     * Retrieves a divisionID given a division from database
     * @param division division
     * @throws SQLException
     */
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

    /**
     * Retrieves an observable list of all divisions given a country from databse
     * @param country country
     * @throws SQLException
     */
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
