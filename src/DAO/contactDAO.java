package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class contactDAO {

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
