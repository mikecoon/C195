package DAO;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.firstLevelDivision;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Accesses the databse to perform queries and actions related to first level divisions*/
public class fldDAO extends firstLevelDivision{

    /**
     * initializes the fldDAO
     * @param countryID country ID
     * @param divID division ID
     * @param divisionName division Name
     */
    public fldDAO(String divisionName, int divID, int countryID) {
        super(divisionName, divID, countryID);
    }

    /**
     * Retrieves an observable list of all first level divisions from databse
     * @throws SQLException
     */
    public static ObservableList<fldDAO> getFLDs() throws SQLException {
        ObservableList<fldDAO> flds = FXCollections.observableArrayList();
        String sql = "SELECT * from first_level_divisions";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()){
            String divName = rs.getString("Division");
            int divID = rs.getInt("Division_ID");
            int countryID = rs.getInt("COUNTRY_ID");
            fldDAO fld = new fldDAO(divName,divID,countryID);
            flds.add(fld);

        }
        return flds;
    }
}
