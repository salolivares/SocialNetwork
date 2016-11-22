package edu.cs174a.buzmo;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.cs174a.buzmo.util.Database;
import edu.cs174a.buzmo.util.DatabaseThreadFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    private StackPane rootLayout;
    private GUIManager GUIManager;
    private Database databaseConnection;

    // executes database operations concurrent to JavaFX operations.
    private ExecutorService databaseExecutor;

    // initialize the program.
    // setting the database executor thread pool size to 1 ensures
    // only one database command is executed at any one time.
    @Override
    public void init() throws Exception {
        databaseExecutor = Executors.newFixedThreadPool(1, new DatabaseThreadFactory() );
    }

    public MainApp() {
        /* Setup all global variables here. For example all the models. */
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Buzmo");

        GUIManager = new GUIManager(this);

        initRootLayout();
        GUIManager.showLoginScreen();
    }

    @Override
    public void stop() throws Exception {
        databaseExecutor.shutdown();
        if (!databaseExecutor.awaitTermination(4, TimeUnit.SECONDS)) {
            System.out.println("Database execution thread timed out.");
        }
        Database.getInstance().closeConnection();
        System.out.println("Application is exiting...");
    }

    /**
     * Initializes the root layout.
     */
    private void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/RootLayout.fxml"));
            rootLayout = (StackPane) loader.load();

            // Show the scene containing the root layout.
            primaryStage.setScene(new Scene(rootLayout));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public StackPane getRootLayout() {
        return rootLayout;
    }

    public GUIManager getGUIManager() {
        return GUIManager;
    }

    public ExecutorService getDatabaseExecutor() {
        return databaseExecutor;
    }

    public static void main(String[] args) {
        launch(args);
    }
}