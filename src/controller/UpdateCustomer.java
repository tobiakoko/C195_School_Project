package controller;

import database.CountryQuery;
import database.CustomerQuery;
import database.DivisionQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static helper.Util.errorAlert;

/**
 * Controller class for the Update Customer Screen, handling customer information modification.
 * The UpdateCustomer class manages the user interface and logic for updating existing customer information in the application.
 * Users can edit details like name, address, postal code, phone number, and division.
 * The system validates for empty fields and provides error messages for invalid entries.
 * Updated customer information is saved to the database upon successful validation.
 *
 * @author Daniel Akoko
 */
public class UpdateCustomer implements Initializable {

    @FXML private TextField customerID;
    @FXML private TextField Name;
    @FXML private TextField Address;
    @FXML private TextField postalCode;
    @FXML private TextField phoneNumber;
    @FXML private ComboBox<model.Division> Division;
    @FXML private ComboBox<model.Country> Country;

    /**
     * Populates the Update Customer Screen with details of the selected customer for modification.
     * Fills the UI elements with existing data from the selected customer.
     * Retrieves and sets corresponding division and country information based on the customer's division ID.
     *
     * @param customer The customer whose details are being modified.
     * @throws SQLException If there is an error with the SQL query.
     */
    public void getCustomerInfo(Customer customer) throws SQLException {
        // Set values in the input fields based on the selected customer
        customerID.setText(Integer.toString(customer.getCustomerId()));
        Name.setText(customer.getCustomerName());
        Address.setText(customer.getAddress());
        phoneNumber.setText(customer.getPhone());
        postalCode.setText(customer.getPostalCode());
        model.Division d = DivisionQuery.returnDivisionLevel(customer.getDivisionId());
        Division.setValue(d);
        model.Country c = CountryQuery.returnCountry(customer.getCustomerId());
        Country.setValue(c);
        model.Country C = Country.getValue();
        Division.setItems(DivisionQuery.showDivision(C.getCountryId()));
    }

    /**
     * Event handler for the "Save" button click, saving the modified customer details.
     * Saves edited customer details upon clicking the "Save" button:
     *     Extracts input from text fields and combo boxes.
     *     Validates for empty fields and phone number format.
     *     Updates the customer in the database if valid.
     *     Navigates back to the main customer screen.
     *
     * @param actionEvent The event triggering the action.
     */
    public void onSave(ActionEvent actionEvent) {
        try {
            // Retrieve customer details from the input fields
            int customer_ID = Integer.parseInt(customerID.getText());
            String name = Name.getText();
            String address = Address.getText();
            String postal_Code = postalCode.getText();
            String phone = phoneNumber.getText();

            // Validate input fields
            if (name.isEmpty()) {
                errorAlert("Customer Name is blank", "The customer Name field is blank. Please enter a valid name");
                return;
            } else if (address.isEmpty() || address.isBlank()) {
                errorAlert("Customer Address is blank", "The customer address field is blank. Please enter a valid address");
                return;
            } else if (postal_Code.isEmpty() || postal_Code.isBlank()) {
                errorAlert("Customer Postal Code is blank", "The customer postal code field is blank. Please enter a valid postal code");
                return;
            } else if (phone.isEmpty() || phone.isBlank()) {
                errorAlert("Error phone number", "Please enter a valid phone number");
                return;
            }

            int division_ID = Division.getValue().getDivisionId();
            // Update the customer details in the database
            CustomerQuery.updateCustomer(customer_ID, name, address, postal_Code, phone, division_ID);

            // Return to the Customer Screen
            Parent parent = FXMLLoader.load(getClass().getResource("../view/CustomerScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (NumberFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Event handler for the "Cancel" button click, prompting the user for confirmation before leaving the page.
     *
     * @param actionEvent The event triggering the action.
     * @throws IOException If there is an error loading the scene.
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        // Prompt user for confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm cancellation");
        alert.setContentText("Are you sure you want to leave this page? Changes will not be saved");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        // If user confirms, return to the Customer Screen
        if(result.isPresent() && result.get() == ButtonType.YES) {
            Parent parent = FXMLLoader.load(getClass().getResource("../view/CustomerScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Event handler for the "Country" dropdown selection change, updating the "Division" dropdown accordingly.
     *
     * @param actionEvent The event triggering the action.
     * @throws SQLException If there is an error with the SQL query.
     */
    public void onCountry(ActionEvent actionEvent) throws SQLException {
        // Retrieve the selected country ID and update the Division dropdown
        int county_Id = Country.getValue().getCountryId();
        Division.setItems(DivisionQuery.showDivision(county_Id));
        Division.getSelectionModel().selectFirst();
    }

    /**
     * Initializes the UpdateCustomer Screen.
     * Populates the "Country" combo box with all available countries.
     *
     * @param url            The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populate the Country dropdown with data from the database
        Country.setItems(CountryQuery.getAllCountry());
    }
}
