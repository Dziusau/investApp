package com.example.myapp;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class UserApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        SceneLoader.stage = stage;
        SceneLoader.loadScene("View/intro.fxml");

        stage.setTitle("Invest_like_God");
    }

    public static void main(String[] args) {
        launch();
    }
}