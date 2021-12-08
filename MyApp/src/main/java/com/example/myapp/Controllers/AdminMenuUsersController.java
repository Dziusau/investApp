package com.example.myapp.Controllers;

import com.example.myapp.Client;
import com.example.myapp.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Objects;

public class AdminMenuUsersController {
    //FXML stuff

    //region Fields
    @FXML
    private TextField emailNotifyField;

    @FXML
    private TextField textNotifyField;

    @FXML
    private TextField emailOperField;

    @FXML
    private Label errorLabel;
    //endregion

    //region onMethods
    @FXML
    public void initialize() {

        errorLabel.setVisible(false);
    }

    @FXML
    protected void onSendNotificationButtonClick() {

    }

    @FXML
    protected void onViewOperationsButtonClick() {

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
