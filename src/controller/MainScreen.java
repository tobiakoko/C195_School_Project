package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainScreen {
    @FXML private Button exitButton;
    @FXML private Button customerButton;
    @FXML private Button appointmentButton;
    @FXML private Button reportButton;

    @FXML void customerButton(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/CustomerScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML void appointmentButton(ActionEvent actionEvent) throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("../view/AppointmentScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML void reportButton(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/ReportScreen.fxml"));
        Scene scene = new Scene(parent);
        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    @FXML void exitButton(ActionEvent ExitButton) {
        Stage stage = (Stage) ((Node) ExitButton.getSource()).getScene().getWindow();
        stage.close();
    }
}
