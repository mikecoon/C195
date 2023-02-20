package DAO;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class countryDAO {

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
