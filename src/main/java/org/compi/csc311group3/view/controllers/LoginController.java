package org.compi.csc311group3.view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private Button loginButton;

    @FXML
    private TextField pwTextField;

    @FXML
    private Button registerButton;

    @FXML
    private TextField usernameTextField;

    public void initialize() {

        // Set the ids for the elements so we can style them individually in the CSS file
        loginButton.setId("loginButton");
        pwTextField.setId("pwTextField");
        usernameTextField.setId("usernameTextField");
        registerButton.setId("registerButton");
    }
    @FXML
    void LoginClicked(ActionEvent event) {

        // TODO: Implement login functionality after implementing the database

    }

    @FXML
    void RegisterClicked(ActionEvent event) {

        // TODO: Implement opening the registration page once it's added

    }

}
