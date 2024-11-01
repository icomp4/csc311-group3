package org.compi.csc311group3.view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrationController {
    @FXML
    private TextField regUsername;
    @FXML
    private TextField regPassword;
    @FXML
    private TextField regEmail;
    @FXML
    private Button registerbttn;

    //TODO Registration CSS page
    @FXML
    private void Register(ActionEvent event) {
        //TODO adds it to the database

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/compi/csc311group3/login-view.fxml"));
            Scene registrationScene = new Scene(fxmlLoader.load(), 600, 400);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();//dont rly understand this line
            stage.setScene(registrationScene);
            stage.setTitle("App Name");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
