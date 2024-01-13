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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.ResourceBundle;

import static java.time.LocalDateTime.now;

public class UpdateCustomer implements Initializable {
    @FXML private Button save;
    @FXML private Button cancel;
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
        Division.setItems(DivisionQuery.displayDivision(d.getCountryId()));
    }

    public void onSave(ActionEvent actionEvent) {
        try{
            int customer_ID = Integer.parseInt(customerID.getText());
            String name = Name.getText();
            if (name.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Customer Name is blank");
                alert.setContentText("The customer Name field is blank. \nPlease enter a valid name");
                alert.showAndWait();
                return;
            }
            String address = Address.getText();
            if(address.isEmpty() || address.isBlank()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Customer Address is blank");
                alert.setContentText("The customer address field is blank. \nPlease enter a valid address");
                alert.showAndWait();
                return;
            }
            String postal_Code = postalCode.getText();
            if(postal_Code.isEmpty() || postal_Code.isBlank()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Customer Postal Code is blank");
                alert.setContentText("The customer postal code field is blank. \nPlease enter a valid postal code");
                alert.showAndWait();
                return;
            }
            String phone =phoneNumber.getText();
            if(name.isEmpty() || name.isBlank()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error phone number");
                alert.setContentText("Please enter a valid phone number");
                alert.showAndWait();
                return;
            }

            int division_ID = Division.getValue().getDivisionId();
            int country_ID = Country.getValue().getCountryId();
            String lastUpdatedBy = "script";
            Timestamp lastUpdated = Timestamp.valueOf(now());
            CustomerQuery.updateCustomer(customer_ID, name, address, postal_Code, phone, lastUpdatedBy, division_ID, country_ID, lastUpdated);

            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("./view/CustomerScreen.fxml")));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (NumberFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onCancel(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("./view/CustomerScreen.fxml")));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void onDivision(ActionEvent actionEvent) {
    }

    public void onCountry(ActionEvent actionEvent) {
        model.Country c = Country.getValue();
        try{
            Division.setItems(DivisionQuery.displayDivision(c.getCountryId()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Country.setItems(CountryQuery.getAllCountry());
    }
}
