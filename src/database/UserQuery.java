package database;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 *
 * @author Daniel Akoko
 */
public class UserQuery {

    /**
     * Retrieves all users from the "Users" table.
     * Uses a prepared statement with a simple SELECT query to fetch data.
     * Creates User objects based on the retrieved user ID and name.
     * Catches and prints any SQL exceptions.
     *
     * @return ObservableList of User objects containing all users.
     */
    public static ObservableList<User> getUserList() {
        ObservableList<User> userList = FXCollections.observableArrayList();
        try {
            // Selecting all users from the database
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement("SELECT * FROM Users ");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                // Extracting user details from the result set
                int userId = resultSet.getInt("User_ID");
                String username = resultSet.getString("User_Name");

                // Creating User object and adding it to the list
                User user = new User(userId, username);
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    /**
     * Attempts to validate a user login by checking username and password.
     * Uses a prepared statement with a SELECT query that filters by username and password.
     * Checks if any result is found in the result set.
     * Catches and prints any SQL exceptions.
     *
     * @param username The username to validate.
     * @param password The password to validate.
     * @return True if the username and password combination match, false otherwise.
     */
    public static boolean validateUser(String username, String password) {
         try {
             // Selecting user based on username and password
            String query = "SELECT * FROM Users WHERE User_Name = ? AND Password = ?";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
             preparedStatement.setString(1, username);
             preparedStatement.setString(2, password);
             ResultSet resultSet = preparedStatement.executeQuery();

             // Checking if a matching user is found
             if(resultSet.next()) {
                 return true;
             }
        } catch (SQLException e) {
             e.printStackTrace();
         }
         return false;
    }

    /**
     * Checks if a provided username already exists in the "Users" table.
     * Uses a prepared statement with a SELECT query that filters by username (case-sensitive).
     * Checks if any result is found in the result set.
     * Catches and prints any SQL exceptions.
     *
     * @param username The username to check.
     * @return True if the username already exists, false otherwise.
     */
    public static boolean validUsername(String username) {
        try(PreparedStatement preparedStatement = JDBC.connection.prepareStatement("SELECT * FROM Users WHERE BINARY User_Name = ?")) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Checking if a matching username is found
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks if a provided password exists in the "Users" table.
     * Uses a prepared statement with a SELECT query that filters by password (case-sensitive).
     * Checks if any result is found in the result set.
     * Catches and prints any SQL exceptions.
     *
     * @param password The password to check.
     * @return True if the password already exists, false otherwise.
     */
    public static boolean validPassword(String password) {
        try(PreparedStatement preparedStatement = JDBC.connection.prepareStatement("SELECT * FROM Users WHERE BINARY Password = ?")) {
            preparedStatement.setString(1, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Checking if a matching password is found
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves the user ID associated with a specific username.
     * Uses a prepared statement with a SELECT query that filters by username.
     * Expects only one result and retrieves the user ID from the result set.
     * Catches and throws any SQL exceptions.
     *
     * @param username The username for which to retrieve the ID.
     * @return The user ID associated with the provided username as an integer.
     * @throws SQLException If a SQL exception occurs during the database interaction.
     */
    public static int getUserId(String username) throws SQLException {
        int userId = 0;
        // Selecting user ID based on username
        PreparedStatement preparedStatement = JDBC.connection.prepareStatement("SELECT User_ID, User_Name FROM Users WHERE User_Name = '" + username + "'");
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            // Extracting user ID from the result set
            userId = resultSet.getInt("User_ID");
            username = resultSet.getString("User_Name");
            }
        return userId;
    }

    /**
     * Retrieves user information based on their ID.
     * Uses a prepared statement with a SELECT query that filters by user ID.
     * Creates a User object based on the retrieved user ID and name.
     * Catches and throws any SQL exceptions.
     *
     * @param userId The ID of the user to retrieve.
     * @return User object if found, or throws a RuntimeException if not.
     */
    public static User returnUserId(int userId) {
        try {
            // Selecting user based on user ID
            String query = "SELECT User_ID, User_Name FROM users WHERE User_ID = ?";
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();

            resultSet.next();
            // Extracting user details from the result set
            int user_ID = resultSet.getInt("User_ID");
            String username = resultSet.getString("User_Name");

            // Creating and returning User object
            return new User(user_ID, username);
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}
