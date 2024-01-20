package controller;

import database.AppointmentQuery;
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

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static database.UserQuery.*;
import static helper.Util.errorAlert;

public class Login implements Initializable {
    @FXML private TextField usernameTextField;
    @FXML private Button cancelButton;
    @FXML private Button loginButton;
    @FXML private Label zoneId;
    @FXML private PasswordField passwordTextField;
    private static final String LOGIN_ACTIVITY_FILE = "login_activity.txt";

    //Language bundle to automatically translate error control message into English or French based on the user's computer language setting
    ResourceBundle rb = ResourceBundle.getBundle("language/lang", Locale.getDefault());
    LocalDateTime now = LocalDateTime.now();

    public void cancelButtonAction(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void loginButtonAction(ActionEvent actionEvent) throws Exception {
        try {
            //accepts username and password
            String usernameInput = usernameTextField.getText();
            String passwordInput = passwordTextField.getText();

            //Username and Password Validation Check
            if (usernameInput.isEmpty() || usernameInput.isBlank()) {
                errorAlert(rb.getString("UsernameFieldBlank"), rb.getString("BlankusernamefieldPleasetryagain"));
            } else if (!validUsername(usernameInput) && !validPassword(passwordInput)) {
                errorAlert(rb.getString("InvalidUsernameorPassword"), rb.getString("InvalidusernameorpasswordentryPleasetryagain"));
                loginAttempt(usernameInput, now, false);
            } else if (passwordInput.isEmpty() || passwordInput.isBlank()) {
                errorAlert(rb.getString("InvalidUsernameorPassword"), rb.getString("BlankpasswordfieldPleasetryagain"));
            } else if (!validUsername(usernameInput)) {
                errorAlert(rb.getString("InvalidUsername"), rb.getString("InvalidusernameentryPleasetryagain"));
                loginAttempt(usernameInput, now, false);
            } else if (!validPassword(passwordInput)) {
                errorAlert(rb.getString("InvalidPassword"), rb.getString("InvalidpasswordentryPleasetryagain"));
                loginAttempt(usernameInput, now, false);
            } else if (validateUser(usernameInput, passwordInput)) {
                int userID = getUserId(usernameInput);
                ObservableList<Appointment> appointments = AppointmentQuery.getUserAppointment(userID);

                Parent parent = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

                loginAttempt(usernameInput, now, true);

                //Check for appointments upon successful login
                boolean userValid = false;
                for (Appointment appointment : appointments) {

                    LocalDateTime startTime = appointment.getStart();
                    LocalDateTime upcomingAppt = now.plusMinutes(15);

                    if((startTime.isAfter(now) || startTime.isEqual(upcomingAppt)) &&
                        startTime.isBefore(upcomingAppt) || startTime.isEqual(now)){
                        showAppointmentAlert(appointment);
                        userValid = true;
                    }
                }
                if (!userValid) {
                    showNoAppointmentAlert();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            //Determine the user's location(i.e Zone ID) and display it in a label on the log-in form
            Locale locale = Locale.getDefault();
            Locale.setDefault(locale);

            ZoneId zone = ZoneId.systemDefault();
            zoneId.setText(String.valueOf(zone));
            usernameTextField.setText(rb.getString("Username"));
            passwordTextField.setText(rb.getString("Password"));
            loginButton.setText(rb.getString("Login"));
            cancelButton.setText(rb.getString("Cancel"));
    }


    private void showAppointmentAlert(Appointment appointment) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(null);
        alert.setContentText(rb.getString("Appointment") + " " + appointment.getAppointmentId() + " " + rb.getString("beginsat") + " " +  appointment.getStart());
        alert.showAndWait();
    }

    private void showNoAppointmentAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(null);
        alert.setContentText(rb.getString("NoUpcomingAppointments"));
        alert.showAndWait();
    }

    public void loginAttempt(String username, LocalDateTime timestamp, boolean success) {
        String logEntry;
        if(success) {
            logEntry = String.format("Username: %s, Timestamp: %s, Success: %s%n", username, timestamp, true);
        }
        else {
            logEntry = String.format("Username: %s, Timestamp: %s, Success: %s%n", username, timestamp, false);
        }

        try {
            Files.write(Path.of(LOGIN_ACTIVITY_FILE), logEntry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
