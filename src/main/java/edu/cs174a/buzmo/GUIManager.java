package edu.cs174a.buzmo;


import java.io.IOException;

import edu.cs174a.buzmo.controllers.*;
import edu.cs174a.buzmo.util.ChatGroup;
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
            controller.setNewTime();
            controller.isManager();
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

    public void showSearchUsersLayout() {
        try {
            // Load Login Layout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/SearchUsersLayout.fxml"));
            AnchorPane searchUsersLayout = (AnchorPane) loader.load();

            // Set login layout into the center of root layout.
            ((BorderPane)mainApp.getRootLayout().getChildren().get(0)).setCenter(searchUsersLayout);

            // Give the controller access to the main app.
            SearchUsersController controller = loader.getController();
            controller.setMainApp(mainApp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMyCircleMenuLayout() {
        try {
            // Load Login Layout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/MyCircleMenuLayout.fxml"));
            AnchorPane searchUsersLayout = (AnchorPane) loader.load();

            // Set login layout into the center of root layout.
            ((BorderPane)mainApp.getRootLayout().getChildren().get(0)).setCenter(searchUsersLayout);

            // Give the controller access to the main app.
            MyCircleController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.refreshFriendsList();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFriendRequestLayout() {
        try {
            // Load Login Layout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/FriendRequestLayout.fxml"));
            AnchorPane searchUsersLayout = (AnchorPane) loader.load();

            // Set login layout into the center of root layout.
            ((BorderPane)mainApp.getRootLayout().getChildren().get(0)).setCenter(searchUsersLayout);

            // Give the controller access to the main app.
            FriendRequestController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.refreshRequestList();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getEmail() {
        return email;
    }

    public void showChatGroupsLayout() {
        try {
            // Load Login Layout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/ChatGroupsLayout.fxml"));
            AnchorPane searchUsersLayout = (AnchorPane) loader.load();

            // Set login layout into the center of root layout.
            ((BorderPane)mainApp.getRootLayout().getChildren().get(0)).setCenter(searchUsersLayout);

            // Give the controller access to the main app.
            ChatGroupsController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.refreshList();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPrivateMessageLayout() {
        try {
            // Load Login Layout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/PrivateMessageLayout.fxml"));
            AnchorPane searchUsersLayout = (AnchorPane) loader.load();

            // Set login layout into the center of root layout.
            ((BorderPane)mainApp.getRootLayout().getChildren().get(0)).setCenter(searchUsersLayout);

            // Give the controller access to the main app.
            PrivateMessageController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.refreshFriendsList();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMyCircleMessageLayout() {
        try {
            // Load Login Layout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/MyCircleMessageLayout.fxml"));
            AnchorPane searchUsersLayout = (AnchorPane) loader.load();

            // Set login layout into the center of root layout.
            ((BorderPane)mainApp.getRootLayout().getChildren().get(0)).setCenter(searchUsersLayout);

            // Give the controller access to the main app.
            MyCircleMessageController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.refreshMessages();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSearchMessagesLayout() {
        try {
            // Load Login Layout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/SearchMessagesLayout.fxml"));
            AnchorPane searchUsersLayout = (AnchorPane) loader.load();

            // Set login layout into the center of root layout.
            ((BorderPane)mainApp.getRootLayout().getChildren().get(0)).setCenter(searchUsersLayout);

            // Give the controller access to the main app.
            SearchMessagesController controller = loader.getController();
            controller.setMainApp(mainApp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showChatGroupInviteLayout(ChatGroup chatgroup) {
        try {
            // Load Login Layout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/view/ChatGroupInviteLayout.fxml"));
            AnchorPane searchUsersLayout = (AnchorPane) loader.load();

            // Set login layout into the center of root layout.
            ((BorderPane)mainApp.getRootLayout().getChildren().get(0)).setCenter(searchUsersLayout);

            // Give the controller access to the main app.
            ChatGroupInviteController controller = loader.getController();
            controller.setMainApp(mainApp);
            controller.setChatGroup(chatgroup);
            controller.updateLabel();
            controller.refreshFriendsList();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}