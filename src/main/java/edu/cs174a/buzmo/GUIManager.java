package edu.cs174a.buzmo;


import java.io.IOException;

import edu.cs174a.buzmo.controllers.HomeController;
import edu.cs174a.buzmo.controllers.LoginController;
import edu.cs174a.buzmo.controllers.NewAccountController;
import edu.cs174a.buzmo.controllers.TopicWordsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/** Manages control flow for login */
public class GUIManager {
    private MainApp mainApp;
    private String email;

    public GUIManager(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Callback method invoked to notify that a user has been authenticated.
     * Will show the main application screen.
     */
    public void authenticated(String email) {
        System.out.println("Authenticated: " + email );
        this.email = email;
        showHomeLayout();
    }

    /**
     * Callback method invoked to notify that a user has logged out of the main application.
     * Will show the login application screen.
     */
    public void logout() {
        showLoginScreen();
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
            ((BorderPane)mainApp.getRootLayout().getChildren().get(0)).setCenter(loginLayout);

            // Give the controller access to the main app.
            LoginController controller = loader.getController();
            controller.setMainApp(mainApp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showHomeLayout() {
        try {
            // Load Login Layout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/HomeLayout.fxml"));
            AnchorPane homeLayout = (AnchorPane) loader.load();

            // Set login layout into the center of root layout.
            ((BorderPane)mainApp.getRootLayout().getChildren().get(0)).setCenter(homeLayout);

            // Give the controller access to the main app.
            HomeController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setEmail(this.email);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCreateAccountLayout() {
        try {
            // Load Login Layout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/NewAccountLayout.fxml"));
            AnchorPane newAccountLayout = (AnchorPane) loader.load();

            // Set login layout into the center of root layout.
            ((BorderPane)mainApp.getRootLayout().getChildren().get(0)).setCenter(newAccountLayout);

            // Give the controller access to the main app.
            NewAccountController controller = loader.getController();
            controller.setMainApp(mainApp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showTopicWordsLayout() {
        try {
            // Load Login Layout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/TopicWordsLayout.fxml"));
            AnchorPane topicWordsLayout = (AnchorPane) loader.load();

            // Set login layout into the center of root layout.
            ((BorderPane)mainApp.getRootLayout().getChildren().get(0)).setCenter(topicWordsLayout);

            // Give the controller access to the main app.
            TopicWordsController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.refreshWordList();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getEmail() {
        return email;
    }
}