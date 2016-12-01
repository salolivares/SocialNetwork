package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import edu.cs174a.buzmo.tasks.CreateTopicTask;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * Created by jordannguyen on 11/30/16.
 */
public class SearchMessagesController {
    private MainApp mainApp;
    @FXML private Button backButton;
    @FXML private Button addTopicButton;
    @FXML private Button searchButton;
    @FXML private ListView<String> topicsList;
    @FXML private TextField topicsTextField;

    @FXML private void initialize() {
        backButton.setOnAction(this::handleBackAction);
        addTopicButton.setOnAction(this::handleAddTopicAction);
    }

    private void handleBackAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showMyCircleMenuLayout();
    }

    private void handleAddTopicAction(ActionEvent actionEvent) {
        if (!topicsTextField.equals("") && !topicsList.getItems().contains(topicsTextField.getText())) {
            ObservableList<String> newList = topicsList.getItems();
            newList.add(topicsTextField.getText());
            topicsList.setItems(newList);
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
