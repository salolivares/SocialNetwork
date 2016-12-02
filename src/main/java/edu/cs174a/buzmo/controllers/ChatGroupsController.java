package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import edu.cs174a.buzmo.tasks.*;
import edu.cs174a.buzmo.util.ChatGroup;
import edu.cs174a.buzmo.util.Message;
import edu.cs174a.buzmo.util.ProgressSpinner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Optional;


public class ChatGroupsController {
    private MainApp mainApp;

    @FXML private Button backButton;
    @FXML private Button chatSettingsButton;
    @FXML private Button createButton;
    @FXML private Button refreshButton;
    @FXML private Button acceptButton;
    @FXML private Button inviteButton;
    @FXML private ListView<ChatGroup> chatGroupList;
    @FXML private ListView<Message> messageList;

    @FXML private void initialize() {
        backButton.setOnAction(this::handleBackAction);
        chatSettingsButton.setOnAction(this::handleChatSettingsAction);
        createButton.setOnAction(this::handleCreateButton);
        refreshButton.setOnAction(this::handleRefreshAction);
        acceptButton.setOnAction(this::handleAcceptAction);
        inviteButton.setOnAction(this::handleInviteAction);
        chatGroupList.setCellFactory(param -> new ListCell<ChatGroup>() {
            @Override
            protected void updateItem(ChatGroup item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getGroupName() == null) {
                    setText(null);
                } else if (item.getMemberStatus() == 0){
                    setText(item.getGroupName() + " (INVITE RECIEVED FROM THIS GROUP)");
                } else{
                    setText(item.getGroupName());
                }
            }
        });
        chatGroupList.setOnMouseClicked(this::handleChatGroupClick);
    }

    private void handleChatGroupClick(MouseEvent mouseEvent) {
        changeMessageList(chatGroupList.getSelectionModel().getSelectedItem());
    }

    private void changeMessageList(ChatGroup selectedItem) {
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();

        final FetchChatGroupMessageTask fetchChatGroupMessageTask = new FetchChatGroupMessageTask(selectedItem.getGroupName());

        fetchChatGroupMessageTask.setOnSucceeded(t->{
            Platform.runLater(ps::stopSpinner);
            messageList.setItems(fetchChatGroupMessageTask.getValue());
        });

        fetchChatGroupMessageTask.setOnFailed(t->{
            Platform.runLater(ps::stopSpinner);
            System.out.println("FAILED");
        });

        mainApp.getDatabaseExecutor().submit(fetchChatGroupMessageTask);
    }


    private void handleInviteAction(ActionEvent actionEvent) {
        ChatGroup chatGroup = chatGroupList.getSelectionModel().getSelectedItem();
        if(chatGroup != null) {
            if (chatGroup.getMemberStatus() == 1) {
                mainApp.getGUIManager().showChatGroupInviteLayout(chatGroup);
            }
        }

    }

    private void handleRefreshAction(ActionEvent actionEvent) {
        populateLists();
    }

    private void populateLists() {
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();

        final FetchChatGroupsTask fetchChatGroupsTask = new FetchChatGroupsTask(mainApp.getGUIManager().getEmail());

        fetchChatGroupsTask.setOnSucceeded(t -> {
            Platform.runLater(ps::stopSpinner);
            chatGroupList.setItems(fetchChatGroupsTask.getValue());
        });

        mainApp.getDatabaseExecutor().submit(fetchChatGroupsTask);
    }

    private void handleAcceptAction(ActionEvent actionEvent) {
        ChatGroup chatGroup = chatGroupList.getSelectionModel().getSelectedItem();
        if(chatGroup != null) {
            if (chatGroup.getMemberStatus() == 0) {
                final AcceptUserToChatGroupTask acceptUser = new AcceptUserToChatGroupTask(chatGroup.getGroupName(), mainApp.getGUIManager().getEmail());
                acceptUser.setOnSucceeded(t -> {
                    Platform.runLater(() -> {
                        refreshButton.fire();
                    });
                });

                mainApp.getDatabaseExecutor().submit(acceptUser);
            }
        }
    }


    private void handleCreateButton(ActionEvent actionEvent) {
        // Create the custom dialog.
        Dialog<Pair<String, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Create ChatGroup");
        dialog.setHeaderText("Create a chat group");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the name and duration labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField name = new TextField();
        name.setPromptText("Name");
        TextField duration = new TextField();
        duration.setPromptText("Duration");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Duration"), 0, 1);
        grid.add(duration, 1, 1);

        // Enable/Disable login button depending on whether a name was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        duration.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the name field by default.
        Platform.runLater(name::requestFocus);

        // Convert the result to a name-duration-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(name.getText(), Integer.parseInt(duration.getText()));
            }
            return null;
        });

        Optional<Pair<String, Integer>> result = dialog.showAndWait();

        result.ifPresent(chatGroupSettings -> {
            System.out.println("Name=" + chatGroupSettings.getKey() + ", Duration=" + chatGroupSettings.getValue());
            createChatGroup(chatGroupSettings.getKey(), chatGroupSettings.getValue());
        });
    }

    private void createChatGroup(String key, Integer value) {
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();

        final CreateChatGroupTask createChatGroupTask = new CreateChatGroupTask(key, value, mainApp.getGUIManager().getEmail());
        final InviteUserToChatGroupTask inviteUserToChatGroupTask = new InviteUserToChatGroupTask(key, mainApp.getGUIManager().getEmail());
        final AcceptUserToChatGroupTask acceptUserToChatGroupTask = new AcceptUserToChatGroupTask(key, mainApp.getGUIManager().getEmail());

        acceptUserToChatGroupTask.setOnSucceeded(t -> {
            Platform.runLater(()->{
                ps.stopSpinner();
                refreshButton.fire();
            });
        });

        mainApp.getDatabaseExecutor().submit(createChatGroupTask);
        mainApp.getDatabaseExecutor().submit(inviteUserToChatGroupTask);
        mainApp.getDatabaseExecutor().submit(acceptUserToChatGroupTask);
    }

    private void handleBackAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showHomeLayout();
    }
    private void handleChatSettingsAction(ActionEvent actionEvent) {
        ChatGroup chatGroup = chatGroupList.getSelectionModel().getSelectedItem();
        if(chatGroup != null){
            if(chatGroup.getOwner().equals(mainApp.getGUIManager().getEmail()))
                changeChatGroupSettings(chatGroup.getGroupName(), chatGroup.getDuration());
        }
    }

    private void changeChatGroupSettings(String groupName, int duration) {
        // Create the custom dialog.
        Dialog<Pair<String, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Edit ChatGroup");
        dialog.setHeaderText("Edit a chat group");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the name and durationOld labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField name = new TextField();
        name.setText(groupName);
        TextField durationOld = new TextField();
        durationOld.setText(String.valueOf(duration));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Duration"), 0, 1);
        grid.add(durationOld, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Request focus on the name field by default.
        Platform.runLater(name::requestFocus);

        // Convert the result to a name-durationOld-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(name.getText(), Integer.parseInt(durationOld.getText()));
            }
            return null;
        });

        Optional<Pair<String, Integer>> result = dialog.showAndWait();

        result.ifPresent(chatGroupSettings -> {
            System.out.println("Name=" + chatGroupSettings.getKey() + ", Duration=" + chatGroupSettings.getValue());
            editChatGroupSettings(groupName, chatGroupSettings.getKey(), chatGroupSettings.getValue());
        });
    }

    private void editChatGroupSettings(String groupName, String key, Integer value) {
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();

        final EditChatGroupTask editChatGroupTask = new EditChatGroupTask(groupName, key, value);

        editChatGroupTask.setOnSucceeded(t -> {
            Platform.runLater(()->{
                ps.stopSpinner();
                refreshButton.fire();
            });
        });

        mainApp.getDatabaseExecutor().submit(editChatGroupTask);
    }

    public void refreshList() {
        refreshButton.fire();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

}
