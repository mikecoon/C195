package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

/** Accesses the databse to perform queries and actions related to users*/
public class userDAO {
    private static ZoneId timeZone;
    private static User currentUser;

    /**
     * Attempts to login based on given username and password.
     * @param password password
     * @param userName user name
     * @throws SQLException
     */
    public static boolean AttemptLogin(String userName, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE " + "User_Name = ? AND Password = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userName);
        ps.setString(2, password);
        //This reads the select query results and stores them in rs.
        ResultSet rs = ps.executeQuery();
        //If result.next() is false that means there is no users matching the username and password
        if (!rs.next()) {
            ps.close();
            System.out.println("Invalid username or password, please try again.");
            return false;
        } else {
            System.out.println("Log in successful!");
            currentUser = new User(rs.getInt("User_ID"), rs.getString("User_Name"), rs.getString("Password"));
            timeZone = ZoneId.systemDefault();
            ps.close();
            return true;
        }

    }

    /**
     * Gets the users time zone
     */
    public static ZoneId getTimeZone(){
        return timeZone;
    }

    /**
     * Gets the current user
     */
    public static User getCurrentUser(){
        return currentUser;
    }

    /**
     * Returns an observable list of all userIDs from the database
     */
    public static ObservableList<Integer> getUserIDs() throws SQLException{
        ObservableList<Integer> userIDs = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT User_ID FROM users;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while ( rs.next() ) {
            userIDs.add(rs.getInt("User_ID"));
        }
        ps.close();
        return userIDs;
    }

}
