package controller;

import database.AppointmentQuery;
import database.ContactQuery;
import database.CountryQuery;
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
import model.Country;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
/**
 * Controller class for the Report Screen, handling various report-related functionalities.
 * The Report class manages the user interface and logic for generating various reports in the application.
 * It offers tabs for reports on appointment types, monthly appointment counts, and contact schedules.
 * Users can select a contact from a combo box to view their associated appointments.
 */
public class Report implements Initializable {

    @FXML private TableView<Country> monthTable;
    @FXML private TableColumn<Country, Integer> monthCount;
    @FXML private TableColumn<Country, String> month;
    @FXML private TableView<Appointment> typeCountTable;
    @FXML private TableColumn<Appointment, Integer> typeTotal;
    @FXML private TableColumn<Appointment, String> typeColumn;
    @FXML private TableView<Appointment> monthCountTable;
    @FXML private TableColumn<Appointment, Integer> monthTotal;
    @FXML private TableColumn<Appointment, String> monthColumn;
    @FXML private TableView<Appointment> contactScheduleTable;
    @FXML private TableColumn<Appointment, Integer> appointmentID;
    @FXML private TableColumn<Appointment, String> title;
    @FXML private TableColumn<Appointment, String> type;
    @FXML private TableColumn<Appointment, String> description;
    @FXML private TableColumn<Appointment, Timestamp> start;
    @FXML private TableColumn<Appointment, Timestamp> end;
    @FXML private TableColumn<Appointment, Integer> customerID;
    @FXML private TableColumn<Appointment, Integer> contact;
    @FXML private ComboBox<Contact> contactCombo;

    /**
     * Event handler for the "Back" button click.
     * Navigates back to the main screen.
     *
     * @param actionEvent The event triggering the action.
     * @throws IOException If there is an error loading the scene.
     */
    public void onBack(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Event handler for the selection in the "Contact Combo" box.
     * Updates the contact schedule table based on the selected contact from the combo box.
     *
     * @param actionEvent The event triggering the action.
     */
    public void onContactCombo(ActionEvent actionEvent) {
        int contacts = contactCombo.getSelectionModel().getSelectedItem().getContactId();
        contactScheduleTable.setItems(AppointmentQuery.getContactAppointment(contacts));
    }

    /**
     * Initializes the Report Screen.
     * Initializes data for each report tab:
     *     Appointments by Type: Fills the typeCountTable with appointment types and their occurrences.
     *     Appointments by Month: Fills the monthCountTable with appointment counts for each month.
     *     Contact Schedule:
     *         Populates the contactCombo with available contacts.
     *         Sets a placeholder message for the table when no contact is selected.
     *         Initializes table columns with appointment details (ID, title, type, etc.).
     *         Fills the table with appointments for the currently selected contact (updated with onContactCombo).
     *
     * @param url            The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Initialize customer appointments table
        typeCountTable.setItems(AppointmentQuery.getAppointmentType());
        typeTotal.setCellValueFactory(new PropertyValueFactory<>("typeTotal"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        monthCountTable.setItems(AppointmentQuery.getAppointmentTypeMonth());
        monthTotal.setCellValueFactory(new PropertyValueFactory<>("typeTotal"));
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        //Initialize contact schedule table
        ObservableList<Contact> contacts = ContactQuery.getAllContacts();
        contactCombo.setItems(contacts);
        contactScheduleTable.setPlaceholder(new Label("Select contact from the contact list above"));
        appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        start.setCellValueFactory(new PropertyValueFactory<>("start"));
        end.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        contactScheduleTable.refresh();

        //Initialize Country (custom) reports table
        monthTable.setItems(CountryQuery.countryTotals());
        month.setCellValueFactory(new PropertyValueFactory<>("month"));
        monthCount.setCellValueFactory(new PropertyValueFactory<>("monthCount"));

    }
}
