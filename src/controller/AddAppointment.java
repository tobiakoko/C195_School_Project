package controller;

import database.AppointmentQuery;
import database.ContactQuery;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddAppointment implements Initializable {
    @FXML private Button save;
    @FXML private Button cancel;
    @FXML private TextField appointmentID;
    @FXML private TextField title;
    @FXML private TextField description;
    @FXML private TextField location;
    @FXML private ComboBox<Contact> contact;
    @FXML private TextField type;
    @FXML private TextField customerID;
    @FXML private TextField userID;
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Please select a valid contact");
            alert.setContentText("Please select a valid contact");
            alert.showAndWait();
            return;
        }
        int appointmentContact = apptContact.getContactId();

        LocalDate start_date = startDate.getValue();
        if(start_date == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Please select a valid start date");
            alert.setContentText("The start date field is blank. Please choose a date");
            alert.showAndWait();
            return;
        }

        LocalTime start_time = startTime.getValue();
        if(start_time == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Please select a valid start time");
            alert.setContentText("The start time field is blank. Please choose a start time.");
            alert.showAndWait();
            return;
        }
        LocalDateTime start = LocalDateTime.of(startDate.getValue(), startTime.getValue());

        LocalDate end_date = endDate.getValue();
        if(end_date == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Please select a valid end date");
            alert.setContentText("The end date field is blank. Please choose a date");
            alert.showAndWait();
            return;
        }

        LocalTime end_time = endTime.getValue();
        if(start_time == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Please select a valid end time");
            alert.setContentText("The start date field is blank. Please choose an end time");
            alert.showAndWait();
            return;
        }
        LocalDateTime end = LocalDateTime.of(endDate.getValue(), endTime.getValue());

        Customer customer = customer.getValue().getCustomerId();
        if (customer == null) {
            //Error 22
            return;
        }
        int CustomerID = customer.getValue().getCustomerId();

        User user = user.getValue();
        if(user == null) {
            //Error 23
            return;
        }
        int userId = user.getValue().getUserID();
        String Location = location.getText();

        if (title.isBlank() || title.isEmpty()) {
            //Error 8
        } else if (description.isBlank() || description.isEmpty()) {
            //Error 9
        } else if(description.isBlank() || description.isEmpty()) {
            //Error 10
        } else if (description.isBlank() || description.isEmpty()) {
            //Error 11
        } else if (Appointment.businessHours(start, end)) {
            return;
        } else if (Appointment.overlapCheck(customerID, start, end)) {
            return;
        } else {
            AppointmentQuery.addAppointment(apptTitle, apptDescription, Location, apptType, start, end, CustomerID, userId, appointmentContact);
            //controller.Appointment.back(actionEvent);
            //confirmation 5
        }


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
        contact.setItems(contacts);
    }
}
