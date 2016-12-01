package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class MyCircleController {
    private MainApp mainApp;
    @FXML private Button viewRequestsButton;


    @FXML private void initialize() {
        viewRequestsButton.setOnAction(this::handleViewRequestsAction);
    }

    private void handleViewRequestsAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showFriendRequestLayout();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

}
