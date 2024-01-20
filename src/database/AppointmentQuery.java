package database;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import model.Appointment;

import java.sql.*;
import java.time.LocalDateTime;

public class AppointmentQuery {

    static ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    static LocalDateTime now = LocalDateTime.now();
    static LocalDateTime weekLater = now.plusWeeks(1);
    static LocalDateTime monthLater = now.plusMonths(1);

    public static ObservableList<Appointment> getAppointmentList() {
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM appointments JOIN contacts ON appointments.Contact_ID = contacts.CONTACT_ID ORDER BY appointments.Appointment_ID";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                int contactId = rs.getInt("Contact_ID");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");

                Appointment c = new Appointment(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
                appointmentList.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentList;
    }

    public static void modifyAppointment(int appointmentId, String title, String description, String location, String type,
                                         LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId) {
        try {
            String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_Id = ?, User_ID = ?, Contact_ID  = ? WHERE Appointment_ID = ?";
            PreparedStatement modifyAppointment = JDBC.connection.prepareStatement(sql);

            modifyAppointment.setString(1, title);
            modifyAppointment.setString(2, description);
            modifyAppointment.setString(3, location);
            modifyAppointment.setString(4, type);
            modifyAppointment.setTimestamp(5, Timestamp.valueOf(start));
            modifyAppointment.setTimestamp(6, Timestamp.valueOf(end));
            modifyAppointment.setInt(7, customerId);
            modifyAppointment.setInt(8, userId);
            modifyAppointment.setInt(9, contactId);
            modifyAppointment.setInt(10, appointmentId);
            modifyAppointment.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addAppointment(String title, String description, String location, String type,
                                      LocalDateTime start, LocalDateTime end, int customerId, int userId, int contactId){
        try {
            String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, USER_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);

            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, Timestamp.valueOf(start));
            ps.setTimestamp(6, Timestamp.valueOf(end));
            ps.setInt(7, customerId);
            ps.setInt(8,userId);
            ps.setInt(9, contactId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Appointment> getApptByWeek(){
        appointments = getAppointmentList();
        try {
            String query = "SELECT * FROM appointments INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID WHERE Start >=? AND Start <= ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(query);
            ps.setTimestamp(1, Timestamp.valueOf(now));
            ps.setTimestamp(2, Timestamp.valueOf(weekLater));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointment byWeek = new Appointment(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
                appointments.add(byWeek);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public static void deleteAppointment(int appointmentId){
        try {
            String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
            preparedStatement.setInt(1, appointmentId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Appointment> getMonthlyAppointment(){
        appointments = getAppointmentList();
        try {
            String sql = "SELECT * FROM appointments INNER JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID WHERE WHERE Start >=? AND Start <=?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setTimestamp(1, Timestamp.valueOf(now));
            ps.setTimestamp(2, Timestamp.valueOf(monthLater));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointment byMonth = new Appointment(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
                appointments.add(byMonth);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public static ObservableList<Appointment> getUserAppointment(int userID){
        ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM appointments WHERE User_ID = ' " + userID + " ' ";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointment results = new Appointment(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
                userAppointments.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userAppointments;
    }

    public static ObservableList<Appointment> getContactAppointment( int contactID) {
        ObservableList<Appointment> contactAppointments = FXCollections.observableArrayList();
        try {
            String sql = "SELECT * FROM appointments WHERE User_ID = ' " + contactID + " ' ";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointment results = new Appointment(appointmentId, title, description, location, type, start, end, customerId, userId, contactId);
                contactAppointments.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactAppointments;
    }

    public static ObservableList<Appointment> AppointmentType(){
        ObservableList<Appointment> appointmentListType = FXCollections.observableArrayList();
        try {
            String query = "SELECT Type, Count(*) AS NUM FROM appointments GROUP BY Type";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String type = rs.getString("Type");
                int typeTotal = rs.getInt("NUM");
                Appointment results = new Appointment(type, typeTotal);
                appointmentListType.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentListType;
    }

    public static ObservableList<Appointment> getAppointmentTypeMonth(){
        ObservableList<Appointment> appointmentTypeMonthTotal = FXCollections.observableArrayList();
        try {
            String sql = "SELECT DISTINCT(MONTHNAME(Start)) AS Month, Count(*) AS NUM FROM appointments GROUP BY Month";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String type = rs.getString("Month");
                int typeTotal = rs.getInt("NUM");
                Appointment results = new Appointment(type, typeTotal);
                appointmentTypeMonthTotal.add(results);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentTypeMonthTotal;
    }
}
