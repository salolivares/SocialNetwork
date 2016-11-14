package edu.cs174a.buzmo.controllers;


import edu.cs174a.buzmo.util.ProgressSpinner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import edu.cs174a.buzmo.MainApp;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class LoginController {
    @FXML private Button loginButton;
    @FXML private Button closeButton;
    @FXML private TextField emailTextField;
    @FXML private PasswordField passwordField;

    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public LoginController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        // Define event handling
        loginButton.setOnAction(this::handleLoginButtonAction);
        closeButton.setOnAction(this::handleCloseButtonAction);
        passwordField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER){
                loginButton.fire();
            }
        });
    }

    private void handleLoginButtonAction(ActionEvent action) {
        System.out.println("Login Button Pressed!");
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();

        Thread login = new Thread(() -> {
            System.out.println("Logging in.");
            try {
                // Simulate long server call!
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Update UI and stop spinner
            Platform.runLater(() -> {
                ps.stopSpinner();
            });

        });

        login.start();

    }

    private void handleCloseButtonAction(ActionEvent action) {
        Platform.exit();
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
