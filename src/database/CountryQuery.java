package database;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The CountryQuery class provides methods for querying and retrieving country data from a database.
 * It uses prepared statements to ensure secure and efficient database access.
 *
 * @author Daniel Akoko
 */
public class CountryQuery {

    /**
     * Retrieves all countries from the "countries" table.
     * Uses a prepared statement with a SELECT query to fetch data.
     * Creates Country objects based on the retrieved data and adds them to an ObservableList.
     * Catches and throws any SQL exceptions.
     *
     * @return ObservableList of Country objects containing all available countries.
     */
    public static ObservableList<Country> getAllCountry() {
        ObservableList<Country> countryList = FXCollections.observableArrayList();
        try {
            String query = "SELECT Country_ID, Country FROM countries";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                // Extracting country details from the result set
                int countryId = resultSet.getInt("Country_ID");
                String countryName = resultSet.getString("Country");

                // Creating Country object and adding it to the list
                Country c = new Country(countryId, countryName);
                countryList.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return countryList;
    }

    /**
     * Retrieves a specific country from the database based on the provided country ID.
     * Uses a prepared statement with a SELECT query that filters by the country ID.
     * Creates a single Country object from the result set.
     * Catches and throws any SQL exceptions.
     *
     * @param countryId The ID of the country to retrieve.
     * @return The Country object if found, or null if not.
     */
    public static Country returnCountry(int countryId) {
        try {
            String query = "SELECT Country_ID, Country FROM countries WHERE Country_ID = ?";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            preparedStatement.setInt(1, countryId);
            ResultSet resultSet = preparedStatement.executeQuery();;

            if (resultSet.next()){
                // Extracting country details from the result set
                int country_Id = resultSet.getInt("Country_ID");
                String countryName = resultSet.getString("Country");

                // Returning Country object
                return new Country(country_Id, countryName);
            } else {
                // No result found, return null or handle accordingly
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Counts the number of customers associated with each country and returns the results.
     * Uses a JOIN between multiple tables to connect countries with customers through divisions.
     * Groups the results by country and retrieves the count for each.
     * Creates Country objects with "country" and "count" fields for each entry.
     * Catches and prints any SQL exceptions.
     *
     * @return ObservableList containing country and customer count pairs.
     */
    public static ObservableList<Country> countryTotals() {
        ObservableList<Country> customerCountry = FXCollections.observableArrayList();
        try {
            String query = "SELECT countries.Country, COUNT(customers.Customer_ID) AS Count FROM countries INNER JOIN first_level_divisions ON countries.Country_ID = first_level_divisions.Country_ID INNER JOIN customers ON customers.Division_ID = first_level_divisions.Division_ID group by countries.Country";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                // Extracting country details and customer count from the result set
                String month = resultSet.getString("Country");
                int monthCount = resultSet.getInt("Count");

                // Creating Country object and adding it to the list
                Country country1 = new Country(month, monthCount);
                customerCountry.add(country1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerCountry;
    }
}
