package controller;

import database.AppointmentQuery;
import database.CustomerQuery;
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

/**
 * Controller class for handling customer-related actions.
 * The Customer class manages the user interface and logic for displaying and manipulating customer data.
 * It allows users to view a list of existing customers, add new customers, modify selected customers, and delete them.
 *
 * @author Daniel Akoko
 */
public class Customer implements Initializable {
    @FXML private TableView<model.Customer> customerTable;
    @FXML private TableColumn<model.Customer, Integer> customerID;
    @FXML private TableColumn<model.Customer, String> Name;
    @FXML private TableColumn<model.Customer, String> Address;
    @FXML private TableColumn<model.Customer, Integer> Country;
    @FXML private TableColumn<model.Customer, String> postalCode;
    @FXML private TableColumn<model.Customer, String> phoneNumber;
    @FXML private TableColumn<model.Customer, Integer> Division;

    /**
     * Handles the action when "Add Customer" button is clicked.
     * Opens the AddCustomer.fxml view to create a new customer.
     *
     * @param actionEvent The event triggering the action.
     * @throws IOException If there is an error loading the scene.
     */
    public void onAddCustomer(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/AddCustomer.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the action when "Modify Customer" button is clicked.
     * Checks if a customer is selected in the table.
     * Loads the UpdateCustomer.fxml view and passes the selected customer information to its controller.
     * Shows the UpdateCustomer.fxml view with pre-populated data.
     * Displays an error message if no customer is selected.
     *
     * @param actionEvent The event triggering the action.
     * @throws IOException If there is an error loading the scene.
     * @throws SQLException If there is an SQL exception
     */
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
            alert.setTitle("SELECTION ERROR");
            alert.setContentText("Please select a customer to continue.");
            alert.showAndWait();
        }
    }

    /**
     * Handles the action when "Delete Customer" button is clicked.
     * Checks if a customer is selected in the table.
     * Displays a confirmation alert for deletion.
     * Removes all associated appointments before deleting the customer.
     * Uses CustomerQuery.deleteCustomer to remove the selected customer from the database.
     * Refreshes the table with updated data.
     * Displays success or error messages based on the deletion outcome.
     *
     * @param actionEvent The event triggering the action.
     */
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
                // Deleting associated appointments
                for (Appointment appointment : appointments) {
                    if (appointment.getCustomerId() == customer_Id) {
                        AppointmentQuery.deleteAppointment(appointment.getAppointmentId());
                    }
                }

                // Deleting the customer
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
            errorAlert("SELECTION ERROR", "Please select a customer for deletion.");
        }
    }

    /**
     * Handles the action when "Back" button is clicked.
     * Navigates back to the MainScreen.fxml view.
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
     * Initializes the controller.
     * Sets the initial data for the customerTable using CustomerQuery.getCustomerList.
     * Initializes cell value factories for each table column using property value factories.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populating the customer table with data
        customerTable.setItems(CustomerQuery.getCustomerList());
        // Setting up cell value factories for table columns
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        Name.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        Address.setCellValueFactory(new PropertyValueFactory<>("address"));
        Country.setCellValueFactory(new PropertyValueFactory<>("country"));
        postalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phone"));
        Division.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
    }
}
