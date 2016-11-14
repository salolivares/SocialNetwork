package edu.cs174a.buzmo;

import java.io.IOException;

import edu.cs174a.buzmo.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    private StackPane rootLayout;

    public MainApp() {
        /* Setup all global variables here. For example all the models. */
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Buzmo");

        initRootLayout();

        showLoginScreen();
    }

    @Override
    public void stop() {
        System.out.println("Application is exiting");
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
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
     * Shows the login screen inside the root layout.
     */
    public void showLoginScreen() {
        try {
            // Load Login Layout.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/LoginLayout.fxml"));
            AnchorPane loginLayout = (AnchorPane) loader.load();

            // Set login layout into the center of root layout.
            ((BorderPane)rootLayout.getChildren().get(0)).setCenter(loginLayout);

            // Give the controller access to the main app.
            LoginController controller = loader.getController();
            controller.setMainApp(this);

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

    public static void main(String[] args) {
        launch(args);
    }
}