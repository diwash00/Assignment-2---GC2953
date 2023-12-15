package com.example.moviesexplorer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.Image;


import java.io.IOException;

public class HelloApplication extends Application {

    // Variables to store initial mouse cursor position
    private double x = 0;
    private double y = 0;


    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file for the splash screen
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("splash.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Movies Explorer");

        // Set the icon for the stage
        stage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));



        // Set up event handlers for dragging the window by mouse cursor
        scene.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        scene.setOnMouseDragged(event -> {
            // Update the window position based on the mouse movement
            stage.setX(event.getScreenX() - x);
            stage.setY(event.getScreenY() - y);
        });



        // Set the scene and display the stage
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch();
    }
}
