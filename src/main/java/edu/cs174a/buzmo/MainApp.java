package edu.cs174a.buzmo;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    private StackPane rootLayout;
    private LoginManager loginManager;

    public MainApp() {
        /* Setup all global variables here. For example all the models. */
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Buzmo");

        loginManager = new LoginManager(this);

        initRootLayout();
        loginManager.showLoginScreen();

    }

    @Override
    public void stop() {
        System.out.println("Application is exiting");
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

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public static void main(String[] args) {
        launch(args);
    }
}