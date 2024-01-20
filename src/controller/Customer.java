package controller;

import database.AppointmentQuery;
import database.CustomerQuery;
import helper.JDBC;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static helper.Util.confirmAlert;
import static helper.Util.errorAlert;

public class Customer implements Initializable {
    @FXML private TableView<model.Customer> customerTable;
    @FXML private TableColumn<model.Customer, Integer> customerID;
    @FXML private TableColumn<model.Customer, String> Name;
    @FXML private TableColumn<model.Customer, String> Address;
    @FXML private TableColumn<model.Customer, Integer> Country;
    @FXML private TableColumn<model.Customer, String> postalCode;
    @FXML private TableColumn<model.Customer, String> phoneNumber;
    @FXML private TableColumn<model.Customer, Integer> Division;
    @FXML private Button addCustomer;
    @FXML private Button modifyCustomer;
    @FXML private Button deleteCustomer;

    ObservableList<model.Customer> customers = CustomerQuery.getCustomerList();

    public void onAddCustomer(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/AddCustomer.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void onModifyCustomer(ActionEvent actionEvent) throws IOException, SQLException {
        if(customerTable.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../view/UpdateCustomer.fxml"));
            loader.load();

            UpdateCustomer MCController = loader.getController();
            MCController.getCustomerInfo(customerTable.getSelectionModel().getSelectedItem());

            Stage stage = (Stage)((Button) actionEvent.getSource()).getScene().getWindow();
            Parent parent = loader.getRoot();
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selection");
            alert.setContentText("Please select a customer to continue.");
            alert.showAndWait();
        }
    }

    public void onDeleteCustomer(ActionEvent actionEvent) {
        ObservableList<Appointment> appointments = AppointmentQuery.getAppointmentList();
        model.Customer customer = customerTable.getSelectionModel().getSelectedItem();

        if(customer !=null) {
            int customer_Id = customer.getCustomerId();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm deletion");
            alert.setContentText("Are you sure you want to delete the selected data? This action cannot be undone");

            alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK) {
                for (Appointment appointment : appointments) {
                    if (appointment.getCustomerId() == customer_Id) {
                        AppointmentQuery.deleteAppointment(appointment.getAppointmentId());
                    }
                }
                int deletedCustomer = CustomerQuery.deleteCustomer(customer_Id);
                 if(deletedCustomer > 0) {
                     confirmAlert("Deletion Successful", "Customer and associated appointments successfully deleted");
                     customerTable.setItems(CustomerQuery.getCustomerList());
                     customerTable.refresh();
                 } else {
                     errorAlert("Deletion Error", "Failed to delete customer and associated appointments");
                 }
            }

        } else {
            errorAlert("NO Selection", "Please select a customer for deletion.");
        }
    }

    public void onBack(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/MainScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerTable.setItems(CustomerQuery.getCustomerList());
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        Name.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        Address.setCellValueFactory(new PropertyValueFactory<>("address"));
        Country.setCellValueFactory(new PropertyValueFactory<>("country"));
        postalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phone"));
        Division.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
    }
}
