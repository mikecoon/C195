package DAO;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.*;
import java.time.LocalDate;

public class appointmentDAO {

    public static ObservableList<Appointment> getAllAppointments() throws SQLException{

        ObservableList<Appointment> appointmentOL = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        //System.out.println(rs);
        while(rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String type = rs.getString("Type");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            Timestamp startDateTime = rs.getTimestamp("Start");
            Timestamp endDateTime = rs.getTimestamp("End");
            int contactID = rs.getInt("Contact_ID");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            Timestamp createDate = rs.getTimestamp("Create_Date");
            Timestamp lastUpdateDateTime = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            //System.out.println(startDateTime);
            Appointment appointment = new Appointment(appointmentID, title, type, description, location, startDateTime, endDateTime, contactID,
                    customerID,userID,createDate,lastUpdateDateTime,lastUpdatedBy);
            //System.out.println(appointment.getStartDateTime());
            appointmentOL.add(appointment);


        }
        ps.close();
        return appointmentOL;
    }
    public static ObservableList<Appointment> getCustomerAppointments(LocalDate startDate, LocalDate endDate, Integer customerID) throws SQLException {
        String sql = "SELECT * FROM appointments as a LEFT OUTER JOIN contacts as c ON a.Contact_ID = c.Contact_ID WHERE datediff(a.Start, ?) = 0 AND datediff(a.End, ?) = 0 AND Customer_ID = ?;";
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        ps.setString(1, startDate.toString());
        ps.setString(2, endDate.toString());
        ps.setInt(3, customerID);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String type = rs.getString("Type");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            Timestamp startDateTime = rs.getTimestamp("Start");
            Timestamp endDateTime = rs.getTimestamp("End");
            int contactID = rs.getInt("Contact_ID");
            int _customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            Timestamp createDate = rs.getTimestamp("Create_Date");
            Timestamp lastUpdateDateTime = rs.getTimestamp("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            //System.out.println(startDateTime);
            Appointment appointment = new Appointment(appointmentID, title, type, description, location, startDateTime, endDateTime, contactID,
                    _customerID, userID, createDate, lastUpdateDateTime, lastUpdatedBy);
            //System.out.println(appointment.getStartDateTime());
            appointments.add(appointment);

        }
        ps.close();
        return appointments;
    }
}
