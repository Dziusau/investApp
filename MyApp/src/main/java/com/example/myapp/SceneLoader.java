package com.example.myapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneLoader {

    static Stage stage;

    static int role;

    static String name;

    public static void setRole(int i){
        role = i;
    }

    public static int getRole() { return role; }

    public static void setName(String i){
        name = i;
    }

    public static String getName() { return name; }


    public static void loadScene(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(UserApplication.class.getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load(), 640, 400);

        stage.setScene(scene);
        stage.show();
    }
}
