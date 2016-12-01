package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HomeController {
    private MainApp mainApp;
    private String email;

    @FXML private Label loggedinLabel;
    @FXML private Label isManagerLabel;
    @FXML private Button logoutButton;
    @FXML private Button topicWordButton;
    @FXML private Button searchButton;
    @FXML private Button myCircleButton;
    @FXML private Button chatGroupButton;

    public HomeController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        topicWordButton.setOnAction(this::handleTopicButtonAction);
        logoutButton.setOnAction(this::handleLogoutButtonAction);
        searchButton.setOnAction(this::handleSearchButtonAction);
        myCircleButton.setOnAction(this::handleMyCircleButton);
        chatGroupButton.setOnAction(this::handleChatGroupButton);
    }

    private void handleChatGroupButton(ActionEvent actionEvent) {
        mainApp.getGUIManager().showChatGroupsLayout();
    }

    private void handleMyCircleButton(ActionEvent actionEvent) {
        mainApp.getGUIManager().showMyCircleMenuLayout();
    }

    private void handleTopicButtonAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showTopicWordsLayout();
    }

    private void handleLogoutButtonAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().logout();
    }

    private void handleSearchButtonAction(ActionEvent actionEvent) { mainApp.getGUIManager().showSearchUsersLayout();}

    /**
     * Is called by the main application to give a reference back to itself.
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setEmail(String email) {
        this.email = email;
        loggedinLabel.setText("Logged in: " + email);
    }
}
