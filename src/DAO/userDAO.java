package DAO;

import helper.JDBC;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

public class userDAO {
    private static ZoneId timeZone;
    private static User currentUser;

    //Attempts to login based on given username and password.
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
    public static ZoneId getTimeZone(){
        return timeZone;
    }
    public static User getCurrentUser(){
        return currentUser;
    }

}
