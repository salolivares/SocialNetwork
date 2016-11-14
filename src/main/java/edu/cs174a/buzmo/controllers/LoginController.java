package edu.cs174a.buzmo.controllers;


import edu.cs174a.buzmo.util.ProgressSpinner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import edu.cs174a.buzmo.MainApp;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

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
    }

    private void handleLoginButtonAction(ActionEvent action) {
        System.out.println("Login Button Pressed!");
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();
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
