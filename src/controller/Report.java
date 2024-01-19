package controller;

import database.AppointmentQuery;
import database.ContactQuery;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.ResourceBundle;

public class Report implements Initializable {
    public TableView<Appointment> customerCountTable;
    public TableView<Appointment> contactScheduleTable;
    @FXML private ComboBox<Contact> contactCombo;
    @FXML private Tab customerCountTab;
    @FXML private Tab contactScheduleTab;
    @FXML private TableColumn<Appointment, Integer> countColumn;
    @FXML private TableColumn<Appointment, String> typeColumn;
    @FXML private TableColumn<Appointment, String> monthColumn;
    @FXML private TableColumn<Appointment, Integer> appointmentID;
    @FXML private TableColumn<Appointment, Integer> title;
    @FXML private TableColumn<Appointment, String> type;
    @FXML private TableColumn<Appointment, String> description;
    @FXML private TableColumn<Appointment, Timestamp> start;
    @FXML private TableColumn<Appointment, Timestamp> end;
    @FXML private TableColumn<Appointment, Integer> customerID;
    @FXML private TableColumn<Appointment, Integer> contact;
    @FXML private Button back;

    public void onBack(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void onContactCombo(ActionEvent actionEvent) {
        int contacts = contactCombo.getSelectionModel().getSelectedItem().getContactId();
        contactScheduleTable.setItems(AppointmentQuery.getContactAppointment(contacts));
    }

    /**
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Initialize customer appointments table
        countColumn.setCellValueFactory(new PropertyValueFactory<>("countColumn"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeColumn"));
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("monthColumn"));
        ObservableList<Appointment> a ;
        //customerCountTable.setItems(reportQuery.getAllReports());

        //Initialize contact schedule table
        appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        start.setCellValueFactory(new PropertyValueFactory<>("start"));
        end.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        contactCombo.setItems(ContactQuery.getAllContacts());
        //Initialize custom reports table



    }
}
