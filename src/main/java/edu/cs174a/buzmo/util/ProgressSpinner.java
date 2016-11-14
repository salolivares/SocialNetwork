package edu.cs174a.buzmo.util;


import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ProgressSpinner {
    StackPane root;
    ProgressIndicator pi;
    VBox box;

    public ProgressSpinner(StackPane root) {
        this.root = root;
        this.pi = new ProgressIndicator();
        this.box = new VBox(pi);
        box.setAlignment(Pos.CENTER);

    }

    public void startSpinner() {
        // Grey out screen
        root.getChildren().get(0).setDisable(true);
        root.getChildren().add(box);
    }

    public void stopSpinner(){
        root.getChildren().get(0).setDisable(false);
        root.getChildren().remove(1);
    }


}
