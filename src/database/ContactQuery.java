package database;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class provides methods for querying and retrieving contact data from a database.
 * It uses prepared statements to ensure secure and efficient database access.
 *
 * @author Daniel Akoko
 */
public class ContactQuery {

    /**
     * Retrieves all contacts from the "contacts" table.
     * Uses a prepared statement with a SELECT query to fetch data.
     * Creates Contact objects based on the retrieved data and adds them to an ObservableList.
     * Catches and throws any SQL exceptions.
     *
     * @return ObservableList of Contact objects containing details like contact ID, contact name, and email.
     */
    public static ObservableList<Contact> getAllContacts() {
        ObservableList<Contact> contactList = FXCollections.observableArrayList();
        try{
            String query = "SELECT Contact_ID, Contact_Name, Email FROM contacts";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                // Extracting contact details from the result set
                int contactId = resultSet.getInt("Contact_ID");
                String contactName = resultSet.getString("Contact_Name");
                String contactEmail = resultSet.getString("Email");

                // Creating Contact object and adding it to the list
                Contact d = new Contact(contactId, contactName, contactEmail);
                contactList.add(d);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return contactList;
    }

    /**
     * Retrieves a specific contact based on their ID.
     * Uses a prepared statement with a SELECT query that filters by the contact ID.
     * Creates a single Contact object from the result set.
     * Catches and throws any SQL exceptions.
     *
     * @param contactID The ID of the contact to retrieve.
     * @return Contact object containing details like contact ID, contact name, and email.
     */
    public static Contact returnContactList(int contactID) {
        try{
            String query = "SELECT * FROM contacts WHERE Contact_ID = ?";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);

            // Setting the parameter for the prepared statement
            preparedStatement.setInt(1, contactID);
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            resultSet.next();

            // Extracting contact details from the result set
            int contactId = resultSet.getInt("Contact_ID");
            String contactName = resultSet.getString("Contact_Name");
            String contactEmail = resultSet.getString("Email");
            Contact s = new Contact(contactId, contactName, contactEmail);
            return s;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the ID of a contact based on their name.
     * Uses a prepared statement with a SELECT query that filters by the contact name.
     * Expects only one matching contact and returns its ID as an integer.
     * Catches and throws any SQL exceptions.
     *
     * @param contactName The name of the contact.
     * @return The contact ID associated with the provided contact name.
     * @throws SQLException If a database access error occurs or if multiple contacts with the same name are found.
     */
    public static int returnContactId(String contactName) throws SQLException {
        int contactId = 0;
        String query = "SELECT * FROM contacts WHERE Contact_Name = ?";
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);

        // Setting the parameter for the prepared statement
        preparedStatement.setString(1, contactName);
        ResultSet resultSet = preparedStatement.executeQuery();

        while(resultSet.next()) {
            // Extracting contact ID from the result set
            contactId = resultSet.getInt("Contact_ID");
        }
        return contactId;
    }
}
