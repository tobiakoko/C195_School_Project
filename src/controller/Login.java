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
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static database.UserQuery.*;
import static helper.Util.confirmAlert;
import static helper.Util.errorAlert;

/**
 * Controller class for handling login-related actions.
 * The Login class manages the user interface and logic for the login screen.
 * It allows users to enter their username and password,
 * validates the provided credentials, and handles successful and unsuccessful login attempts.
 * It also displays login attempts and their results to a log file.
 *
 * @author Daniel Akoko
 */
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

    /**
     * Handles the action when "Cancel" button is clicked.
     * Closes the login window.
     *
     * @param actionEvent The event triggering the action.
     */
    public void cancelButtonAction(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the action when "Login" button is clicked.
     * Performs login checks and processes:
     *     Validates username and password fields for emptiness and validity.
     *     Logs attempts and indicates success or otherwise.
     *     Displays error messages for invalid or empty fields.
     *     Navigates to the main screen on successful login and checks for upcoming appointments.
     *     Shows alerts for successful login with upcoming appointments or no upcoming appointments.
     *
     * @param actionEvent The event triggering the action.
     * @throws Exception If there is an exception.
     */
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

                // Load the main screen upon successful login
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
                    confirmAlert(null, rb.getString("NoUpcomingAppointments"));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the controller.
     * Determines the user's location (zone ID) and displays it on the login form.
     * Sets UI labels and buttons based on the current language using a resource bundle.
     *
     * @param url            The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
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


    /**
     * Displays an alert with information about an upcoming appointment.
     *
     * @param appointment The appointment to display in the alert.
     */
    private void showAppointmentAlert(Appointment appointment) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(null);
        alert.setContentText(rb.getString("Appointment") + " " + appointment.getAppointmentId() + " " + rb.getString("beginsat") + " " +  appointment.getStart());
        alert.showAndWait();
    }

    /**
     * Records a login attempt.
     * Creates a log entry for the login attempt with username, timestamp, and success status.
     * Appends the log entry to a dedicated file.
     *
     * <b>Lambda Expression 2</b>
     * @param username The username used for the login attempt.
     * @param timestamp The timestamp of the login attempt.
     * @param success   Indicates whether the login attempt was successful.
     */
    public void loginAttempt(String username, LocalDateTime timestamp, boolean success) {
        //Lambda Expression
        Consumer<String> writeLogEntry = entry -> {
            try {
                Files.write(Path.of(LOGIN_ACTIVITY_FILE), entry.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        String logEntry = String.format("Username: %s, Timestamp: %s, Success: %s%n", username, timestamp, success);
        writeLogEntry.accept(logEntry);
    }

    /*


     */
}
