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
import java.time.LocalDate;
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

    public void onSave(ActionEvent actionEvent) {
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
