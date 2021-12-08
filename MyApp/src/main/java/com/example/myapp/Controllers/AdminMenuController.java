package com.example.myapp.Controllers;

import com.example.myapp.SceneLoader;
import javafx.fxml.FXML;

import java.io.IOException;

public class AdminMenuController {
    //FXML stuff

    //region onMethods
    @FXML
    protected void onProfileButtonClick() {
        try {
            SceneLoader.loadScene("View/adminMenuProfile.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onActiveButtonClick() {
        try {
            SceneLoader.loadScene("View/adminMenuActive.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onUsersButtonClick() {
        try {
            SceneLoader.loadScene("View/adminMenuUsers.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onExitButtonClick() {
        try {
            SceneLoader.loadScene("View/intro.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion
}
