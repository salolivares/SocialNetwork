package edu.cs174a.buzmo.controllers;

import edu.cs174a.buzmo.MainApp;

public class HomeController {
    private MainApp mainApp;

    /**
     * Is called by the main application to give a reference back to itself.
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
