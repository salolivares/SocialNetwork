package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import edu.cs174a.buzmo.tasks.FetchFriendsTask;
import edu.cs174a.buzmo.tasks.FetchPrivateMessageTask;
import edu.cs174a.buzmo.util.ProgressSpinner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class MyCircleController {
    private MainApp mainApp;
    @FXML private Button viewRequestsButton;
    @FXML private Button backButton;
    @FXML private Button viewMessagesButton;
    @FXML private Button searchMessagesButton;
    @FXML private ListView<String> friendsList;

    @FXML private void initialize() {
        viewRequestsButton.setOnAction(this::handleViewRequestsAction);
        backButton.setOnAction(this::handleBackAction);
        viewMessagesButton.setOnAction(this::handleMessagesAction);
        searchMessagesButton.setOnAction(this::handleSearchAction);
    }

    private void handleBackAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showHomeLayout();
    }

    private void handleViewRequestsAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showFriendRequestLayout();
    }

    private void handleMessagesAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showMyCircleMessageLayout();
    }

    private void handleSearchAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showSearchMessagesLayout();
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

    public void refreshFriendsList() {
        populateFriendList();
    }


    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

}
