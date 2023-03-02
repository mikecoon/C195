package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Accesses the databse to perform queries and actions related to contacts*/
public class contactDAO {

    /**
     * Retrieves a contact given its ID from the database
     * @param contactID contacts ID
     * @throws SQLException
     */
    public static String getContactByID(String contactID) throws SQLException {
        String sql = "SELECT * FROM contacts WHERE Contact_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, contactID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            contactID = rs.getString("Contact_ID");
        }
        ps.close();
        return contactID;
    }

    /**
     * Retrieves a contacts ID given a contact name from databse
     * @param contactName contacts name
     * @throws SQLException
     */
    public static Integer getIDbyName(String contactName) throws SQLException{
        String sql = "SELECT * FROM contacts WHERE Contact_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, contactName);
        Integer id = 0;
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            id = rs.getInt("Contact_ID");
        }
        return id;
    }

    /**
     * Retrieves a contacts name given its ID from databse
     * @param contactID contactsID
     * @throws SQLException
     */
    public static String givenIDgetName(Integer contactID) throws SQLException{
        String sql = "SELECT * FROM contacts WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, contactID);
        String name = null;
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            name = rs.getString("Contact_Name");
        }
        return name;
    }

    /**
     * Retrieves an observable list of all contact names from databse
     * @throws SQLException
     */
    public static ObservableList<String> getContactNames() throws SQLException {
        ObservableList<String> contactNames = FXCollections.observableArrayList();
        String sql = "SELECT * from contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            contactNames.add(rs.getString("Contact_Name"));
        }
        ps.close();
        return contactNames;
    }

}
