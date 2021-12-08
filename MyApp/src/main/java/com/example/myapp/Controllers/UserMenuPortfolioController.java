package com.example.myapp.Controllers;

import com.example.myapp.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class UserMenuPortfolioController {

    //FXML stuff

    //region Fields
    @FXML
    private Label errorLabel;
    //endregion

    //region onMethods
    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
    }

    @FXML
    protected void onViewActivesButtonClick() {

        errorLabel.setVisible(false);
        try {
            SceneLoader.loadScene("View/userMenuPortfolioTable.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onGraphicPortfolioButtonClick() {

    }

    @FXML
    protected void onDiagramPortfolioButtonClick() {

    }

    @FXML
    protected void onExitButtonClick() {
        try {
            SceneLoader.loadScene("View/userMenu.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion
}
