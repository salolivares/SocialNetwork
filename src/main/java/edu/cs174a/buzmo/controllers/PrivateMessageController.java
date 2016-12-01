package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import edu.cs174a.buzmo.tasks.AcceptFriendTask;
import edu.cs174a.buzmo.tasks.FetchFriendRequestsTask;
import edu.cs174a.buzmo.tasks.FetchFriendsTask;
import edu.cs174a.buzmo.tasks.RejectFriendTask;
import edu.cs174a.buzmo.util.ProgressSpinner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

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
    @FXML private ListView<?> messageList;

    public PrivateMessageController() {

    }

    @FXML
    private void initialize() {
        backButton.setOnAction(this::handleBackAction);
        refreshButton.setOnAction(this::handleRefreshAction);
    }

    private void handleBackAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showHomeLayout();
    }

    private void handleRefreshAction(ActionEvent actionEvent) {
        System.out.println("Refreshing...");
        populateFriendList();
        System.out.println("Populated!");
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
