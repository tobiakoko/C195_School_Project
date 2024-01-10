package controller;

import helper.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

public class Login implements Initializable {
    public TextField usernameTextField;
    public PasswordField passwordTextField;
    public Button cancelButton;
    public Button loginButton;
    public Label zoneId;

    public void cancelButtonAction(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public void loginButtonAction(ActionEvent actionEvent) throws IOException {

        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        boolean checkedUser = JDBC.validateLogin(username, password);
        if(checkedUser){
            Parent parent = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("errorTitle");
            alert.setHeaderText("errorHeader");
            alert.setContentText("errorMessage");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Locale locale = Locale.getDefault();
        resourceBundle = ResourceBundle.getBundle("helper/Login", locale);
        // Set location label
        zoneId.setText(ZoneId.systemDefault().getId());

        // Add event listener to login button
        loginButton.setOnAction(event -> {
            // Get username and password
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();

            // Validate username and password
            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please enter a username and password.");
                return;
            }

            // Check if login is successful
            if (loginSuccessful(username, password)) {
                // Display the main application window
                // ...
            } else {
                errorLabel.setText("Invalid username or password.");
            }
        });
    }
}
