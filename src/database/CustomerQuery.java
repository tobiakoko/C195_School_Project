package database;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CustomerQuery {

    public static ObservableList<Customer> getCustomerList() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Create_Date, customers.Last_Update, customers.Created_By, customers.Last_Updated_By, customers.Postal_Code, customers.Phone, customers.Division_ID, first_level_divisions.Division, first_level_divisions.Country_ID, countries.Country FROM customers JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID JOIN countries ON countries.Country_ID = first_level_divisions.Country_ID ORDER BY customers.Customer_ID";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                String createdBy = rs.getString("Created_By");
                String lastUpdatedBy = rs.getString("Last_Updated_By");
                int divisionId = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryId = rs. getInt("Country_ID");
                String country = rs.getString("Country");
                Customer c = new Customer(customerId, customerName, address, postalCode, phone, createdBy, lastUpdatedBy, divisionId, countryId, divisionName, country);
                customerList.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;
    }

    public static void deleteCustomer(int customerId) {
        try {
            String sql = "DELETE FROM customers WHERE Customer_ID = ?";
            PreparedStatement deleteCust = JDBC.connection.prepareStatement(sql);
            deleteCust.setInt(1, customerId);
            deleteCust.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCustomer(int customerId, String customerName, String address, String postalCode, String phone, String lastUpdatedBy, int divisionId, int countryId, Timestamp lastUpdated) {
        try {
            String sql ="UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update_By = ?, Last_Update = ?, Division_ID = ?, WHERE Customer_ID = ?";
            PreparedStatement updateCust = JDBC.connection.prepareStatement(sql);
            updateCust.setString(1, customerName);
            updateCust.setString(2, address);
            updateCust.setString(3, postalCode);
            updateCust.setString(4, phone);
            updateCust.setString(5, lastUpdatedBy);
            updateCust.setTimestamp(6, lastUpdated);
            updateCust.setInt(7, divisionId);
            updateCust.setInt(8, customerId);
            updateCust.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addCustomer(String customerName, String address, String postalCode, String phone, LocalDateTime createdDate, LocalDateTime lastUpdate, int divisionId) throws SQLException {
        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Create_Date, Last_Update, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement insertCust = JDBC.connection.prepareStatement(sql);
        insertCust.setString(1, customerName);
        insertCust.setString(2, address);
        insertCust.setString(3, postalCode);
        insertCust.setString(4, phone);
        insertCust.setTimestamp(5, Timestamp.valueOf(createdDate));
        insertCust.setTimestamp(6, Timestamp.valueOf(lastUpdate));
        insertCust.setInt(1, divisionId);
        insertCust.executeUpdate();


    }

    public static Customer returnCustomerList(int customerId) throws SQLException {
        String sql = "SELECT * FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerId);
        ps.execute();
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int customer_ID = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");

            Customer c = new Customer(customer_ID, customerName);
            return c;
        }
        return null;
    }
}
