package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import edu.cs174a.buzmo.tasks.FetchChatGroupsTask;
import edu.cs174a.buzmo.tasks.FetchFriendsTask;
import edu.cs174a.buzmo.tasks.InviteUserToChatGroupTask;
import edu.cs174a.buzmo.util.ChatGroup;
import edu.cs174a.buzmo.util.ProgressSpinner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;

//Condor is here...

/**
 * Created by jordannguyen on 11/30/16.
 */
public class ChatGroupInviteController {
    private MainApp mainApp;
    private ChatGroup chatGroup;
    @FXML private Button sendInviteButton;
    @FXML private Button backButton;
    @FXML private ListView<String> friendsListView;
    @FXML private Button refreshButton;
    @FXML private Label titleLabel;


    public ChatGroupInviteController() {

    }

    @FXML
    private void initialize() {
        backButton.setOnAction(this::handleBackAction);
        refreshButton.setOnAction(this::handleRefreshAction);
        sendInviteButton.setOnAction(this::handleInviteAction);
    }

    private void handleBackAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showChatGroupsLayout();
    }

    private void handleRefreshAction(ActionEvent actionEvent) {
        System.out.println("Refreshing...");
        populateFriendList();
        System.out.println("Populated!");
    }

    private void handleInviteAction(ActionEvent actionEvent) {
        String inviteeEmail = friendsListView.getSelectionModel().getSelectedItem();
        if (inviteeEmail != null) {
            final InviteUserToChatGroupTask invite = new InviteUserToChatGroupTask(chatGroup.getGroupName(), inviteeEmail);
            mainApp.getDatabaseExecutor().submit(invite);
        }
    }

    private void populateFriendList() {
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();

        final FetchFriendsTask fetchFriends = new FetchFriendsTask(mainApp.getGUIManager().getEmail());

        fetchFriends.setOnSucceeded(t -> {
            Platform.runLater(ps::stopSpinner);
            friendsListView.setItems(fetchFriends.getValue());
        });

        fetchFriends.setOnFailed(t -> {
            Platform.runLater(ps::stopSpinner);
            System.out.println("FAILED");
        });

        mainApp.getDatabaseExecutor().submit(fetchFriends);
    }

    public void setChatGroup(ChatGroup cg) { this.chatGroup = cg; }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void updateLabel() {this.titleLabel.setText("Invite friend to ChatGroup: " + this.chatGroup.getGroupName());}

    public void refreshFriendsList(){
        refreshButton.fire();
    }
}
