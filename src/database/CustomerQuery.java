package database;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
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
            String sql = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, customers.Create_Date, customers.Last_Update, customers.Postal_Code, customers.Phone, customers.Division_ID, first_level_divisions.Division, first_level_divisions.Country_ID, countries.Country FROM customers JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID JOIN countries ON countries.Country_ID = first_level_divisions.Country_ID ORDER BY customers.Customer_ID";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");
                String divisionName = rs.getString("Division");
                int countryId = rs. getInt("Country_ID");
                String country = rs.getString("Country");
                Customer c = new Customer(customerId, customerName, address, postalCode, phone, divisionId, countryId, divisionName, country);
                customerList.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;
    }

    public static int deleteCustomer(int customerId) {
        int count = 0;
        try {
            String sql = "DELETE FROM customers WHERE Customer_ID = ?";
            PreparedStatement deleteCustomer = JDBC.connection.prepareStatement(sql);
            deleteCustomer.setInt(1, customerId);
            count = deleteCustomer.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Customer DELETE");

            if(count > 0) {
                alert.setContentText("Customer " + customerId +" was successfully deleted.");
                alert.showAndWait();
            } else {
                alert.setContentText("Customer " + customerId +" failed to delete.");
                alert.showAndWait();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static void updateCustomer(int customerId, String customerName, String address, String postalCode, String phone, int divisionId) {
        try {
            String sql ="UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ?, WHERE Customer_ID = ?";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
            preparedStatement.setString(1, customerName);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, postalCode);
            preparedStatement.setString(4, phone);
            preparedStatement.setInt(5, divisionId);
            preparedStatement.setInt(6, customerId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addCustomer(String customerName, String address, String postalCode, String phone, int divisionId) throws SQLException {
        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
        preparedStatement.setString(1, customerName);
        preparedStatement.setString(2, address);
        preparedStatement.setString(3, postalCode);
        preparedStatement.setString(4, phone);
        preparedStatement.setInt(5, divisionId);
        preparedStatement.executeUpdate();


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
