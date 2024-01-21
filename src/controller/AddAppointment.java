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
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static helper.Util.*;

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


    public void onSave(ActionEvent actionEvent) throws IOException, SQLException {
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
        LocalDateTime start_date_time = LocalDateTime.of(start_date.getYear(), start_date.getMonth(), start_date.getDayOfMonth(), start_time.getHour(), start_time.getMinute());
        LocalDateTime end_date_time = LocalDateTime.of(end_date.getYear(), end_date.getMonth(), end_date.getDayOfMonth(), end_time.getHour(), end_time.getMinute());

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
            } else if(!validateOverlapping(customer_Id, start_date_time, end_date_time)){
                AppointmentQuery.addAppointment(Title, Description, Location, Type, start_date_time, end_date_time, customer_Id, user_Id, Contact);

                Parent parent = FXMLLoader.load(getClass().getResource("../view/AppointmentScreen.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
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
        appointmentID.setId(appointmentID.getId());
        ObservableList<Contact> contacts = ContactQuery.getAllContacts();
        ObservableList<User> users = UserQuery.getUserList();
        ObservableList<Customer> customers = CustomerQuery.getCustomerList();
        ObservableList<String> populateTime = FXCollections.observableArrayList();
        ObservableList<String> populateDate = FXCollections.observableArrayList();

        contact.setItems(contacts);
        contact.getSelectionModel().selectFirst();
        userBox.setItems(users);
        userBox.getSelectionModel().selectFirst();
        customerBox.setItems(customers);
        customerBox.getSelectionModel().selectFirst();
        startDate.setValue(LocalDate.now());
        endDate.setValue(LocalDate.now());
        startTime.setItems(initializeBusinessHours(ZoneId.systemDefault(), ZoneId.of("America/New_York"), LocalTime.of(8, 0), 14));
        startTime.getSelectionModel().selectFirst();
        endTime.setItems(initializeBusinessHours(ZoneId.systemDefault(), ZoneId.of("America/New_York"), LocalTime.of(9, 0), 13));
        endTime.getSelectionModel().selectFirst();
        //validationFunction(appointmentDateTime,  existingAppointments, newAppointmentStart, newAppointmentEnd);


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

