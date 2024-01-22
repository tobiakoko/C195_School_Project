package database;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Division;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * This class provides methods for querying and retrieving division data from a database.
 * It uses prepared statements to ensure secure and efficient database access.
 *
 * @author Daniel Akoko
 */
public class DivisionQuery {

    /**
     * Retrieves all divisions from the "first_level_divisions" table.
     * Uses a prepared statement with a SELECT query to fetch data.
     * Creates Division objects based on the retrieved data, including timestamps and converted local date/times.
     * Catches and throws any SQL exceptions.
     *
     * @return ObservableList of Division objects containing all divisions.
     */
    public static ObservableList<Division> getAllDivisionID() {
        ObservableList<Division> divisionList = FXCollections.observableArrayList();
        try{
            String sql = "SELECT * FROM first_level_divisions";
            PreparedStatement contacts = JDBC.connection.prepareStatement(sql);
            ResultSet rs = contacts.executeQuery();

            while (rs.next()) {
                // Extracting division details from the result set
                int divisionId = rs.getInt("Division_ID");
                String division = rs.getString("Contact_Name");
                int countryId = rs.getInt("Country_ID");
                Timestamp create_date = rs.getTimestamp("Create_Date");
                LocalDateTime createDate = create_date.toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                Timestamp last_update = rs.getTimestamp("Last_Update");
                LocalDateTime lastUpdate = last_update.toLocalDateTime();
                String lastUpdatedBy = rs.getString("Last_Updated_By");

                // Creating Division object and adding it to the list
                Division d = new Division(divisionId, division, createDate, createdBy, lastUpdate, lastUpdatedBy, countryId);
                divisionList.add(d);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return divisionList;
    }

    /**
     * Retrieves a specific division based on its ID.
     * Uses a prepared statement with a SELECT query that filters by the division ID.
     * Creates a single Division object from the first matching result in the result set.
     * Catches and throws any SQL exceptions.
     *
     * @param divisionId The ID of the division to retrieve.
     * @return Division object if found, or null if not.
     */
    public static Division returnDivisionLevel(int divisionId){
        try {
            String sql = "SELECT Division_ID, Division FROM first_level_divisions WHERE Division_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, divisionId);
            ps.execute();

            ResultSet rs = ps.getResultSet();

            rs.next();
            // Extracting division details from the result set
            int division_ID = rs.getInt("Division_ID");
            String division = rs.getString("Division");

            // Creating and returning Division object
            return new Division(division_ID, division);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves all divisions associated with a specific country based on its ID.
     * Uses a prepared statement with a SELECT query that filters by the country ID.
     * Creates Division objects based on the retrieved data, including timestamps and converted local date/times.
     * Catches and throws any SQL exceptions.
     *
     * @param countryId The ID of the country for which divisions are to be displayed.
     * @return ObservableList of Division objects containing division details for the specified country.
     * @throws SQLException If a SQL exception occurs during the database interaction.
     */
    public static ObservableList<Division> showDivision(int countryId) throws SQLException {
        ObservableList<Division> divisionCountryOptions = FXCollections.observableArrayList();

        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = " + countryId;
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.execute();
        ResultSet rs = ps.getResultSet();

        while(rs.next()) {
            // Extracting division details from the result set
            int divisionId = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            countryId = rs.getInt("Country_ID");
            Timestamp create_date = rs.getTimestamp("Create_Date");
            LocalDateTime createDate = create_date.toLocalDateTime();
            String createdBy = rs.getString("Created_By");
            Timestamp last_update = rs.getTimestamp("Last_Update");
            LocalDateTime lastUpdate = last_update.toLocalDateTime();
            String lastUpdatedBy = rs.getString("Last_Updated_By");

            // Creating Division object and adding it to the list
            Division division1 = new Division(divisionId, division, createDate, createdBy, lastUpdate, lastUpdatedBy, countryId);
            divisionCountryOptions.add(division1);
        }
        return divisionCountryOptions;
    }

}
