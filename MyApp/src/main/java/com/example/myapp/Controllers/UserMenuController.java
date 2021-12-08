package com.example.myapp.Controllers;

import com.example.myapp.SceneLoader;
import javafx.fxml.FXML;
import java.io.IOException;

public class UserMenuController {

    //FXML stuff

    //region onMethods
    @FXML
    protected void onProfileButtonClick() {
        try {
            SceneLoader.loadScene("View/userMenuProfile.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onActiveButtonClick() {
        try {
            SceneLoader.loadScene("View/userMenuActive.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onPortfolioButtonClick() {
        try {
            SceneLoader.loadScene("View/userMenuPortfolio.fxml");
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
