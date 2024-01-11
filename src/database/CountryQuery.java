package database;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryQuery {

    public static ObservableList<Country> getAllCountry() {
        ObservableList<Country> countryList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT Country_ID, Country FROM countries";
            PreparedStatement country = JDBC.connection.prepareStatement(sql);
            ResultSet rs = country.executeQuery();

            while(rs.next()) {
                int countryId = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Country c = new Country(countryId, countryName);
                countryList.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return countryList;
    }

    public static Country returnCountry(int countryId) {
        try {
            String sql = "SELECT Country_ID, Country FROM countries WHERE Country_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, countryId);
            ps.execute();

            ResultSet rs = ps.getResultSet();

            rs.next();
            int country_Id = rs.getInt("Country_ID");
            String countryName = rs.getString("Country");
            Country c = new Country(country_Id, countryName);
            return c;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static ObservableList<Country> countryTotals() {
        ObservableList<Country> customerCountry = FXCollections.observableArrayList();
        try {
            String sql = "SELECT countries.Country, COUNT(customers.Customer_ID) AS Count FROM countries INNER JOIN first_level_divisions ON countries.Country_ID = first_level_divisions.Country_ID INNER JOIN customers ON customers.Division_ID = first_level_divisions.Division_ID group by countries.Country";
            PreparedStatement country = JDBC.connection.prepareStatement(sql);
            ResultSet rs = country.executeQuery();

            while(rs.next()) {
                String month = rs.getString("Country");
                int monthCount = rs.getInt("Count");
                Country results = new Country(month, monthCount);
                customerCountry.add(results);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerCountry;
    }
}
