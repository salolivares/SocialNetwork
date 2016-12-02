package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import edu.cs174a.buzmo.tasks.*;
import edu.cs174a.buzmo.util.Message;
import edu.cs174a.buzmo.util.ProgressSpinner;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

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
        sendButton.setOnAction(this::handleSendAction);
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
        publicChoiceBox.setItems(FXCollections.observableArrayList(
                "Public Message", "Private Message"
        ));
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

    private void handleSendAction(ActionEvent actionEvent) {
        sendMessage();
        populateMessages();
    }

    private void sendMessage() {
        String publicChoice = publicChoiceBox.getSelectionModel().getSelectedItem();

        if (messageField.getText() != null && publicChoice != null && !topicListView.getItems().isEmpty()) {

            int pub = 0;
            if (publicChoice.equals("Public Message")) pub = 1;
            else pub = 0;

            ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
            ps.startSpinner();


            Duration timeElapsed = Duration.between(mainApp.getStartTime(), Instant.now());
            LocalTime time = mainApp.getGlobalTime().plusMinutes(timeElapsed.toMinutes());
            LocalDate date = mainApp.getGlobalDate();

            String timestamp = date.toString() + " " + time.toString();

            final SendMessageTask send = new SendMessageTask(mainApp.getGUIManager().getEmail(),
                                            messageField.getText(), timestamp, 1, pub, topicListView.getItems());

            send.setOnSucceeded(t -> {
                Platform.runLater(ps::stopSpinner);
            });

            send.setOnFailed(t -> {
                Platform.runLater(ps::stopSpinner);
                System.out.println("FAILED");
            });
            messageField.clear();
            mainApp.getDatabaseExecutor().submit(send);
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
