package DAO;

import helper.JDBC;

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
        return contactID;
    }
}
