package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.tasks.FetchLoginCredentialsTask;
import edu.cs174a.buzmo.util.ProgressSpinner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import edu.cs174a.buzmo.MainApp;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.sql.SQLException;

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
        try {
            login();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void login() throws SQLException, ClassNotFoundException {
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();

        final FetchLoginCredentialsTask fetchLoginCredentialsTask = new FetchLoginCredentialsTask(emailTextField.getText(), passwordField.getText());

        fetchLoginCredentialsTask.setOnSucceeded(t -> {
            Platform.runLater(ps::stopSpinner);

            if(fetchLoginCredentialsTask.getValue()){
                Platform.runLater(()->{
                    mainApp.getLoginManager().authenticated(emailTextField.getText());
                });
            } else {
                System.out.println("Authentication Failed");
            }
        });

        mainApp.getDatabaseExecutor().submit(fetchLoginCredentialsTask);
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
