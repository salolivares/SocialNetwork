package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Created by jordannguyen on 11/30/16.
 */
public class SearchMessagesController {
    private MainApp mainApp;
    @FXML private Button backButton;
    @FXML private Button addTopicButton;
    @FXML private Button searchButton;


    @FXML private void initialize() {
        backButton.setOnAction(this::handleBackAction);
    }

    private void handleBackAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showMyCircleMenuLayout();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
