package com.example.myapp.Controllers;

import com.example.myapp.SceneLoader;
import javafx.fxml.FXML;
import java.io.IOException;

public class IntroController {

    //FXML stuff

    //region onMethods
    @FXML
    protected void onUserButtonClick() {
        try {
            SceneLoader.setRole(1);
            SceneLoader.loadScene("View/name_password.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onAdminButtonClick() {
        try {
            SceneLoader.setRole(2);
            SceneLoader.loadScene("View/name_password.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onRegisterButtonClick() {
        try {
            SceneLoader.loadScene("View/registration.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion
}