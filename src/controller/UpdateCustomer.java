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
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;

import static helper.Util.errorAlert;
import static java.time.LocalDateTime.now;

public class UpdateCustomer implements Initializable {

    @FXML private TextField customerID;
    @FXML private TextField Name;
    @FXML private TextField Address;
    @FXML private TextField postalCode;
    @FXML private TextField phoneNumber;
    @FXML private ComboBox<model.Division> Division;
    @FXML private ComboBox<model.Country> Country;

    public void getCustomerInfo(Customer customer) throws SQLException {
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
            int country_ID = Country.getValue().getCountryId();
            String lastUpdatedBy = "script";
            Timestamp lastUpdated = Timestamp.valueOf(now());
            CustomerQuery.updateCustomer(customer_ID, name, address, postal_Code, phone, division_ID);

            Parent parent = FXMLLoader.load(getClass().getResource("../view/CustomerScreen.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (NumberFormatException | IOException e) {
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

    public void onCountry(ActionEvent actionEvent) throws SQLException {
        //model.Country c = Country.getValue();
        //try{
       //     Division.setItems(DivisionQuery.displayDivision(c.getCountryId()));
       // } catch (SQLException e) {
        //    throw new RuntimeException(e);
       // }
        int county_Id = Country.getValue().getCountryId();
        Division.setItems(DivisionQuery.showDivision(county_Id));
        Division.getSelectionModel().selectFirst();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Country.setItems(CountryQuery.getAllCountry());
    }
}
