package controller;

import database.*;
import helper.JDBC;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

import static helper.Util.errorAlert;
import static helper.Util.validationFunction;

public class UpdateAppointment implements Initializable {

    @FXML private ComboBox<Customer> customerId;
    @FXML private ComboBox<User> userId;
    @FXML private Button save;
    @FXML private Button cancel;
    @FXML private TextField appointmentId;
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


    public void onSave(ActionEvent actionEvent) throws IOException {
        int appointment_Id = selectedAppointment.getAppointmentId();
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
            errorAlert("Please select a valid contact", "Please select a valid contact");
        } else if(description.isBlank() || description.isEmpty()) {
            errorAlert("Please select a valid contact", "Please select a valid contact");
        } else if(type.isBlank() || type.isEmpty()) {
            errorAlert("Please select a valid contact", "Please select a valid contact");
        } else if (location.isBlank() || location.isEmpty()) {
            errorAlert("Please select a valid contact", "Please select a valid contact");
        } else {
            //Appointment Time OverLap and Business hours validation needed here
            AppointmentQuery.modifyAppointment(appointment_Id, title, description, location, type, start_date_time, end_date_time, customerID, userID, contactID);

            Parent parent = FXMLLoader.load(getClass().getResource("../view/Appointments.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }

        // validationFunction(LocalDateTime.now(ZoneId.systemDefault()), AppointmentQuery.getAppointmentList(), start_Date_Time, end_Date_Time);
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
        //appointmentId.appointment.getAppointmentId();
        Title.setText(appointment.getTitle());
        Description.setText(appointment.getDescription());
        Location.setText(appointment.getLocation());
        Type.setText(appointment.getType());
        startDate.setValue(appointment.getStart().toLocalDate());
        startTime.setValue(appointment.getStart().toLocalTime());
        endDate.setValue(appointment.getEnd().toLocalDate());
        endTime.setValue(appointment.getEnd().toLocalTime());
        Contact contact = ContactQuery.returnContactList(appointment.getContact());
        Contact.setValue(contact);
        Customer customer = CustomerQuery.returnCustomerList(appointment.getCustomerId());
        customerId.setValue(customer);
        User user = UserQuery.returnUserId(appointment.getUserId());
        userId.setValue(user);
    }
}
