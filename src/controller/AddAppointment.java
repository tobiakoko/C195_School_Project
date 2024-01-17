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
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static helper.Util.errorAlert;
import static helper.Util.validationFunction;

public class AddAppointment implements Initializable {
    @FXML private Button save;
    @FXML private Button cancel;
    @FXML private TextField appointmentID;
    @FXML private TextField title;
    @FXML private TextField description;
    @FXML private TextField location;
    @FXML private ComboBox<Contact> contact;
    @FXML private TextField type;
    @FXML private ComboBox<Customer> customerComboBox;
    @FXML private ComboBox<User> userComboBox;
    @FXML private ComboBox<LocalTime> startTime;
    @FXML private ComboBox<LocalTime> endTime;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;

    public void onSave(ActionEvent actionEvent) throws IOException, SQLException {
        String apptTitle = title.getText();
        String apptDescription = description.getText();
        String apptType = type.getText();

        Contact apptContact = contact.getValue();
        if (apptContact == null) {
            errorAlert("Please select a valid contact", "Please select a valid contact");
            return;
        }
        int appointmentContact = apptContact.getContactId();

        LocalDate start_date = startDate.getValue();
        if(start_date == null) {
            errorAlert("Please select a valid start date", "The start date field is blank. Please choose a date");
            return;
        }

        LocalTime start_time = startTime.getValue();
        if(start_time == null) {
            errorAlert("Please select a valid start time", "The start time field is blank. Please choose a start time");
            return;
        }
        LocalDateTime start = LocalDateTime.of(startDate.getValue(), startTime.getValue());

        LocalDate end_date = endDate.getValue();
        if(end_date == null) {
            errorAlert("Please select a valid end date", "The end date field is blank. Please choose a date");

            return;
        }

        LocalTime end_time = endTime.getValue();
        if(end_time == null) {
            errorAlert("Please select a valid end time", "The end time field is blank. Please choose a time");

            return;
        }
        LocalDateTime end = LocalDateTime.of(endDate.getValue(), endTime.getValue());

    }

    public void onCancel(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/Appointments.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentID.setId(appointmentID.getId());

        ObservableList<Contact> contacts = ContactQuery.getAllContacts();
        ObservableList<User> users = UserQuery.getUserList();
        ObservableList<Customer> customers = CustomerQuery.getCustomerList();

        contact.setItems(contacts);
        contact.getSelectionModel().selectFirst();
        userComboBox.setItems(users);
        userComboBox.getSelectionModel().selectFirst();
        customerComboBox.setItems(customers);
        customerComboBox.getSelectionModel().selectFirst();
        startDate.setValue(LocalDate.now());
        endDate.setValue(LocalDate.now());
        startTime.setItems(initializeBusinessHours(ZoneId.systemDefault(), ZoneId.of("America/New_York"), LocalTime.of(8, 0), 13));
        startTime.getSelectionModel().selectFirst();
        endTime.setItems(initializeBusinessHours(ZoneId.systemDefault(), ZoneId.of("America/New_York"), LocalTime.of(9, 0), 13));
        endTime.getSelectionModel().selectFirst();
        //validationFunction(appointmentDateTime,  existingAppointments, newAppointmentStart, newAppointmentEnd);


        /*
            private ObservableList<Customer> masterData = FXCollections.observableArrayList();
            private final ObservableList<String> startTimes = FXCollections.observableArrayList();
            private final ObservableList<String> endTimes = FXCollections.observableArrayList();
            private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
            private final DateTimeFormatter dateDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
            ObservableList<Appointment> apptTimeList;


            ZoneId osZId = ZoneId.systemDefault();
            ZoneId businessZId = ZoneId.of("America/New_York");
            LocalTime startTime = LocalTime.of(8, 0);
            int workHours = 13;
         */
    }

private static  ObservableList<LocalTime> initializeBusinessHours(ZoneId systemZoneId, ZoneId businessZoneId, LocalTime startHour, int workingHours) {
        ZonedDateTime businessStartTime = ZonedDateTime.of(LocalDate.now(), startHour, businessZoneId);
        ZonedDateTime localStartTime = businessStartTime.withZoneSameInstant(systemZoneId);
        int localStartingHour = localStartTime.getHour();

        return IntStream.range(localStartingHour, localStartingHour + workingHours).mapToObj(i -> LocalTime.of(i % 24, 0))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        }

}

