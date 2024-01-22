package main;

import helper.JDBC;
import helper.Util;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * The main class of the Scheduling Application, extending JavaFX Application.
 * This initializes the database connection, sets the application timezone,
 * launches the first scene (Login Screen) and managing the application's lifecycle.
 *
 * @author Daniel Akoko
 */
public class Main extends Application {

    /**
     * The main entry point for the Scheduling Application.
     *
     * @param stage The primary stage for the application, onto which the application scene can be set.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @Override public void start(Stage stage) throws IOException {
        //Load the main FXML view
        Parent parent = FXMLLoader.load(getClass().getResource("../view/LoginScreen.fxml"));
        Scene scene = new Scene(parent, 700, 500);
        stage.setScene(scene);
        stage.setTitle("Scheduling Application");
        stage.show();
    }

    /**
     * The main method that launches the JavaFX application.
     * It initializes the database connection, converts the current time to the system time zone,
     * and then launches the JavaFX application.
     *
     * @param args Command-line arguments passed to the application. (not used in this application)
     */
    public static void main(String[] args) {
        // Open the JDBC database connection
        JDBC.openConnection();

        // Create a Util object and convert the current time to the system time zone
        Util obj = new Util();
        obj.convertToSystemTimeZone(LocalDateTime.now());

        // Launch the JavaFX application
        launch(args);

        // Close the JDBC database connection when the application exits
        JDBC.closeConnection();
    }
}