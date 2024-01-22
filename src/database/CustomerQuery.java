package database;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The CustomerQuery class provides methods for querying and managing customer data in a database.
 * It uses prepared statements to ensure secure and efficient database access.
 *
 * @author Daniel Akoko
 */
public class CustomerQuery {

    /**
     * Retrieves all customers from the "customers" table, joined with "first_level_divisions" and "countries" tables for additional information.
     * Uses a prepared statement with a complex JOIN query to fetch detailed customer data.
     * Creates Customer objects based on the retrieved data and adds them to an ObservableList.
     * Catches and prints any SQL exceptions.
     *
     * @return ObservableList of Customer objects containing all customers.
     */
    public static ObservableList<Customer> getCustomerList() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try {
            String query = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, " +
                            "customers.Create_Date, customers.Last_Update, customers.Postal_Code, " +
                            "customers.Phone, customers.Division_ID, first_level_divisions.Division, " +
                            "first_level_divisions.Country_ID, countries.Country " +
                            "FROM customers " +
                            "JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID " +
                            "JOIN countries ON countries.Country_ID = first_level_divisions.Country_ID " +
                            "ORDER BY customers.Customer_ID";
            PreparedStatement statement = JDBC.connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                // Extracting customer details from the result set
                int customerId = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                String postalCode = resultSet.getString("Postal_Code");
                String phone = resultSet.getString("Phone");
                int divisionId = resultSet.getInt("Division_ID");
                String divisionName = resultSet.getString("Division");
                int countryId = resultSet. getInt("Country_ID");
                String country = resultSet.getString("Country");

                // Creating Customer object and adding it to the list
                Customer customer = new Customer(customerId, customerName, address, postalCode, phone, divisionId, countryId, divisionName, country);
                customerList.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;
    }

    /**
     * Attempts to delete a customer based on their ID.
     * Uses a prepared statement with a DELETE query targeting the "customers" table.
     * Counts the number of affected rows (representing deleted customers).
     * Displays a confirmation alert based on the deletion success or failure.
     * Catches and prints any SQL exceptions.
     *
     * @param customerId The ID of the customer to be deleted.
     * @return The number of rows affected by the delete operation.
     */
    public static int deleteCustomer(int customerId) {
        int count = 0;
        try {
            String query = "DELETE FROM customers WHERE Customer_ID = ?";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            preparedStatement.setInt(1, customerId);
            count = preparedStatement.executeUpdate();

            // Displaying confirmation or failure message using an Alert
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

    /**
     * Updates the information of a specific customer based on their ID.
     * Uses a prepared statement with an UPDATE query targeting the "customers" table.
     * Sets the prepared statement parameters to the provided customer information.
     * Executes the query to update the customer record.
     * Catches and prints any SQL exceptions.
     *
     * @param customerId The ID of the customer to be updated
     * @param customerName The new name for the customer.
     * @param address The new address for the customer.
     * @param postalCode The new postal code for the customer.
     * @param phone The new phone number for the customer.
     * @param divisionId The new division ID for the customer.
     */
    public static void updateCustomer(int customerId, String customerName, String address, String postalCode, String phone, int divisionId) {
        String query ="UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ?, WHERE Customer_ID = ?";
        try (PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query)){
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

    /**
     * Adds a new customer to the "customers" table.
     * Uses a prepared statement with an INSERT query.
     * Sets the prepared statement parameters to the provided customer information.
     * Executes the query to insert the new customer record.
     * Catches and throws any SQL exceptions.
     *
     * @param customerName The name of the new customer.
     * @param address  The address of the new customer.
     * @param postalCode The postal code of the new customer.
     * @param phone The phone number of the new customer.
     * @param divisionId The division ID of the new customer.
     * @throws SQLException If a SQL exception occurs during the database interaction.
     */
    public static void addCustomer(String customerName, String address, String postalCode, String phone, int divisionId) throws SQLException {
        String query = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
        preparedStatement.setString(1, customerName);
        preparedStatement.setString(2, address);
        preparedStatement.setString(3, postalCode);
        preparedStatement.setString(4, phone);
        preparedStatement.setInt(5, divisionId);
        preparedStatement.execute();


    }

    /**
     * Retrieves a specific customer based on their ID.
     * Uses a prepared statement with a SELECT query that filters by the customer ID.
     * Creates a single Customer object from the first matching result in the result set.
     * Catches and throws any SQL exceptions.
     *
     * @param customerId The ID of the customer to retrieve.
     * @return Customer object if found, or null if not.
     * @throws SQLException If a SQL exception occurs during the database interaction.
     */
    public static Customer returnCustomerList(int customerId) throws SQLException {
        String query = "SELECT * FROM customers WHERE Customer_ID = ?";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
        preparedStatement.setInt(1, customerId);
        preparedStatement.execute();
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            // Extracting customer details from the result set
            int customer_ID = resultSet.getInt("Customer_ID");
            String customerName = resultSet.getString("Customer_Name");

            // Creating and returning Customer object
            return new Customer(customer_ID, customerName);
        }
        return null;
    }
}
