package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import edu.cs174a.buzmo.tasks.FetchDBTask;
import edu.cs174a.buzmo.tasks.UpdateDBTimeTask;
import edu.cs174a.buzmo.util.ProgressSpinner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.*;
import java.util.Optional;

public class HomeController {
    private MainApp mainApp;
    private String email;

    @FXML private Label loggedinLabel;
    @FXML private Label isManagerLabel;
    @FXML private Button logoutButton;
    @FXML private Button topicWordButton;
    @FXML private Button searchButton;
    @FXML private Button myCircleButton;
    @FXML private Button chatGroupButton;
    @FXML private Button privateMessageButton;
    @FXML private DatePicker datePicker;
    @FXML private Button setTimeButton;
    @FXML private TextField timeTextField;

    public HomeController() {
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        topicWordButton.setOnAction(this::handleTopicButtonAction);
        logoutButton.setOnAction(this::handleLogoutButtonAction);
        searchButton.setOnAction(this::handleSearchButtonAction);
        myCircleButton.setOnAction(this::handleMyCircleButton);
        chatGroupButton.setOnAction(this::handleChatGroupButton);
        privateMessageButton.setOnAction(this::handlePrivateMessageButton);
        setTimeButton.setOnAction(this::handleSetTime);
    }

    private void handleSetTime(ActionEvent actionEvent) {
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();

        LocalTime time = LocalTime.parse(timeTextField.getText());
        LocalDate date = datePicker.getValue();

        final UpdateDBTimeTask updateDBTimeTask = new UpdateDBTimeTask(date.toString(), time.toString());

        updateDBTimeTask.setOnSucceeded(t->{

            Platform.runLater(ps::stopSpinner);
            mainApp.setGlobalDate(date);
            mainApp.setGlobalTime(time);
        });

        mainApp.getDatabaseExecutor().submit(updateDBTimeTask);

    }

    private void handleChatGroupButton(ActionEvent actionEvent) {
        mainApp.getGUIManager().showChatGroupsLayout();
    }

    private void handleMyCircleButton(ActionEvent actionEvent) {
        mainApp.getGUIManager().showMyCircleMenuLayout();
    }

    private void handleTopicButtonAction(ActionEvent actionEvent) {
        mainApp.getGUIManager().showTopicWordsLayout();
    }

    private void handleLogoutButtonAction(ActionEvent actionEvent) {
        // Save time to database

        Duration timeElapsed = Duration.between(mainApp.getStartTime(), Instant.now());

        LocalTime time = mainApp.getGlobalTime().plusMinutes(timeElapsed.toMinutes());

        // WRITE NEW TIME TO DATABASE
        final UpdateDBTimeTask updateDBTimeTask = new UpdateDBTimeTask(mainApp.getGlobalDate().toString(), time.toString());

        mainApp.getDatabaseExecutor().submit(updateDBTimeTask);

        mainApp.getGUIManager().logout();
    }

    private void handleSearchButtonAction(ActionEvent actionEvent) { mainApp.getGUIManager().showSearchUsersLayout();}

    private void handlePrivateMessageButton(ActionEvent actionEvent) { mainApp.getGUIManager().showPrivateMessageLayout(); }

    /**
     * Is called by the main application to give a reference back to itself.
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setEmail(String email) {
        this.email = email;
        loggedinLabel.setText("Logged in: " + email);
    }

    public void setNewTime(){
        // GET TIME FROM DATABASE
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();

        final FetchDBTask fetchDBTask = new FetchDBTask();

        fetchDBTask.setOnSucceeded(t -> {
            Platform.runLater(ps::stopSpinner);

            String[] dataTime = fetchDBTask.getValue();

            LocalDate date = LocalDate.parse(dataTime[0]);
            LocalTime time = LocalTime.parse(dataTime[1]);

            mainApp.setGlobalDate(date);
            mainApp.setGlobalTime(time);
        });

        mainApp.getDatabaseExecutor().submit(fetchDBTask);
    }
}
