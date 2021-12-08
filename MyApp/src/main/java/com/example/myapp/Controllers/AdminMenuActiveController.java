package com.example.myapp.Controllers;

import com.example.myapp.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class AdminMenuActiveController {
    //FXML stuff

    //region Fields

    @FXML
    private Label errorLabel;
    //endregion

    //region onMethods
    @FXML
    protected void onAllActivesButtonClick() {
        try {
            SceneLoader.loadScene("View/adminMenuActiveTable.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onOperationsViewButtonClick() {
        try {
            SceneLoader.loadScene("View/adminMenuActiveOperations.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onExitButtonClick() {
        try {
            SceneLoader.loadScene("View/adminMenu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion
}
