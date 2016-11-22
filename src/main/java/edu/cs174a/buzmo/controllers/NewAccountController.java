package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import edu.cs174a.buzmo.tasks.CreateAccountTask;
import edu.cs174a.buzmo.util.ProgressSpinner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class NewAccountController {
    @FXML private Button submitButton;
    @FXML private Button cancelButton;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField phoneField;
    @FXML private TextField screennameField;
    @FXML private Label errorLabel;

    // Reference to the main application.
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public NewAccountController() {
    }

    private void handleCancelButtonAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showLoginScreen();
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        submitButton.setOnAction(this::handleSubmitButtonAction);
        cancelButton.setOnAction(this::handleCancelButtonAction);
    }

    private void handleSubmitButtonAction(ActionEvent actionEvent) {
        if(validateFields()){
            createAccount();
        }
    }

    private void createAccount() {
        // SQL statement here to create account
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();

        final CreateAccountTask createAccountTask = new CreateAccountTask(nameField.getText(), emailField.getText(),
                passwordField.getText(), phoneField.getText(), screennameField.getText());

        createAccountTask.setOnSucceeded(t -> {
            Platform.runLater(()->{
                ps.stopSpinner();
                mainApp.getGUIManager().showLoginScreen();
                System.out.println("Account created!");
            });
        });

        System.out.println("Creating account...");
        mainApp.getDatabaseExecutor().submit(createAccountTask);
    }

    private boolean validateFields() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String phone = phoneField.getText();

        if(name.isEmpty() || name.length() > 20){
            errorLabel.setText("Name invalid");
            errorLabel.setVisible(true);
            return false;
        }

        if(email.isEmpty() || email.length() > 20 || email.contains(" ") ){
            errorLabel.setText("Email invalid");
            errorLabel.setVisible(true);
            return false;
        }

        if(password.isEmpty() || password.length() > 10 || password.length() < 2){
            errorLabel.setText("Password invalid");
            errorLabel.setVisible(true);
            return false;
        }

        if(phone.isEmpty() || !phone.matches("[0-9]+") || phone.length() != 10){
            errorLabel.setText("Phone Invalid");
            errorLabel.setVisible(true);
            return false;
        }

        errorLabel.setVisible(false);
        return true;
    }

    /**
     * Is called by the main application to give a reference back to itself.
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
