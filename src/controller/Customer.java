package controller;

import database.CustomerQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Customer implements Initializable {
    @FXML private TableView<model.Customer> customerTable;
    @FXML private TableColumn<model.Customer, Integer> customerID;
    @FXML private TableColumn<model.Customer, String> Name;
    @FXML private TableColumn<model.Customer, String> Address;
    @FXML private TableColumn<model.Customer, String> postalCode;
    @FXML private TableColumn<model.Customer, String> phoneNumber;
    @FXML private TableColumn<model.Customer, Integer> divisionID;
    @FXML private Button addCustomer;
    @FXML private Button modifyCustomer;
    @FXML private Button deleteCustomer;

    public void onAddCustomer(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/AddCustomer.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void onModifyCustomer(ActionEvent actionEvent) {
    }

    public void onDeleteCustomer(ActionEvent actionEvent) {
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
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        Name.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        Address.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        postalCode.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        divisionID.setCellValueFactory(new PropertyValueFactory<>("customerDivisionID"));
    }
}
