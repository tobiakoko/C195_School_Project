package controller;

import database.AppointmentQuery;
import database.UserQuery;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class Login implements Initializable {
    @FXML private TextField usernameTextField;
    @FXML private Button cancelButton;
    @FXML private Button loginButton;
    @FXML private Label zoneId;
    @FXML private PasswordField passwordTextField;

    public void cancelButtonAction(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void loginButtonAction(ActionEvent actionEvent) throws SQLException, IOException, Exception {
        try {
            //definition for +/- 15 minute appointment check
            ObservableList<Appointment> getAllAppointments = AppointmentQuery.getAppointmentList();
            LocalDateTime currentTimeMinus15Min = LocalDateTime.now().minusMinutes(15);
            LocalDateTime currentTimePlus15Min = LocalDateTime.now().plusMinutes(15);
            LocalDateTime startTime;
            int getAppointmentID = 0;
            LocalDateTime displayTime = null;
            boolean appointmentWithin15Min = false;

            ResourceBundle rb = ResourceBundle.getBundle("language/login", Locale.getDefault());

            String usernameInput = usernameTextField.getText();
            String passwordInput = passwordTextField.getText();
            int userId = UserQuery.userValidation(usernameInput, passwordInput);

            FileWriter fileWriter = new FileWriter("login_activity.txt", true);
            PrintWriter outputFile = new PrintWriter(fileWriter);

            if(userId > 0) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/MainScreen.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) loginButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

                outputFile.print("user: " + usernameInput + "successfully logged in at: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");

                for(Appointment appointment: getAllAppointments) {
                    startTime = appointment.getStart();
                    if((startTime.isAfter(currentTimeMinus15Min) || startTime.isEqual(currentTimeMinus15Min)) && (startTime.isBefore(currentTimePlus15Min) || (startTime.isEqual(currentTimePlus15Min)))){
                        getAppointmentID = appointment.getAppointmentId();
                        displayTime = startTime;
                        appointmentWithin15Min = true;
                    }
                }

                if (appointmentWithin15Min) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Appointment within 15 minutes: " + getAppointmentID + " and appointment start time of: " + displayTime);
                    Optional<ButtonType> confirmation = alert.showAndWait();
                    System.out.println("There is an appointment within 15 minutes");
                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "No upcoming appointments");
                    Optional<ButtonType> confirmation = alert.showAndWait();
                    System.out.println("No upcoming appointments");
                }

            } else if (userId < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("Error"));
                alert.setContentText(rb.getString("Incorrect"));

                outputFile.print("user: " + usernameInput + "failed login attempt at: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Locale locale = Locale.getDefault();
            Locale.setDefault(locale);

            ZoneId zone = ZoneId.systemDefault();

            zoneId.setText(String.valueOf(zone));

            resourceBundle = ResourceBundle.getBundle("language/login", Locale.getDefault());
            usernameTextField.setText(resourceBundle.getString("username"));
            passwordTextField.setText(resourceBundle.getString("password"));
            loginButton.setText(resourceBundle.getString("Login"));
            cancelButton.setText(resourceBundle.getString("Exit"));

        } catch(MissingResourceException e) {
            System.out.println("Resource file missing: " + e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
