package controller;

import database.AppointmentQuery;
import database.ContactQuery;
import database.CustomerQuery;
import database.UserQuery;
import javafx.collections.FXCollections;
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
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static helper.Util.errorAlert;
import static helper.Util.validateOverlapping;

/**
 * This class manages the user interface and logic for adding a new appointment.
 * It populates drop-down menus with contacts, users, customers, and available times within business hours.
 * It validates user input and adds appointments to the database upon confirmation.
 *
 * @author Daniel Akoko
 */
public class AddAppointment implements Initializable {

    @FXML private ComboBox<Customer> customerBox;
    @FXML private ComboBox<User> userBox;
    @FXML private TextField appointmentID;
    @FXML private TextField title;
    @FXML private TextField description;
    @FXML private TextField location;
    @FXML private ComboBox<Contact> contact;
    @FXML private TextField type;
    @FXML private ComboBox<LocalTime> startTime;
    @FXML private ComboBox<LocalTime> endTime;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;

    private static final LocalTime BUSINESS_START_TIME = LocalTime.of(8, 0);
    private static final LocalTime BUSINESS_END_TIME = LocalTime.of(22, 0);

    /**
     * Handles the save action for adding an appointment.
     * Attempts to save a new appointment based on user input.
     * Retrieves user-selected values for title, description, location, type, contact, customer, user, start date/time, and end date/time.
     * Validates if start and end dates and times are present and within business hours.
     * Checks for overlap with existing appointments for the customer.
     * If valid, adds the appointment to the database using AppointmentQuery.addAppointment.
     * Navigates to the AppointmentScreen if successful.
     * Displays error messages for missing required fields or invalid times/overlaps.
     *
     * @param actionEvent The ActionEvent associated with the event.
     * @throws IOException If an I/O error occurs.
     * @throws SQLException If a SQL exception occurs.
     */
    public void onSave(ActionEvent actionEvent) throws IOException, SQLException {
        // Extracting input data from the form
        String Title = title.getText();
        String Description = description.getText();
        String Type = type.getText();
        String Location = location.getText();
        int Contact = contact.getSelectionModel().getSelectedItem().getContactId();
        int customer_Id = customerBox.getSelectionModel().getSelectedItem().getCustomerId();
        int user_Id = userBox.getSelectionModel().getSelectedItem().getUserId();
        LocalDate start_date = startDate.getValue();
        LocalTime start_time = startTime.getSelectionModel().getSelectedItem();
        LocalDate end_date = endDate.getValue();
        LocalTime end_time = endTime.getSelectionModel().getSelectedItem();

        // Validating input fields
        if(start_date == null || start_time == null || end_date == null || end_time == null) {
            errorAlert("Missing Fields", "Please fill in all date and time fields.");

        }
        LocalDateTime start_date_time = LocalDateTime.of(start_date.getYear(), start_date.getMonth(), start_date.getDayOfMonth(), start_time.getHour(), start_time.getMinute());
        LocalDateTime end_date_time = LocalDateTime.of(end_date.getYear(), end_date.getMonth(), end_date.getDayOfMonth(), end_time.getHour(), end_time.getMinute());

        //Appointment Time OverLap and Business hours validation needed here
        boolean isValid = validateBusinessHours.test(start_time, end_time);
        if(!isValid){
            errorAlert("Out of Bounds Error", "Appointments must be scheduled between 8:00 a.m. and 10:00 p.m. ET, including weekends.");
        } else if(!validateOverlapping(customer_Id, start_date_time, end_date_time)){
            // Adding the appointment to the database
            AppointmentQuery.addAppointment(Title, Description, Location, Type, start_date_time, end_date_time, customer_Id, user_Id, Contact);

            // Redirecting to the main appointment screen
            Parent parent = FXMLLoader.load(getClass().getResource("../view/AppointmentScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Handles the cancellation action for adding an appointment.
     * Confirms with the user if they want to cancel adding an appointment and return to the AppointmentScreen.
     *
     * @param actionEvent The event triggering the cancellation action.
     * @throws IOException If an I/O error occurs.
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        // Displaying confirmation dialog for cancellation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm cancellation");
        alert.setContentText("Are you sure you want to leave this page? Changes will not be saved");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.YES) {
            // Redirecting to the main appointment screen
            Parent parent = FXMLLoader.load(getClass().getResource("../view/AppointmentScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Initializes the controller.
     * Sets the ID for the appointmentID field (for potential customization).
     * Populates drop-down menus with lists of contacts, users, and customers obtained from respective queries.
     * Sets default values for dates and times.
     * Creates ObservableLists for time and date options based on business hours (8:00 AM - 10:00 PM ET).
     * Selects the first item in each ComboBox by default.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentID.setId(appointmentID.getId());
        ObservableList<model.Contact> contacts = ContactQuery.getAllContacts();
        ObservableList<User> users = UserQuery.getUserList();
        ObservableList<Customer> customers = CustomerQuery.getCustomerList();
        /*
        ObservableList<String> populateTime = FXCollections.observableArrayList();
        ObservableList<String> populateDate = FXCollections.observableArrayList();
                // Initialize start and end time combo boxes
        startTime.setItems(initializeBusinessHours(ZoneId.systemDefault(), ZoneId.of("America/New_York"), BUSINESS_START_TIME, BUSINESS_END_TIME));
        endTime.setItems(initializeBusinessHours(ZoneId.systemDefault(), ZoneId.of("America/New_York"), BUSINESS_START_TIME, BUSINESS_END_TIME));

        */

        // Populating dropdowns with data
        contact.setItems(contacts);
        contact.getSelectionModel().selectFirst();
        userBox.setItems(users);
        userBox.getSelectionModel().selectFirst();
        customerBox.setItems(customers);
        customerBox.getSelectionModel().selectFirst();
        startDate.setValue(LocalDate.now());
        endDate.setValue(LocalDate.now());
        startTime.setItems(initializeBusinessHours(ZoneId.systemDefault(), ZoneId.of("America/New_York"), BUSINESS_START_TIME, BUSINESS_END_TIME));
        startTime.getSelectionModel().selectFirst();
        endTime.setItems(initializeBusinessHours(ZoneId.systemDefault(), ZoneId.of("America/New_York"), BUSINESS_START_TIME, BUSINESS_END_TIME));
        endTime.getSelectionModel().selectFirst();
    }

    /**
     * Initializes business hours for a given time zone.
     *
     * @param systemZoneId   The system time zone.
     * @param businessZoneId The business time zone.
     * @param startHour      The starting hour of the business day.
     * @param endHour        The ending hour of the business day.
     * @return ObservableList of LocalTime representing business hours.
     */
    private static  ObservableList<LocalTime> initializeBusinessHours(ZoneId systemZoneId, ZoneId businessZoneId, LocalTime startHour, LocalTime endHour) {
        ZonedDateTime businessStartTime = ZonedDateTime.of(LocalDate.now(), startHour, businessZoneId);
        ZonedDateTime localStartTime = businessStartTime.withZoneSameInstant(systemZoneId);
        int localStartingHour = localStartTime.getHour();

        ZonedDateTime businessEndTime = ZonedDateTime.of(LocalDate.now(), endHour, businessZoneId);
        ZonedDateTime localEndTime = businessEndTime.withZoneSameInstant(systemZoneId);
        int localEndingHour = localEndTime.getHour();

        return IntStream.range(localStartingHour, localEndingHour)
                .filter(hour -> hour >= BUSINESS_START_TIME.getHour() && hour <= BUSINESS_END_TIME.getHour())
                .mapToObj(i -> LocalTime.of(i / 2, (i % 2) * 30))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    /**
     * Validates if the appointment is scheduled within business hours.
     *
     *<b>Lambda Expression 1</b>
     *
     */
    static BiPredicate<LocalTime, LocalTime> validateBusinessHours =
            (startTime, endTime) -> startTime.isBefore(BUSINESS_START_TIME) || endTime.isAfter(BUSINESS_END_TIME);

    /*
    private static boolean validateBusinessHours(LocalTime startTime, LocalTime endTime) {
    // Validate that an appointment is scheduled within business hours
        return startTime.isBefore(BUSINESS_START_TIME) || endTime.isAfter(BUSINESS_END_TIME);
    }

     */
}

