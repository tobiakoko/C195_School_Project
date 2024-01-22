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

public class AddCustomer implements Initializable {
    @FXML private TextField customerID;
    @FXML private TextField Name;
    @FXML private TextField Address;
    @FXML private TextField postalCode;
    @FXML private TextField phoneNumber;
    @FXML private ComboBox<model.Division> Division;
    @FXML private ComboBox<model.Country> Country;

    public void onCountry(ActionEvent actionEvent) throws SQLException {

        model.Country country = Country.getValue();
        try{
            Division.setItems(DivisionQuery.showDivision(country.getCountryId()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onSave(ActionEvent actionEvent) {
        try{
            int customer_ID = Integer.parseInt(customerID.getText());
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
            String phone =phoneNumber.getText();
            if(name.isEmpty() || name.isBlank()) {
                errorAlert("Error phone number", "Please enter a valid phone number" );
                return;
            }

            int division_ID = Division.getValue().getDivisionId();
            CustomerQuery.addCustomer(name, address, postal_Code, phone, division_ID);

            Parent parent = FXMLLoader.load(getClass().getResource("../view/CustomerScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (NumberFormatException | IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm cancellation");
        alert.setContentText("Are you sure you want to leave this page? Changes will not be saved");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.YES) {
            Parent parent = FXMLLoader.load(getClass().getResource("../view/CustomerScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Country.setItems(CountryQuery.getAllCountry());
    }
}
