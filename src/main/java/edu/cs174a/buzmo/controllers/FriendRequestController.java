package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import edu.cs174a.buzmo.tasks.FetchFriendRequestsTask;
import edu.cs174a.buzmo.tasks.FetchTopicWordsTask;
import edu.cs174a.buzmo.util.ProgressSpinner;
import edu.cs174a.buzmo.util.TopicWord;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * Created by jordannguyen on 11/30/16.
 */
public class FriendRequestController {
    private MainApp mainApp;
    @FXML private Button acceptButton;
    @FXML private Button rejectButton;
    @FXML private ListView<String> requestsListView;
    @FXML private Button backButton;
    @FXML private Button refreshButton;

    public FriendRequestController() {

    }

    @FXML
    private void initialize() {
        refreshButton.setOnAction(this::handleRefreshAction);
    }

    private void handleRefreshAction(ActionEvent actionEvent) {
        System.out.println("Refreshing...");
        populateRequestList();
        System.out.println("Populated!");
    }

    private void populateRequestList() {
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();

        final FetchFriendRequestsTask fetchFriendRequests = new FetchFriendRequestsTask(mainApp.getGUIManager().getEmail());

        fetchFriendRequests.setOnSucceeded(t -> {
            Platform.runLater(ps::stopSpinner);
            requestsListView.setItems(fetchFriendRequests.getValue());
        });

        fetchFriendRequests.setOnFailed(t -> {
            Platform.runLater(ps::stopSpinner);
            System.out.println("FAILED");
        });

        mainApp.getDatabaseExecutor().submit(fetchFriendRequests);
    }

    public void refreshRequestList(){
        refreshButton.fire();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
