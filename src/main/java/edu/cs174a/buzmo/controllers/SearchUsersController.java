package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import edu.cs174a.buzmo.tasks.*;
import edu.cs174a.buzmo.util.ProgressSpinner;
import edu.cs174a.buzmo.util.TopicWord;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.swing.*;

/**
 * Created by jordannguyen on 11/30/16.
 */
public class SearchUsersController {
    private MainApp mainApp;


    @FXML private Button backButton;
    @FXML private TextField emailTextField;
    @FXML private TextField topicTextField;
    @FXML private TextField numMessagesTextField;
    @FXML private ChoiceBox<Integer> recentChoiceBox;
    @FXML private ListView<String> userListField;
    @FXML private Button searchButton;
    @FXML private Button addFriendButton;

    public SearchUsersController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        backButton.setOnAction(this::handleBackAction);
        searchButton.setOnAction(this::handleSearchAction);
        recentChoiceBox.setItems(FXCollections.observableArrayList(
                1, 2, 3, 4, 5, 6, 7
        ));
        addFriendButton.setOnAction(this::handleAddFriendAction);
    }

    private void handleAddFriendAction(ActionEvent actionEvent) {
        String email2 = userListField.getSelectionModel().getSelectedItem();
        if(email2 != null){
            addFriend(email2);
        }
    }

    private void addFriend(String email2) {
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();

        final AddFriendTask addFriendTask = new AddFriendTask(mainApp.getGUIManager().getEmail(), email2);

        addFriendTask.setOnSucceeded(t -> {
            Platform.runLater(()->{
                ps.stopSpinner();
            });
        });

        mainApp.getDatabaseExecutor().submit(addFriendTask);
    }

    private void handleSearchAction(ActionEvent actionEvent) {
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();

        final FetchUsersTask fetchUsersTask = new FetchUsersTask(emailTextField.getText(), topicTextField.getText(),
                                                numMessagesTextField.getText(), recentChoiceBox.getValue());

        fetchUsersTask.setOnSucceeded(t -> {
            Platform.runLater(ps::stopSpinner);
            userListField.setItems(fetchUsersTask.getValue());
        });

        fetchUsersTask.setOnFailed(t -> {
            Platform.runLater(ps::stopSpinner);
            System.out.println("FAILED");
        });

        mainApp.getDatabaseExecutor().submit(fetchUsersTask);
    }



    private void handleBackAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showHomeLayout();
    }


    /**
     * Is called by the main application to give a reference back to itself.
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
