package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Accesses the databse to perform queries and actions related to countries*/
public class countryDAO {

    /**
     * Retrieves an observable list of all countries from databse
     * @throws SQLException
     */
    public static ObservableList<String> getCountries() throws SQLException {
        ObservableList<String> countries = FXCollections.observableArrayList();
        String sql = "SELECT DISTINCT Country FROM countries";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String countryName = rs.getString("Country");
            countries.add(countryName);
        }
        ps.close();
        return countries;
    }
}
