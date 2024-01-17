package controller;

import database.AppointmentQuery;
import database.ContactQuery;
import database.CustomerQuery;
import database.UserQuery;
import helper.JDBC;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

import static helper.Util.validationFunction;

public class UpdateAppointment implements Initializable {

    @FXML private Button save;
    @FXML private Button cancel;
    @FXML private TextField appointmentId;
    @FXML private TextField Title;
    @FXML private TextField Description;
    @FXML private TextField Location;
    @FXML private TextField Type;
    @FXML private ComboBox<Customer> customerComboBox;
    @FXML private ComboBox<User> userComboBox;
    @FXML private ComboBox<model.Contact> Contact;
    @FXML private ComboBox<LocalTime> startTime;
    @FXML private ComboBox<LocalTime> endTime;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;

   model.Appointment selectedAppointment = null;


    public void onSave(ActionEvent actionEvent) {
        int appointment_Id = selectedAppointment.getAppointmentId();
        String title = Title.getText();
        String description = Description.getText();
        String type = Type.getText();
        String location = Location.getText();
        int contactID = Contact.getSelectionModel().getSelectedItem().getContactId();
        int customerID = customerComboBox.getSelectionModel().getSelectedItem().getCustomerId();
        int userID = userComboBox.getSelectionModel().getSelectedItem().getUserId();
        LocalDate start_date = startDate.getValue();
        LocalDate end_date = endDate.getValue();
        LocalTime start_time = startTime.getSelectionModel().getSelectedItem();
        LocalTime end_time = endTime.getSelectionModel().getSelectedItem();
        LocalDateTime start_Date_Time = LocalDateTime.of(start_date.getYear(), start_date.getMonth(), start_date.getDayOfMonth(), start_time.getHour(), start_time.getMinute());
        LocalDateTime end_Date_Time = LocalDateTime.of(end_date.getYear(), end_date.getMonth(), end_date.getDayOfMonth(), end_time.getHour(), end_time.getMinute());

        validationFunction(LocalDateTime.now(ZoneId.systemDefault()), AppointmentQuery.getAppointmentList(), start_Date_Time, end_Date_Time);
    }

    public void onCancel(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("./view/AppointmentScreen.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<Contact> contactList = ContactQuery.getAllContacts();
        Contact.setItems(contactList);
        Contact.setVisibleRowCount(10);

        ObservableList<Customer> customerList = CustomerQuery.getCustomerList();
        Customer.setItems(customerList);
        Customer.setVisibleRowCount(10);

        ObservableList<User> userList = UserQuery.getUserList();
        User.setItems(userList);
        User.setVisibleRowCount(10);
    }

    public void modifyAppointment(Appointment appointment) {
        JDBC.openConnection();
        appointmentId.setText(Integer.toString(appointment.getAppointmentId()));
        Title.setText(appointment.getTitle());
        Description.setText(appointment.getDescription());
        Location.setText(appointment.getLocation());
        Type.setText(appointment.getType());
        customerComboBox.setText(Integer.toString(appointment.getCustomerId()));
        userComboBox.setText(Integer.toString(appointment.getUserId()));
        Contact.setValue(ContactQuery.returnContactList(appointment.getContact()));

        startTime.setItems(Appointment.getTimes());
        endTime.setItems(Appointment.getTimes());
        startTime.setItems(Appointment.getStart().toLocalTime());
        endTime.setItems(appointment.getEnd().toLocalTime());

        startDate.setText(appointment.getTitle());
        endDate.setText(appointment.getTitle());

    }
}
