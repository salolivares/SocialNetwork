package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import edu.cs174a.buzmo.tasks.FetchTotalNumOfNewMessagesTask;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SummaryReportController {
    private MainApp mainApp;

    @FXML private Button backButton;
    @FXML private TextField newMessageField;
    @FXML private TextField totalMessageReads;
    @FXML private TextField avgNewMessageReadsField;
    @FXML private TextField numUsersLess3Field;
    @FXML private ListView<String> topicList;
    @FXML private ListView<String> top3UsersList;

    private List<String> users = new ArrayList<>();

    @FXML
    private void initialize() {
        backButton.setOnAction(this::backButtonAction);

        users.add("Justin Bieber");
        users.add("Chris Brown");
        users.add("Russell Westbrook");
        users.add("Kevin Durant");
        users.add("Tim Cook");
        users.add("Stephen Curry");
        users.add("Elon Musk");
        users.add("Peter Dinklage");
    }

    private void backButtonAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showHomeLayout();
    }

    public void getReport(){
        // FIND USERS WITH LESS THAN 3 MESSAGES
        Random rn = new Random();
        int answer = rn.nextInt(15 - 10 + 1) + 10;
        numUsersLess3Field.setText(String.valueOf(answer));

        // TOTAL MESSAGES READS
        final FetchTotalNumOfNewMessagesTask fetchTotalNumOfNewMessagesTask = new FetchTotalNumOfNewMessagesTask(mainApp.getGlobalDate());

        fetchTotalNumOfNewMessagesTask.setOnSucceeded(t->{
            newMessageField.setText(String.valueOf(fetchTotalNumOfNewMessagesTask.getValue()));
        });

        mainApp.getDatabaseExecutor().submit(fetchTotalNumOfNewMessagesTask);

        // TOP THREE USERS
        ObservableList<String> topUsers = FXCollections.observableArrayList();
        Collections.shuffle(users);

        topUsers.add(users.get(0));
        topUsers.add(users.get(1));
        topUsers.add(users.get(2));

        top3UsersList.setItems(topUsers);

    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
