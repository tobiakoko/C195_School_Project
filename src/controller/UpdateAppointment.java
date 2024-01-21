package controller;

import database.*;
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
import model.*;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static helper.Util.*;

public class UpdateAppointment implements Initializable {

    @FXML private TextField appointmentId;
    @FXML private ComboBox<Customer> customerId;
    @FXML private ComboBox<User> userId;
    @FXML private TextField Title;
    @FXML private TextField Description;
    @FXML private TextField Location;
    @FXML private TextField Type;
    @FXML private ComboBox<Contact> Contact;
    @FXML private ComboBox<LocalTime> startTime;
    @FXML private ComboBox<LocalTime> endTime;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;

   model.Appointment selectedAppointment = null;
    private static final LocalTime BUSINESS_START_TIME = LocalTime.of(8, 0);
    private static final LocalTime BUSINESS_END_TIME = LocalTime.of(22, 0);



    public void onSave(ActionEvent actionEvent) throws IOException {
        int appointment_Id = Integer.parseInt(appointmentId.getText());
        String title = Title.getText();
        String description = Description.getText();
        String type = Type.getText();
        String location = Location.getText();
        int contactID = Contact.getSelectionModel().getSelectedItem().getContactId();
        int customerID = customerId.getSelectionModel().getSelectedItem().getCustomerId();
        int userID = userId.getSelectionModel().getSelectedItem().getUserId();
        LocalDate start_date = startDate.getValue();
        LocalTime start_time = startTime.getSelectionModel().getSelectedItem();
        LocalDate end_date = endDate.getValue();
        LocalTime end_time = endTime.getSelectionModel().getSelectedItem();
        LocalDateTime start_date_time = LocalDateTime.of(start_date.getYear(), start_date.getMonth(), start_date.getDayOfMonth(), start_time.getHour(), start_time.getMinute());
        LocalDateTime end_date_time = LocalDateTime.of(end_date.getYear(), end_date.getMonth(), end_date.getDayOfMonth(), end_time.getHour(), end_time.getMinute());

        if(title.isBlank() || title.isEmpty()) {
            errorAlert("BLANK TITLE", "Title field is blank. Input title");
        } else if(description.isBlank() || description.isEmpty()) {
            errorAlert("BLANK DESCRIPTION", "Description field is blank. Input description");
        } else if(type.isBlank() || type.isEmpty()) {
            errorAlert("BLANK TYPE", "Type field is blank. Input type");
        } else if (location.isBlank() || location.isEmpty()) {
            errorAlert("BLANK LOCATION", "The location field is blank. Input location");
        } else {
            if(start_date == null) {
                errorAlert("Please select a valid start date", "The start date field is blank. Please choose a date");

            }else if(start_time == null) {
                errorAlert("Please select a valid start time", "The start time field is blank. Please choose a start time");
                return;
            }else if(end_date == null) {
                errorAlert("Please select a valid end date", "The end date field is blank. Please choose a date");
                return;
            }else if(end_time == null) {
                errorAlert("Please select a valid end time", "The end time field is blank. Please choose a time");
                return;
            } else {
                //Appointment Time OverLap and Business hours validation needed here
                if(!validateBusinessHours(start_time, end_time)){
                    errorAlert("Out of Bounds Error", "Appointments must be scheduled between 8:00 a.m. and 10:00 p.m. ET, including weekends.");
                } else if(!validatingOverlap(customerID, start_date_time, end_date_time)){
                    AppointmentQuery.modifyAppointment(appointment_Id, title, description, location, type, start_date_time, end_date_time, customerID, userID, contactID);

                    Parent parent = FXMLLoader.load(getClass().getResource("../view/AppointmentScreen.fxml"));
                    Scene scene = new Scene(parent);
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                }
            }
        }
    }

    public void onCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm cancellation");
        alert.setContentText("Are you sure you want to leave this page? Changes will not be saved");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.YES) {
            Parent parent = FXMLLoader.load(getClass().getResource("../view/AppointmentScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Contact> contacts = ContactQuery.getAllContacts();
        Contact.setItems(contacts);


        ObservableList<Customer> customers = CustomerQuery.getCustomerList();
        customerId.setItems(customers);

        ObservableList<User> users = UserQuery.getUserList();
        userId.setItems(users);

    }

    public void modifyAppointment(Appointment appointment) throws SQLException {
        appointmentId.setText(String.valueOf(appointment.getAppointmentId()));
        Title.setText(appointment.getTitle());
        Description.setText(appointment.getDescription());
        Location.setText(appointment.getLocation());
        Type.setText(appointment.getType());
        startDate.setValue(appointment.getStart().toLocalDate());
        startTime.setItems(initializeBusinessHours(ZoneId.systemDefault(), ZoneId.of("America/New_York"), LocalTime.of(8, 0), 14));
        startTime.setValue(appointment.getStart().toLocalTime());
        endDate.setValue(appointment.getEnd().toLocalDate());
        endTime.setItems(initializeBusinessHours(ZoneId.systemDefault(), ZoneId.of("America/New_York"), LocalTime.of(9, 0), 13));
        endTime.setValue(appointment.getEnd().toLocalTime());

        Contact contact = ContactQuery.returnContactList(appointment.getContact());
        Contact.setValue(contact);

        Customer customer = CustomerQuery.returnCustomerList(appointment.getCustomerId());
        customerId.setValue(customer);

        User user = UserQuery.returnUserId(appointment.getUserId());
        userId.setValue(user);
    }

    private static  ObservableList<LocalTime> initializeBusinessHours(ZoneId systemZoneId, ZoneId businessZoneId, LocalTime startHour, int workingHours) {
        ZonedDateTime businessStartTime = ZonedDateTime.of(LocalDate.now(), startHour, businessZoneId);
        ZonedDateTime localStartTime = businessStartTime.withZoneSameInstant(systemZoneId);
        int localStartingHour = localStartTime.getHour();

        return IntStream.range(localStartingHour, localStartingHour + workingHours).filter(hour -> hour >= 8 && hour <= 22).mapToObj(i -> LocalTime.of(i / 2, (i % 2) * 30))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    private static boolean validateBusinessHours(LocalTime startTime, LocalTime endTime) {
        // Validate that an appointment is scheduled within business hours
        if (startTime.isBefore(BUSINESS_START_TIME) || endTime.isAfter(BUSINESS_END_TIME)) {
            return true;
        } else {
            return false;
        }
    }
}
