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
    @FXML private Button addAppointment;
    @FXML private Button deleteAppointment;
    @FXML private Button updateAppointment;

    ObservableList<model.Appointment> AppointmentList = FXCollections.observableArrayList();

    @FXML void onAllAppointment(ActionEvent actionEvent) {
        appointmentTable.setItems(AppointmentQuery.getAppointmentList());
        appointmentTable.refresh();
    }

    @FXML void onMonthlyAppointment(ActionEvent actionEvent) {
        appointmentTable.setItems(AppointmentQuery.getMonthlyAppointment());
        appointmentTable.setPlaceholder(new Label("No appointments exist within the next month"));
        appointmentTable.refresh();
    }

    @FXML void onWeeklyAppointment(ActionEvent actionEvent) {
        appointmentTable.setItems(AppointmentQuery.getApptByWeek());
        appointmentTable.setPlaceholder(new Label("No appointments exist within the next week"));
        appointmentTable.refresh();
    }

    @FXML void onAddAppointment(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/AddAppointment.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

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

            AppointmentQuery.deleteAppointment(selectedAppointment.getAppointmentId());
            AppointmentList = AppointmentQuery.getAppointmentList();
            appointmentTable.setItems(AppointmentList);
            appointmentTable.refresh();
        } else if (alert.getResult() == ButtonType.CANCEL) {
            alert.close();
        }
    }


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

    @FXML void back(ActionEvent actionEvent) throws IOException {
        loadScene("../view/MainScreen.fxml", actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.allAppointment.setToggleGroup(appointment);
        this.weeklyAppointment.setToggleGroup(appointment);
        this.monthlyAppointment.setToggleGroup(appointment);

        appointmentTable.setItems(AppointmentQuery.getAppointmentList());
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
     *
     * @param fxmlPath    The FXML file path.
     * @param actionEvent The ActionEvent associated with the event.
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
