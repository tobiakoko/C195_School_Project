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

    @FXML void customerButton(ActionEvent actionEvent) throws IOException {
        loadScene("../view/CustomerScreen.fxml", actionEvent);
    }


    @FXML void appointmentButton(ActionEvent actionEvent) throws IOException {
        loadScene("../view/AppointmentScreen.fxml", actionEvent);
    }

    @FXML void reportButton(ActionEvent actionEvent) throws IOException {
        loadScene("../view/ReportScreen.fxml", actionEvent);
    }

    /**
     * Event handler for the "Exit" button click.
     *
     * @param actionEvent
     */
    @FXML void exitButton(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }


    /**
     * Load a new scene with the given FXML file path.
     *
     * @param fxmlPath    The FXML file path.
     * @param actionEvent The ActionEvent associated with the event.
     * @throws IOException If there is an error loading the scene.
     */
    private void loadScene(String fxmlPath, ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
