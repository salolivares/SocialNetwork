package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import edu.cs174a.buzmo.tasks.CreateTopicTask;
import edu.cs174a.buzmo.tasks.FetchFriendsTask;
import edu.cs174a.buzmo.util.ProgressSpinner;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * Created by jordannguyen on 11/30/16.
 */
public class MyCircleMessageController {
    private MainApp mainApp;
    @FXML private Button sendButton;
    @FXML private Button deleteButton;
    @FXML private Button backButton;
    @FXML private Button refreshButton;
    @FXML private ListView<String> topicListView;
    @FXML private ListView<?> messageList;
    @FXML private ChoiceBox<String> publicChoiceBox;
    @FXML private Button addTopicButton;
    @FXML private TextField topicTextField;
    @FXML private TextField messageField;

    public MyCircleMessageController() {

    }

    @FXML
    private void initialize() {
        backButton.setOnAction(this::handleBackAction);
        refreshButton.setOnAction(this::handleRefreshAction);
        addTopicButton.setOnAction(this::handleAddTopicAction);
    }

    private void handleBackAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showMyCircleMenuLayout();
    }

    private void handleRefreshAction(ActionEvent actionEvent) {
        System.out.println("Refreshing...");
        //populateMessages();
        System.out.println("Populated!");
    }

    private void handleAddTopicAction(ActionEvent actionEvent) {
        if (topicTextField.getText() != "" && !topicListView.getItems().contains(topicTextField.getText())) {
            ObservableList<String> newList = topicListView.getItems();
            newList.add(topicTextField.getText());
            topicListView.setItems(newList);

            final CreateTopicTask createTopic = new CreateTopicTask(topicTextField.getText());
            mainApp.getDatabaseExecutor().submit(createTopic);
        }
    }

/*
    private void populateMessages() {
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();

        final FetchMessagesTask fetchMessages = new FetchMessages(mainApp.getGUIManager().getEmail());

        fetchMessages.setOnSucceeded(t -> {
            Platform.runLater(ps::stopSpinner);
            friendsList.setItems(fetchMessages.getValue());
        });

        fetchMessages.setOnFailed(t -> {
            Platform.runLater(ps::stopSpinner);
            System.out.println("FAILED");
        });

        mainApp.getDatabaseExecutor().submit(fetchMessages);
    }
*/

    public void refreshMessages(){
        refreshButton.fire();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
