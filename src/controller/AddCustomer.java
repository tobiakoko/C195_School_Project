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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static helper.Util.errorAlert;

/**
 * Controller class for handling the addition of customers.
 * The AddCustomer class manages the user interface and logic for adding a new customer.
 * It allows users to input customer information including name, address, postal code, phone number, and division/country selection.
 * It interacts with database queries to validate country-specific divisions and add new customer data.
 *
 * @author Daniel Akoko
 */
public class AddCustomer implements Initializable {
    @FXML private TextField customerID;
    @FXML private TextField Name;
    @FXML private TextField Address;
    @FXML private TextField postalCode;
    @FXML private TextField phoneNumber;
    @FXML private ComboBox<model.Division> Division;
    @FXML private ComboBox<model.Country> Country;

    /**
     * Handles the country selection action.
     * Reacts to a change in the selected country and updates the "Division" ComboBox accordingly.
     *         Retrieves the selected Country object.
     *         Uses DivisionQuery.showDivision to fetch relevant divisions based on the chosen country ID.
     *         Sets the items of the Division ComboBox to the retrieved list of divisions.
     *         Catches and throws any SQL exceptions.
     *
     * @param actionEvent The event triggering the country selection action.
     * @throws SQLException If a SQL exception occurs.
     */
    public void onCountry(ActionEvent actionEvent) throws SQLException {

        model.Country country = Country.getValue();
        try{
            // Populate the division dropdown based on the selected country
            Division.setItems(DivisionQuery.showDivision(country.getCountryId()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the save action for adding a customer.
     * Attempts to save a new customer based on user input.
     * Parses the customer ID (customerID) as an integer.
     * Validates and displays error messages for empty or blank fields:
     *     Customer name (Name)
     *     Customer address (Address)
     *     Customer postal code (postalCode)
     *     Customer phone number (phoneNumber)
     * Validates and displays error messages for phone number format
     * Retrieves the selected Division object and gets its division ID.
     * Uses CustomerQuery.addCustomer to add the new customer data to the database.
     * Navigates to the CustomerScreen if successful.
     * Catches and throws exceptions for invalid field formats (NumberFormatException) or database errors (SQLException).
     *
     * @param actionEvent The event triggering the save action.
     */
    public void onSave(ActionEvent actionEvent) {
        try{
            // Extracting input data from the form
            //int customer_ID = Integer.parseInt(customerID.getText());

            String name = Name.getText();
            if (name.isEmpty()){
                errorAlert("Customer Name is blank", "The customer Name field is blank. \nPlease enter a valid name" );
                return;
            }
            String address = Address.getText();
            if(address.isEmpty() || address.isBlank()){
                errorAlert("Customer Address is blank", "The customer address field is blank. \nPlease enter a valid address" );
                return;
            }
            String postal_Code = postalCode.getText();
            if(postal_Code.isEmpty() || postal_Code.isBlank()){
                errorAlert("Customer Postal Code is blank", "The customer postal code field is blank. \nPlease enter a valid postal code" );
                return;
            }
            String phone = phoneNumber.getText();
            if(phone.isEmpty() || phone.isBlank()) {
                errorAlert("Error phone number", "Please enter a valid phone number" );
                return;
            }
            // I hope this is allowed
            String regex = "^\\d{10}$"; // Matches 10-digit numbers only
            if (!phone.matches(regex)) {
                errorAlert("Error phone number", "Phone number must be 10 digits. Please enter a valid format.");
                return;
            }

            // Extracting division_ID only if a division is selected
            int division_ID = (Division.getValue() != null) ? Division.getValue().getDivisionId() : 0;

            // Adding the customer to the database
            CustomerQuery.addCustomer(name, address, postal_Code, phone, division_ID);

            // Redirecting to the main customer screen
            Parent parent = FXMLLoader.load(getClass().getResource("../view/CustomerScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (NumberFormatException | IOException | SQLException e) {
            e.printStackTrace();
            errorAlert("Error", "An unexpected error occurred. Please try again.");
        }
    }

    /**
     * Handles the cancellation action for adding a customer.
     * Displays a confirmation alert before returning to the CustomerScreen.
     *
     * @param actionEvent The event triggering the cancellation action.
     * @throws IOException If an I/O error occurs.
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        // Displaying confirmation dialog for cancellation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm cancellation");
        alert.setContentText("Are you sure you want to leave this page? Changes will not be saved");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.YES) {
            // Redirecting to the main customer screen
            Parent parent = FXMLLoader.load(getClass().getResource("../view/CustomerScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Initializes the controller.
     * Populates the Country ComboBox with all available countries using CountryQuery.getAllCountry.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Populating the country dropdown with data
        Country.setItems(CountryQuery.getAllCountry());
    }
}
