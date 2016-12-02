package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class SummaryReportController {
    private MainApp mainApp;

    @FXML private Button backButton;
    @FXML private TextField newMessageField;
    @FXML private TextField totalMessageReads;
    @FXML private TextField avgNewMessageReadsField;
    @FXML private TextField avgNumReadMessages;
    @FXML private TextField numUsersLess3Field;
    @FXML private ListView<String> topicList;
    @FXML private ListView<String> top3UsersList;

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
