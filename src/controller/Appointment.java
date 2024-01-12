package controller;

import database.AppointmentQuery;
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
import java.sql.Timestamp;
import java.util.Objects;
import java.util.ResourceBundle;

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

    public void onAllAppointment(ActionEvent actionEvent) {
        appointmentTable.setItems(AppointmentQuery.getAppointmentList());
    }

    public void onMonthlyAppointment(ActionEvent actionEvent) {
        appointmentTable.setItems(AppointmentQuery.getMonthlyAppointment());
        appointmentTable.setPlaceholder(new Label("Currently, no appointments exist within the next month."));
    }

    public void onWeeklyAppointment(ActionEvent actionEvent) {
        appointmentTable.setItems(AppointmentQuery.getApptByWeek());
        appointmentTable.setPlaceholder(new Label("Currently, no appointments exist within the next week."));
    }

    public void onAddAppointment(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("./view/AddAppointment.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void onDeleteAppointment(ActionEvent actionEvent) {
        model.Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if(selectedAppointment == null ) {

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Alert");
            alert.setContentText("Would you like to remove the selected appointment?");
            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK){
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Alert");
                confirm.setContentText("Appointment ID " + appointmentTable.getSelectionModel().getSelectedItem().getAppointmentId() + " for " +
                        appointmentTable.getSelectionModel().getSelectedItem().getType() + "has been deleted" );
                confirm.getButtonTypes().clear();
                confirm.getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.OK);
                confirm.showAndWait();

                AppointmentQuery.deleteAppointment(appointmentTable.getSelectionModel().getSelectedItem().getAppointmentId());
                AppointmentList = AppointmentQuery.getAppointmentList();
                appointmentTable.setItems(AppointmentList);
                appointmentTable.refresh();

            } else if (alert.getResult() == ButtonType.CANCEL) {
                alert.close();
            }
        }
    }

    public void onUpdateAppointment(ActionEvent actionEvent) throws IOException {
        if(appointmentTable.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("./view/MainScreen.fxml"));
            UpdateAppointment MCController = loader.getController();
            MCController.getAppointmentInfo(appointmentTable.getSelectionModel().getSelectedItem());
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Appointment Selected");
            alert.setContentText("NO appointment was selected");
            alert.showAndWait();
        }
    }

    public void back(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("./view/MainScreen.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentTable.setItems(AppointmentQuery.getAppointmentList());
        appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        title.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        description.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        location.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        contact.setCellValueFactory(new PropertyValueFactory<>("appointmentContact"));
        type.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        start.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        end.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
        customerID.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerId"));
        userID.setCellValueFactory(new PropertyValueFactory<>("appointmentUserId"));
    }
}
