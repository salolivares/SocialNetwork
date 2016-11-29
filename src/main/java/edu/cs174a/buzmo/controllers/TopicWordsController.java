package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;
import edu.cs174a.buzmo.tasks.CreateUserTopicWordTask;
import edu.cs174a.buzmo.tasks.FetchTopicWordsTask;
import edu.cs174a.buzmo.util.ProgressSpinner;
import edu.cs174a.buzmo.util.TopicWord;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class TopicWordsController {
    private MainApp mainApp;

    @FXML private Button addButton;
    @FXML private Button removeButton;
    @FXML private TextField wordTextField;
    @FXML private ListView<TopicWord> topicList;
    @FXML private Button backButton;
    @FXML private Button refreshButton;


    public TopicWordsController() {

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        refreshButton.setOnAction(this::handleRefreshAction);
        backButton.setOnAction(this::handleBackAction);
        addButton.setOnAction(this::handleAddAction);
        topicList.setCellFactory(param -> new ListCell<TopicWord>() {
            @Override
            protected void updateItem(TopicWord item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getWord() == null) {
                    setText(null);
                } else {
                    setText(item.getWord());
                }
            }
        });
    }

    private void handleAddAction(ActionEvent actionEvent) {
        addNewTopicWord();
    }

    private void addNewTopicWord() {
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();

        final CreateUserTopicWordTask createUserTopicWordTask = new CreateUserTopicWordTask(mainApp.getGUIManager().getEmail(), wordTextField.getText());

        createUserTopicWordTask.setOnSucceeded(t -> {
            Platform.runLater(ps::stopSpinner);
            mainApp.getDatabaseExecutor().submit(createUserTopicWordTask);
        });

        mainApp.getDatabaseExecutor().submit(createUserTopicWordTask);
    }

    private void handleRefreshAction(ActionEvent actionEvent) {
        System.out.println("Refreshing...");
        populateTopicWordList();
        System.out.println("Populated!");
    }

    private void populateTopicWordList() {
        ProgressSpinner ps = new ProgressSpinner(mainApp.getRootLayout());
        ps.startSpinner();

        final FetchTopicWordsTask fetchTopicWordsTask = new FetchTopicWordsTask(mainApp.getGUIManager().getEmail());

        fetchTopicWordsTask.setOnSucceeded(t -> {
            Platform.runLater(ps::stopSpinner);
            topicList.setItems(fetchTopicWordsTask.getValue());
        });

        fetchTopicWordsTask.setOnFailed(t -> {
            Platform.runLater(ps::stopSpinner);
            System.out.println("FAILED");
        });

        mainApp.getDatabaseExecutor().submit(fetchTopicWordsTask);
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
