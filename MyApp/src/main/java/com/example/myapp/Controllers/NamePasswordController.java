package com.example.myapp.Controllers;

import com.example.myapp.Client;
import com.example.myapp.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class NamePasswordController {

    //FXML stuff

    //region Fields
    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label nameLabel;

    @FXML
    private Label errorLabel;
    //endregion

    //region onMethods
    @FXML
    public void initialize() {
        errorLabel.setVisible(false);

        nameLabel.setText((SceneLoader.getRole() == 1)?"Email:":"Имя:");
        nameField.setPromptText((SceneLoader.getRole() == 1)?"Введите email":"Введите имя");
    }

    @FXML
    protected void onEnterButtonClick() {
        String clientMessage = "ent/";
        clientMessage += ((SceneLoader.getRole() == 1)?"1/":"2/") +
                nameField.getText() + '/' +
                passwordField.getText();

        Client client = new Client("0.0.0.0", 2525);

        client.startConnection(clientMessage);
        String serverAnswer = client.getServerAnswer();

        if (serverAnswer.equals("success")){
            try {
                SceneLoader.setName(nameField.getText());
                SceneLoader.loadScene(
                        (SceneLoader.getRole() == 1)?"View/userMenu.fxml":"View/adminMenu.fxml"
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            errorLabel.setVisible(true);
            errorLabel.setText("Ошибка входа! Проверьте правильность введенных данных.");
            errorLabel.setStyle("-fx-text-fill: red;");
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
