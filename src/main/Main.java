package main;

import helper.JDBC;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class for the Scheduling Application.
 *
 */
public class Main extends Application {

    /**
     * The entry point for the JavaFX application.
     *
     * @param stage The primary stage for the application.
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
     * Main method to run the Scheduling Application.
     *
     * <p><b>
     * FUTURE ENHANCEMENT:
     * </b></p>
     * <p><b>
     * RUNTIME ERROR:
     * </b></p>
     * @author Daniel Akoko
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}