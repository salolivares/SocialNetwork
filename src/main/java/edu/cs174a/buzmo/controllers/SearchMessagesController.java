package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import edu.cs174a.buzmo.tasks.CreateTopicTask;
import edu.cs174a.buzmo.tasks.FetchFriendsTask;
import edu.cs174a.buzmo.tasks.SearchMessagesTask;
import edu.cs174a.buzmo.util.Message;
import edu.cs174a.buzmo.util.ProgressSpinner;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
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
    @FXML private ListView<String> topicListView;
    @FXML private ListView<Message> messagesList;
    @FXML private TextField topicTextField;

    @FXML private void initialize() {
        backButton.setOnAction(this::handleBackAction);
        addTopicButton.setOnAction(this::handleAddTopicAction);
        searchButton.setOnAction(this::handleSearchAction);
        messagesList.setCellFactory(param -> new ListCell<Message>() {
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
        mainApp.getGUIManager().showHomeLayout();
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

    private void handleSearchAction(ActionEvent actionEvent) {
        if (!topicListView.getItems().isEmpty()) {

            ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
            ps.startSpinner();

            final SearchMessagesTask search = new SearchMessagesTask(topicListView.getItems());

            search.setOnSucceeded(t -> {
                Platform.runLater(ps::stopSpinner);
                messagesList.setItems(search.getValue());
            });

            search.setOnFailed(t -> {
                Platform.runLater(ps::stopSpinner);
                System.out.println("FAILED");
            });
            mainApp.getDatabaseExecutor().submit(search);
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
