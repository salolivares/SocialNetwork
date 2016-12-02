package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import edu.cs174a.buzmo.tasks.*;
import edu.cs174a.buzmo.util.Message;
import edu.cs174a.buzmo.util.ProgressSpinner;
import edu.cs174a.buzmo.util.TopicWord;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by jordannguyen on 11/30/16.
 */
public class PrivateMessageController {
    private MainApp mainApp;
    @FXML private Button sendButton;
    @FXML private Button deleteMessage;
    @FXML private Button backButton;
    @FXML private Button refreshButton;
    @FXML private ListView<String> friendsList;
    @FXML private ListView<Message> messageList;
    @FXML private TextField messageTextField;

    public PrivateMessageController() {

    }

    @FXML
    private void initialize() {
        backButton.setOnAction(this::handleBackAction);
        refreshButton.setOnAction(this::handleRefreshAction);
        sendButton.setOnAction(this::handleSendAction);
        deleteMessage.setOnAction(this::handleDeleteAction);
        messageList.setCellFactory(param -> new ListCell<Message>() {
            @Override
            protected void updateItem(Message item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getBody() == null) {
                    setText(null);
                } else {
                    setText("[" + item.getTimestamp().substring(5,19) + "] " + item.getSender() + ": " + item.getBody());
                }
            }
        });
    }

    private void handleBackAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showHomeLayout();
    }

    private void handleRefreshAction(ActionEvent actionEvent) {
        System.out.println("Refreshing...");
        populateFriendList();
        populateMessages();
        System.out.println("Populated!");
    }

    private void handleSendAction(ActionEvent actionEvent) {
        sendPrivateMessage();
        populateMessages();
    }

    private void handleDeleteAction(ActionEvent actionEvent) {
        deleteMessage();
        populateMessages();
    }

    private void populateMessages() {

        String friend = friendsList.getSelectionModel().getSelectedItem();
        if (friend != null) {

            ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
            ps.startSpinner();

            final FetchPrivateMessageTask fetchPM = new FetchPrivateMessageTask(mainApp.getGUIManager().getEmail(), friend);

            fetchPM.setOnSucceeded(t -> {
                Platform.runLater(ps::stopSpinner);
                messageList.setItems(null);
                messageList.setItems(fetchPM.getValue());
            });

            fetchPM.setOnFailed(t -> {
                Platform.runLater(ps::stopSpinner);
                System.out.println("FAILED");
            });

            mainApp.getDatabaseExecutor().submit(fetchPM);
        }
    }

    private void sendPrivateMessage() {

        String friend = friendsList.getSelectionModel().getSelectedItem();
        if (friend != null && messageTextField.getText() != null) {

            ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
            ps.startSpinner();


            Duration timeElapsed = Duration.between(mainApp.getStartTime(), Instant.now());
            LocalTime time = mainApp.getGlobalTime().plusMinutes(timeElapsed.toMinutes());
            LocalDate date = mainApp.getGlobalDate();

            String timestamp = date.toString() + " " + time.toString();

            final SendPrivateMessageTask fetchPM = new SendPrivateMessageTask(mainApp.getGUIManager().getEmail(), friend,
                                                        messageTextField.getText(), timestamp, 1);

            fetchPM.setOnSucceeded(t -> {
                Platform.runLater(ps::stopSpinner);
            });

            fetchPM.setOnFailed(t -> {
                Platform.runLater(ps::stopSpinner);
                System.out.println("FAILED");
            });
            messageTextField.clear();
            mainApp.getDatabaseExecutor().submit(fetchPM);
        }
    }

    private void deleteMessage() {

        Message msg = messageList.getSelectionModel().getSelectedItem();
        if (msg != null) {

            ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
            ps.startSpinner();

            final DeletePrivateMessageTask deletePM = new DeletePrivateMessageTask(mainApp.getGUIManager().getEmail(), msg.getSender(), msg.getMid());

            deletePM.setOnSucceeded(t -> {
                Platform.runLater(ps::stopSpinner);
            });

            deletePM.setOnFailed(t -> {
                Platform.runLater(ps::stopSpinner);
                System.out.println("FAILED");
            });

            mainApp.getDatabaseExecutor().submit(deletePM);
        }
    }

    private void populateFriendList() {
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();

        final FetchFriendsTask fetchFriends = new FetchFriendsTask(mainApp.getGUIManager().getEmail());

        fetchFriends.setOnSucceeded(t -> {
            Platform.runLater(ps::stopSpinner);
            friendsList.setItems(fetchFriends.getValue());
        });

        fetchFriends.setOnFailed(t -> {
            Platform.runLater(ps::stopSpinner);
            System.out.println("FAILED");
        });

        mainApp.getDatabaseExecutor().submit(fetchFriends);
    }

    public void refreshFriendsList(){
        refreshButton.fire();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
