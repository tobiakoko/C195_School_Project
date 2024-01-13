package controller;

import database.ContactQuery;
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

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.Objects;
import java.util.ResourceBundle;

public class UpdateAppointment implements Initializable {

    @FXML private Button save;
    @FXML private Button cancel;
    @FXML private TextField Title;
    @FXML private TextField Description;
    @FXML private TextField Location;
    @FXML private TextField Type;
    @FXML private TextField customerId;
    @FXML private TextField userId;
    @FXML private ComboBox<model.Contact> Contact;
    @FXML private ComboBox<LocalTime> startTime;
    @FXML private ComboBox<LocalTime> endTime;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;

    public void getAppointmentInfo(Appointment appointment) {

    }

    public void onSave(ActionEvent actionEvent) {
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

        ObservableList<Contact> customerList = ContactQuery.getAllContacts();
        Contact.setItems(customerList);
        Contact.setVisibleRowCount(10);

        ObservableList<Contact> userList = ContactQuery.getAllContacts();
        Contact.setItems(userList);
        Contact.setVisibleRowCount(10);
    }
}
