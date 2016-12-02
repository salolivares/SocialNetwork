package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SummaryReportController {
    private MainApp mainApp;

    @FXML private Button backButton;

    @FXML
    private void initialize() {
        backButton.setOnAction(this::backButtonAction);
    }

    private void backButtonAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showHomeLayout();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
