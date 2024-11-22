package org.compi.csc311group3.view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.compi.csc311group3.HelloApplication;
import org.compi.csc311group3.service.UserService;

import java.io.IOException;

import static org.compi.csc311group3.HelloApplication.ChangeScreen;


public class LoginController {

    @FXML
    private Button loginButton;

    @FXML
    private TextField pwTextField;

    @FXML
    private Button registerButton;

    @FXML
    private TextField usernameTextField;

    private UserService userService = UserService.getInstance();

    public void initialize() {

        // Set the ids for the elements so we can style them individually in the CSS file
        loginButton.setId("loginButton");
        pwTextField.setId("pwTextField");
        usernameTextField.setId("usernameTextField");
        registerButton.setId("registerButton");
        
        // Disable the login button, and attach our validation methods
        loginButton.setDisable(true);

        // Add listeners to trigger validation
        usernameTextField.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
        pwTextField.textProperty().addListener((observable, oldValue, newValue) -> validateInputs());
    }

    /**
     * Validates the inputs of all fields, and updates the login button accordingly
     */
    private void validateInputs() {
        boolean isUsernameValid = isUsernameValid();
        boolean isPasswordValid = isPasswordValid();

        // Enable login button only if both fields are valid
        loginButton.setDisable(!(isUsernameValid && isPasswordValid));
    }

    /**
     * Validates the username field
     * @return true if the username is not empty and has at least 5 characters
     */
    private boolean isUsernameValid() {
        String username = usernameTextField.getText();
        return username != null && !username.trim().isEmpty() && username.length() >= 5;
    }

    /**
     * Validates the password field
     * @return true if the password is not empty and has at least 5 characters
     */
    private boolean isPasswordValid() {
        String password = pwTextField.getText();

        return password != null && !password.trim().isEmpty() && password.length() >= 5;
    }

    @FXML
    void LoginClicked(ActionEvent event) throws IOException {
        if(isUsernameValid() && isPasswordValid()) {
            boolean loggedIn = userService.Login(usernameTextField.getText(), pwTextField.getText());
            if(loggedIn){
                ChangeScreen("dashboard-view.fxml", 850, 560, loginButton);
            } else {
                System.out.println("Invalid credentials");
            }
        }
    }

    @FXML
    void RegisterClicked(ActionEvent event) throws IOException {
        ChangeScreen("registration-view.fxml", 600, 400, registerButton);
    }

    // TODO: Implement opening the registration page once it's added

}
