package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import edu.cs174a.buzmo.tasks.*;
import edu.cs174a.buzmo.util.ProgressSpinner;
import edu.cs174a.buzmo.util.TopicWord;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import javax.swing.*;

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
        backButton.setOnAction(this::handleBackAction);
        refreshButton.setOnAction(this::handleRefreshAction);
        acceptButton.setOnAction(this::handleAcceptAction);
        rejectButton.setOnAction(this::handleRejectAction);
    }

    private void handleBackAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showMyCircleMenuLayout();
    }

    private void handleRefreshAction(ActionEvent actionEvent) {
        System.out.println("Refreshing...");
        populateRequestList();
        System.out.println("Populated!");
    }

    private void handleAcceptAction(ActionEvent actionEvent) {
        String email = requestsListView.getSelectionModel().getSelectedItem();
        if(email != null){
            acceptFriend(email);
        }
    }

    private void acceptFriend(String email) {
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();

        final AcceptFriendTask acceptFriend = new AcceptFriendTask(mainApp.getGUIManager().getEmail(), email);

        acceptFriend.setOnSucceeded(t -> {
            Platform.runLater(() -> {
                ps.stopSpinner();
                refreshButton.fire();
            });
        });

        mainApp.getDatabaseExecutor().submit(acceptFriend);
    }

    private void handleRejectAction(ActionEvent actionEvent) {
        String email = requestsListView.getSelectionModel().getSelectedItem();
        if(email != null){
            rejectFriend(email);
        }
    }

    private void rejectFriend(String email) {
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();

        final RejectFriendTask rejectFriend = new RejectFriendTask(email, mainApp.getGUIManager().getEmail());

        rejectFriend.setOnSucceeded(t -> {
            Platform.runLater(() -> {
                ps.stopSpinner();
                refreshButton.fire();
            });
        });

        mainApp.getDatabaseExecutor().submit(rejectFriend);
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
