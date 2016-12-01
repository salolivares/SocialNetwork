package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class MyCircleController {
    private MainApp mainApp;
    @FXML private Button viewRequestsButton;
    @FXML private Button backButton;
    @FXML private Button viewMessagesButton;
    @FXML private Button searchMessagesButton;


    @FXML private void initialize() {
        viewRequestsButton.setOnAction(this::handleViewRequestsAction);
        backButton.setOnAction(this::handleBackAction);
        viewMessagesButton.setOnAction(this::handleMessagesAction);
        searchMessagesButton.setOnAction(this::handleSearchAction);
    }

    private void handleBackAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showHomeLayout();
    }

    private void handleViewRequestsAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showFriendRequestLayout();
    }

    private void handleMessagesAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showMyCircleMessageLayout();
    }

    private void handleSearchAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showSearchMessagesLayout();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

}
