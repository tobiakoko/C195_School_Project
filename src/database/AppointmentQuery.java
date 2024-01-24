package database;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * A utility class for handling database queries and appointment data retrieval from the database.
 * It uses prepared statements to ensure secure and efficient database access.
 */
public class AppointmentQuery {

    //Static variables for defining time intervals
    static LocalDateTime now = LocalDateTime.now();
    static LocalDateTime weekLater = now.plusWeeks(1);
    static LocalDateTime monthLater = now.plusMonths(1);

    /**
     * Fetches all appointments from the "appointments" table, joined with the "contacts" table.
     * Uses a prepared statement with a SELECT query to retrieve data.
     * Loops through the result set and creates Appointment objects based on the retrieved data.
     * Catches and prints any SQL exceptions.
     *
     * @return ObservableList of Appointment objects
     */
    public static ObservableList<Appointment> getAppointmentList() {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM appointments JOIN contacts ON appointments.Contact_ID = contacts.CONTACT_ID ORDER BY appointments.Appointment_ID";
            PreparedStatement statement = JDBC.connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                //Extracting appointment details from the result set
                int appointmentId = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                int contactId = resultSet.getInt("Contact_ID");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
                int customerId = resultSet.getInt("Customer_ID");
                int userId = resultSet.getInt("User_ID");

                // Creating Appointment object and adding it to the list
                Appointment c = new Appointment(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
                appointmentList.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentList;
    }

    /**
     * Updates an existing appointment with the provided information.
     * Uses a prepared statement with an UPDATE query.
     * Sets the prepared statement parameters based on the provided arguments.
     * Executes the statement and throws a RuntimeException if an SQL exception occurs.
     *
     * @param appointmentId The unique identifier for the appointment.
     * @param title         The title of the appointment.
     * @param description   The description of the appointment.
     * @param location      The location of the appointment.
     * @param type          The type of the appointment.
     * @param start         The start date and time of the appointment.
     * @param end           The end date and time of the appointment.
     * @param customerId    The ID of the customer associated with the appointment.
     * @param userId        The ID of the user associated with the appointment.
     * @param contactId     The ID of the contact related to the appointment.
     */
    public static void modifyAppointment(int appointmentId, String title, String description, String location, String type,
                                         LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId) {
        try {
            String query = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_Id = ?, User_ID = ?, Contact_ID  = ? WHERE Appointment_ID = ?";
            PreparedStatement statement = JDBC.connection.prepareStatement(query);

            statement.setString(1, title);
            statement.setString(2, description);
            statement.setString(3, location);
            statement.setString(4, type);
            statement.setTimestamp(5, Timestamp.valueOf(start));
            statement.setTimestamp(6, Timestamp.valueOf(end));
            statement.setInt(7, customerId);
            statement.setInt(8, userId);
            statement.setInt(9, contactId);
            statement.setInt(10, appointmentId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Inserts a new appointment into the "appointments" table.
     * Uses a prepared statement with an INSERT query.
     * Sets the prepared statement parameters based on the provided arguments.
     * Executes the statement and throws a RuntimeException if an SQL exception occurs.
     *
     * @param title         The title of the appointment.
     * @param description   The description of the appointment.
     * @param location      The location of the appointment.
     * @param type          The type of the appointment.
     * @param start         The start date and time of the appointment.
     * @param end           The end date and time of the appointment.
     * @param customerId    The ID of the customer associated with the appointment.
     * @param userId        The ID of the user associated with the appointment.
     * @param contactId     The ID of the contact related to the appointment.
     */
    public static void addAppointment(String title, String description, String location, String type,
                                      LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId){
        try {
            String query = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, USER_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = JDBC.connection.prepareStatement(query);

            statement.setString(1, title);
            statement.setString(2, description);
            statement.setString(3, location);
            statement.setString(4, type);
            statement.setTimestamp(5, Timestamp.valueOf(start));
            statement.setTimestamp(6, Timestamp.valueOf(end));
            statement.setInt(7, customerId);
            statement.setInt(8,userId);
            statement.setInt(9, contactId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches appointments scheduled within the current week (starting from now).
     * Uses a prepared statement with a SELECT query that filters based on the start date.
     * Creates Appointment objects from the result set and adds them to an ObservableList.
     * Catches and prints any SQL exceptions.
     * Returns the ObservableList of appointments for the current week.
     *
     * @return ObservableList of Appointment objects.
     */
    public static ObservableList<Appointment> getApptByWeek(){
        ObservableList<Appointment> weekAppointments = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM appointments INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID WHERE Start >=? AND Start <= ?";
            PreparedStatement statement = JDBC.connection.prepareStatement(query);
            statement.setTimestamp(1, Timestamp.valueOf(now));
            statement.setTimestamp(2, Timestamp.valueOf(weekLater));

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int appointmentId = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
                int customerId = resultSet.getInt("Customer_ID");
                int userId = resultSet.getInt("User_ID");
                int contactId = resultSet.getInt("Contact_ID");
                Appointment byWeek = new Appointment(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
                weekAppointments.add(byWeek);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return weekAppointments;
    }

    /**
     * Deletes an appointment from the "appointments" table based on the provided ID.
     * Uses a prepared statement with a DELETE query.
     * Sets the prepared statement parameter to the appointment ID.
     * Executes the statement and throws a RuntimeException if an SQL exception occurs.
     *
     * @param appointmentId appointment's ID
     */
    public static void deleteAppointment(int appointmentId){
        try {
            String query = "DELETE FROM appointments WHERE Appointment_ID = ?";
            PreparedStatement statement = JDBC.connection.prepareStatement(query);
            statement.setInt(1, appointmentId);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves appointments for the current month (starting from now).
     * Uses a prepared statement with a SELECT query that filters based on the start date.
     * Creates Appointment objects from the result set and adds them to an ObservableList.
     * Catches and prints any SQL exceptions.
     *
     * @return The ObservableList of appointments for the current month.
     */
    public static ObservableList<Appointment> getMonthlyAppointment(){
        ObservableList<Appointment> monthAppointments = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM appointments INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID WHERE Start >=? AND Start <=?";
            PreparedStatement statement = JDBC.connection.prepareStatement(query);
            statement.setTimestamp(1, Timestamp.valueOf(now));
            statement.setTimestamp(2, Timestamp.valueOf(monthLater));

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int appointmentId = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
                int customerId = resultSet.getInt("Customer_ID");
                int userId = resultSet.getInt("User_ID");
                int contactId = resultSet.getInt("Contact_ID");
                Appointment byMonth = new Appointment(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
                monthAppointments.add(byMonth);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monthAppointments;
    }

    /**
     * Fetches appointments specific to a user based on their ID.
     * Uses a prepared statement with a SELECT query that filters by user ID.
     * Creates Appointment objects from the result set and adds them to an ObservableList.
     * Catches and prints any SQL exceptions.
     *
     * @param userID user's ID
     * @return The ObservableList of appointments for the specified user.
     */
    public static ObservableList<Appointment> getUserAppointment(int userID){
        ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM appointments WHERE User_ID = ' " + userID + " ' ";
            PreparedStatement statement = JDBC.connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int appointmentId = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
                int customerId = resultSet.getInt("customer_ID");
                int userId = resultSet.getInt("User_ID");
                int contactId = resultSet.getInt("Contact_ID");
                Appointment results = new Appointment(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
                userAppointments.add(results);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userAppointments;
    }

    /**
     *  Retrieves appointments for a specific customer based on their ID.
     *
     * @param customerId customer's ID
     * @return customerAppointment
     */
    public static ObservableList<Appointment> getAppointments (int customerId){
        ObservableList<Appointment> customerAppointment = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM appointments WHERE Customer_ID =? ";
            PreparedStatement statement = JDBC.connection.prepareStatement(query);
            statement.setInt(1, customerId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int appointmentId = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
                customerId = resultSet.getInt("customer_ID");
                int userId = resultSet.getInt("User_ID");
                int contactId = resultSet.getInt("Contact_ID");
                Appointment results = new Appointment(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
                customerAppointment.add(results);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerAppointment;
    }

    /**
     * Similar to getUserAppointment, but retrieves appointments associated with a specific contact based on their ID.
     * Fetches appointments specific to a contact based on their contact ID.
     * Uses a prepared statement with a SELECT query that filters by contact ID.
     * Creates Appointment objects from the result set and adds them to an ObservableList.
     * Catches and prints any SQL exceptions.
     *
     * @param contactID contact's id
     * @return The ObservableList of appointments for the specified contact
     */
    public static ObservableList<Appointment> getContactAppointment( int contactID) {
        ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM appointments WHERE Contact_ID = ' " + contactID + " ' ";
            PreparedStatement statement = JDBC.connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int appointmentId = resultSet.getInt("Appointment_ID");
                String title = resultSet.getString("Title");
                String description = resultSet.getString("Description");
                String location = resultSet.getString("Location");
                String type = resultSet.getString("Type");
                LocalDateTime start = resultSet.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = resultSet.getTimestamp("End").toLocalDateTime();
                int customerId = resultSet.getInt("customer_ID");
                int userId = resultSet.getInt("User_ID");
                int contactId = resultSet.getInt("Contact_ID");
                Appointment results = new Appointment(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
                contactAppointments.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactAppointments;
    }

    /**
     * Counts the number of appointments for each type
     *
     * @return The results as a list of Appointment objects with "type" and "count" fields.
     */
    public static ObservableList<Appointment> getAppointmentType(){
        ObservableList<Appointment> appointmentListType = FXCollections.observableArrayList();
        try {
            String query = "SELECT Type, Count(*) AS NUM FROM appointments GROUP BY Type";
            PreparedStatement statement = JDBC.connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String type = resultSet.getString("Type");
                int typeTotal = resultSet.getInt("NUM");
                Appointment results = new Appointment(type, typeTotal);
                appointmentListType.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentListType;
    }

    /**
     * Counts the number of appointments for each month
     *
     * @return The results as a list of Appointment objects with "month" and "count" fields.
     */
    public static ObservableList<Appointment> getAppointmentTypeMonth(){
        ObservableList<Appointment> appointmentTypeMonthTotal = FXCollections.observableArrayList();
        try {
            String query = "SELECT DISTINCT(MONTHNAME(Start)) AS Month, Count(*) AS NUM FROM appointments GROUP BY Month";
            PreparedStatement statement = JDBC.connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String type = resultSet.getString("Month");
                int typeTotal = resultSet.getInt("NUM");
                Appointment results = new Appointment(type, typeTotal);
                appointmentTypeMonthTotal.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentTypeMonthTotal;
    }
}
