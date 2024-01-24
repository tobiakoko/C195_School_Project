package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Controller class for the Main Screen, handling various button actions.
 * The MainScreen class manages the user interface and interaction for the main screen of the application.
 * It presents buttons for navigating to different sections (customers, appointments, reports) and
 * provides an "Exit" button to close the program.
 *
 * @author Daniel Akoko
 */
public class MainScreen{

    /**
     * Event handler for the "Customer" button click.
     * Loads the CustomerScreen.fxml view when clicked, enabling access to customer management features.
     *
     * @param actionEvent The event triggering the action.
     * @throws IOException If there is an error loading the scene.
     */
    @FXML void customerButton(ActionEvent actionEvent) throws IOException {
        loadScene("../view/CustomerScreen.fxml", actionEvent);
    }

    /**
     * Event handler for the "Appointment" button click.
     * Loads the AppointmentScreen.fxml view when clicked, enabling access to appointment management features.
     *
     * @param actionEvent The event triggering the action.
     * @throws IOException If there is an error loading the scene.
     */
    @FXML void appointmentButton(ActionEvent actionEvent) throws IOException {
        loadScene("../view/AppointmentScreen.fxml", actionEvent);
    }

    /**
     * Event handler for the "Report" button click.
     * Loads the ReportScreen.fxml view when clicked, enabling access to report generation functionalities.
     *
     * @param actionEvent The event triggering the action.
     * @throws IOException If there is an error loading the scene.
     */
    @FXML void reportButton(ActionEvent actionEvent) throws IOException {
        loadScene("../view/ReportScreen.fxml", actionEvent);
    }

    /**
     * Event handler for the "Exit" button click.
     * Closes the entire application when clicked.
     *
     * @param actionEvent The event triggering the action.
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
