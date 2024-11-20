package org.compi.csc311group3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Finance Application");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method is used to change the screen
     * @param fxml the fxml file to load
     * @param x the width of the screen
     * @param y the height of the screen
     * @param currentNode the current node (e.g. the button that was clicked)
     */
    public static void ChangeScreen(String fxml, int x, int y, Node currentNode) throws IOException {

        // Get the current stage and close it
        Stage currentStage = (Stage) currentNode.getScene().getWindow();
        currentStage.close();

        // Open the new stage with the new screen (fxml) and dimensions (x, y)
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load(), x, y);
        Stage stage = new Stage();
        stage.setTitle("Finance Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}