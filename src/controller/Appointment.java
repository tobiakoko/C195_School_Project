package controller;

import database.AppointmentQuery;
import helper.Util;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.ResourceBundle;

import static helper.Util.confirmAlert;

/**
 * Controller class for handling appointments.
 * The Appointment class manages the user interface and logic for displaying and manipulating appointments.
 * It allows users to filter appointments by all, weekly, and monthly views, add new appointments,
 * delete existing appointments, and update selected appointments.
 *
 * @author Daniel Akoko
 */
public class Appointment implements Initializable {
    @FXML private RadioButton allAppointment;
    @FXML private ToggleGroup appointment;
    @FXML private RadioButton weeklyAppointment;
    @FXML private RadioButton monthlyAppointment;
    @FXML private TableView<model.Appointment> appointmentTable;
    @FXML private TableColumn<model.Appointment, Integer> appointmentID;
    @FXML private TableColumn<model.Appointment, String> title;
    @FXML private TableColumn<model.Appointment, String> description;
    @FXML private TableColumn<model.Appointment, String> location;
    @FXML private TableColumn<model.Appointment, Integer> contact;
    @FXML private TableColumn<model.Appointment, String> type;
    @FXML private TableColumn<model.Appointment, Timestamp> start;
    @FXML private TableColumn<model.Appointment, Timestamp> end;
    @FXML private TableColumn<model.Appointment, Integer> customerID;
    @FXML private TableColumn<model.Appointment, Integer> userID;

    ObservableList<model.Appointment> AppointmentList = FXCollections.observableArrayList();

    /**
     * Handles the action when "All Appointments" is selected.
     * Sets the appointmentTable data to display all appointments from the AppointmentQuery.getAppointmentList method.
     *
     * @param actionEvent The event triggering the action.
     */
    @FXML void onAllAppointment(ActionEvent actionEvent) {
        appointmentTable.setItems(AppointmentQuery.getAppointmentList());
        appointmentTable.refresh();
    }

    /**
     * Handles the action when "Monthly Appointments" is selected.
     * Sets the appointmentTable data to display appointments within the next month from the AppointmentQuery.getMonthlyAppointment method.
     * Sets a placeholder message displaying "No appointments exist within the next month" if no appointments are found.
     *
     * @param actionEvent The event triggering the action.
     */
    @FXML void onMonthlyAppointment(ActionEvent actionEvent) {
        appointmentTable.setItems(AppointmentQuery.getMonthlyAppointment());
        appointmentTable.setPlaceholder(new Label("No appointments exist within the next month"));
        appointmentTable.refresh();
    }

    /**
     * Handles the action when "Weekly Appointments" is selected.
     * Sets the appointmentTable data to display appointments within the next week from the AppointmentQuery.getApptByWeek method.
     * Sets a placeholder message displaying "No appointments exist within the next week" if no appointments are found.
     *
     * @param actionEvent The event triggering the action.
     */
    @FXML void onWeeklyAppointment(ActionEvent actionEvent) {
        appointmentTable.setItems(AppointmentQuery.getApptByWeek());
        appointmentTable.setPlaceholder(new Label("No appointments exist within the next week"));
        appointmentTable.refresh();
    }

    /**
     * Handles the action when "Add Appointment" button is clicked.
     * Opens the AddAppointment.fxml view to create a new appointment.
     *
     * @param actionEvent The event triggering the action.
     * @throws IOException
     */
    @FXML void onAddAppointment(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/AddAppointment.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the action when "Delete Appointment" button is clicked.
     * Checks if an appointment is selected in the table.
     * Displays confirmation alerts for deletion and confirms user intent.
     * Uses AppointmentQuery.deleteAppointment to remove the selected appointment from the database.
     * Refreshes the table with updated data.
     *
     * @param actionEvent The event triggering the action.
     */
    @FXML void onDeleteAppointment(ActionEvent actionEvent) {
        model.Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            confirmAlert("SELECTION ERROR", "No appointment selected. Please select an appointment to delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Alert");
        alert.setContentText("Would you like to remove the selected appointment?");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Alert");
            confirm.setContentText("Appointment has been deleted");
            confirm.getButtonTypes().clear();
            confirm.getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
            confirm.showAndWait();

            // Deleting the appointment from the database
            AppointmentQuery.deleteAppointment(selectedAppointment.getAppointmentId());
            AppointmentList = AppointmentQuery.getAppointmentList();
            appointmentTable.setItems(AppointmentList);
            appointmentTable.refresh();
        } else if (alert.getResult() == ButtonType.CANCEL) {
            alert.close();
        }
    }

    /**
     * Handles the action when "Update Appointment" button is clicked.
     * Tries to load the UpdateAppointment.fxml view.
     * Retrieves the selected appointment from the table.
     * Passes the selected appointment to the UpdateAppointment controller via modifyAppointment method.
     * Opens the UpdateAppointment.fxml view with the selected appointment details.
     * Catches exceptions for missing selection or database errors.
     *
     * @param actionEvent The event triggering the action.
     * @throws IOException If there is an error loading the scene.
     */
    @FXML void onUpdateAppointment(ActionEvent actionEvent) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/UpdateAppointment.fxml"));
            Parent parent = loader.load();
            UpdateAppointment modifyAppointment = loader.getController();
            model.Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
            modifyAppointment.modifyAppointment(selectedAppointment);

            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (RuntimeException e) {
            Util.errorAlert("SELECTION ERROR", "No Appointment was selected. Please select an appointment");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the action when "Back" button is clicked.
     * Calls the loadScene method to navigate back to the MainScreen.fxml view.
     *
     * @param actionEvent The event triggering the action.
     * @throws IOException If there is an error loading the scene.
     */
    @FXML void back(ActionEvent actionEvent) throws IOException {
        loadScene("../view/MainScreen.fxml", actionEvent);
    }

    /**
     * Initializes the controller.
     * Configures the radio buttons within the appointment toggle group.
     * Sets the initial data for the appointment table using AppointmentQuery.getAppointmentList.
     * Initializes cell value factories for each table column using property value factories.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Setting up toggle group for radio buttons
        this.allAppointment.setToggleGroup(appointment);
        this.weeklyAppointment.setToggleGroup(appointment);
        this.monthlyAppointment.setToggleGroup(appointment);

        // Populating the appointment table with data
        appointmentTable.setItems(AppointmentQuery.getAppointmentList());
        // Setting up cell value factories for table columns
        appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        start.setCellValueFactory(new PropertyValueFactory<>("start"));
        end.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userID.setCellValueFactory(new PropertyValueFactory<>("userId"));
    }

    /**
     * Load a new scene with the given FXML file path.
     * Sets the new scene on the current stage.
     *
     * @param fxmlPath    The FXML file path.
     * @param actionEvent The event triggering the action.
     * @throws IOException If there is an error loading the scene.
     */
    private void loadScene(String fxmlPath, ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
