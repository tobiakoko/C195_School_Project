package database;

import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserQuery {

    public static ObservableList<User> getUserList() {
        ObservableList<User> userList = FXCollections.observableArrayList();
        try {
            PreparedStatement ps = JDBC.connection.prepareStatement("SELECT * FROM Users ");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");

                User u = new User(userId, userName);
                userList.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public static boolean userLogin(String User_Name, String Password) {
        try(PreparedStatement ps = JDBC.connection.prepareStatement("SELECT * FROM Users WHERE User_Name = ? AND Password = ?")) {
            ps.setString(1, User_Name);
            ps.setString(2, Password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean usernameValidation(String User_Name) {
        try(PreparedStatement ps = JDBC.connection.prepareStatement("SELECT * FROM Users WHERE BINARY User_Name = ?")) {
            ps.setString(1, User_Name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean passwordValidation(String Password) {
        try(PreparedStatement ps = JDBC.connection.prepareStatement("SELECT * FROM Users WHERE BINARY Password = ?")) {
            ps.setString(1, Password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getUserId(String userName) throws SQLException {
        int userId = 0;
        PreparedStatement ps = JDBC.connection.prepareStatement("SELECT User_ID, User_Name FROM Users WHERE User_Name = '" + userName + "'");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            userId = rs.getInt("User_ID");
            userName = rs.getString("User_Name");
            }
        return userId;
    }

    public static User returnUserId(int userId) {
        try {
            String sql = "SELECT User_ID, User_Name FROM users WHERE User_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.execute();

            ResultSet rs = ps.getResultSet();

            rs.next();
            int user_ID = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            User u = new User(user_ID, userName);
            return u;
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}
