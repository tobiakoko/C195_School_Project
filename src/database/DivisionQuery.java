package database;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Division;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDateTime;


public class DivisionQuery {

    public static ObservableList<Division> getAllDivisionID() {
        ObservableList<Division> divisionList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * FROM first_level_divisions";
            PreparedStatement contacts = JDBC.connection.prepareStatement(sql);
            ResultSet rs = contacts.executeQuery();

            while (rs.next()) {
                int divisionId = rs.getInt("Division_ID");
                String division = rs.getString("Contact_Name");
                int countryId = rs.getInt("Country_ID");
                Timestamp create_date = rs.getTimestamp("Create_Date");
                LocalDateTime createDate = create_date.toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp last_update = rs.getTimestamp("Last_Update");
                LocalDateTime lastUpdate = last_update.toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                Division d = new Division(divisionId, division, createDate, createdBy, lastUpdate, lastUpdatedBy, countryId);
                divisionList.add(d);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return divisionList;
    }

    public static Division returnDivisionLevel(int divisionId){
        try {
            String sql = "SELECT Division_ID, Division FROM first_level_divisions WHERE Division_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, divisionId);
            ps.execute();

            ResultSet rs = ps.getResultSet();

            rs.next();
            int division_ID = rs.getInt("Divison_ID");
            String division = rs.getString("Division");
            Division s = new Division(division_ID, division);
            return s;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObservableList<Division> displayDivision(int countryId) throws SQLException {
        ObservableList<Division> divisionCountryOptions = FXCollections.observableArrayList();

        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = " + countryId;
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while(rs.next()) {
            int divisionId = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            countryId = rs.getInt("Country_ID");
            Timestamp create_date = rs.getTimestamp("Create_Date");
            LocalDateTime createDate = create_date.toLocalDateTime();
            String createdBy = rs.getString("Created_By");
            Timestamp last_update = rs.getTimestamp("Last_Update");
            LocalDateTime lastUpdate = last_update.toLocalDateTime();
            String lastUpdatedBy = rs.getString("Last_Updated_By");

            Division c = new Division(divisionId, division, createDate, createdBy, lastUpdate, lastUpdatedBy, countryId);
            divisionCountryOptions.add(c);
        }
        return divisionCountryOptions;
    }

}
