package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import edu.cs174a.buzmo.tasks.CreateTopicTask;
import edu.cs174a.buzmo.tasks.FetchFriendsTask;
import edu.cs174a.buzmo.tasks.FetchMyCircleMessagesTask;
import edu.cs174a.buzmo.util.Message;
import edu.cs174a.buzmo.util.ProgressSpinner;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    @FXML private ListView<Message> messageList;
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
        messageList.setCellFactory(param -> new ListCell<Message>() {
            @Override
            protected void updateItem(Message item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getBody() == null) {
                    setText(null);
                } else {
                    setText("[" + item.getTimestamp().substring(5, 19) + "] " + item.getSender() + ": " + item.getBody());
                }
            }
        });
    }

    private void handleBackAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showMyCircleMenuLayout();
    }

    private void handleRefreshAction(ActionEvent actionEvent) {
        System.out.println("Refreshing...");
        populateMessages();
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

    private void populateMessages() {
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();

        final FetchMyCircleMessagesTask fetchMessages = new FetchMyCircleMessagesTask(mainApp.getGUIManager().getEmail());

        fetchMessages.setOnSucceeded(t -> {
            Platform.runLater(ps::stopSpinner);
            messageList.setItems(fetchMessages.getValue());
        });

        fetchMessages.setOnFailed(t -> {
            Platform.runLater(ps::stopSpinner);
            System.out.println("FAILED");
        });

        mainApp.getDatabaseExecutor().submit(fetchMessages);
    }

    public void refreshMessages(){
        refreshButton.fire();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
