package com.example.myapp.Controllers;

import com.example.myapp.Client;
import com.example.myapp.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Objects;

public class AdminMenuProfileController {
    //FXML stuff

    //region Fields
    @FXML
    private TextField nameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label errorLabel;


    private boolean isDeleted;
    //endregion

    //region onMethods
    @FXML
    public void initialize() {

        errorLabel.setVisible(false);
        isDeleted = false;
        nameField.setDisable(true);
    }

    @FXML
    protected void onViewButtonClick() {
        if (!isDeleted) {
            errorLabel.setVisible(false);

            String clientMessage = "viewAdmin/" + SceneLoader.getName();

            Client client = new Client("0.0.0.0", 2525);

            client.startConnection(clientMessage);
            String serverAnswer = client.getServerAnswer();

            if (!serverAnswer.equals("fail")){
                nameField.setText(SceneLoader.getName());
                passwordField.setText(serverAnswer);
            }
            else {
                errorLabel.setVisible(true);
                errorLabel.setText("Ошибка! Что-то пошло не так.");
                errorLabel.setStyle("-fx-text-fill: red;");
            }
        }
        else {
            errorLabel.setVisible(true);
            errorLabel.setText("Ошибка! Пользователь был удален.");
            errorLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    protected void onEditButtonClick() {
        if (!isDeleted) {
            String clientMessage = "editAdmin/" + SceneLoader.getName();

            if (Objects.equals(passwordField.getText(), "")) {
                errorLabel.setVisible(true);
                errorLabel.setText("Ошибка! Измените поле пароля.");
                errorLabel.setStyle("-fx-text-fill: red;");
            }
            else {
                clientMessage += '/' + passwordField.getText();

                Client client = new Client("0.0.0.0", 2525);

                client.startConnection(clientMessage);
                String serverAnswer = client.getServerAnswer();

                if (serverAnswer.equals("success")) {
                    errorLabel.setVisible(true);
                    errorLabel.setText("Успешно обновлено!");
                    errorLabel.setStyle("-fx-text-fill: green;");
                }
            }
        }
        else {
            errorLabel.setVisible(true);
            errorLabel.setText("Ошибка! Пользователь был удален.");
            errorLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    protected void onDeleteButtonClick() {
        if (!isDeleted) {
            errorLabel.setVisible(false);

            String clientMessage = "deleteAdmin/" + SceneLoader.getName();

            Client client = new Client("0.0.0.0", 2525);

            client.startConnection(clientMessage);
            String serverAnswer = client.getServerAnswer();

            if (!serverAnswer.equals("fail")) {
                isDeleted = true;
                errorLabel.setVisible(true);
                errorLabel.setText("Пользователь успешно удален! Нажмите Назад для выхода.");
                errorLabel.setStyle("-fx-text-fill: green;");
            } else {
                errorLabel.setVisible(true);
                errorLabel.setText("Ошибка! Что-то пошло не так.");
                errorLabel.setStyle("-fx-text-fill: red;");
            }
        }
        else {
            errorLabel.setVisible(true);
            errorLabel.setText("Ошибка! Пользователь был удален.");
            errorLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    protected void onExitButtonClick() {
        try {
            if (isDeleted){
                SceneLoader.loadScene("View/intro.fxml");
            }
            else {
                SceneLoader.loadScene("View/adminMenu.fxml");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion
}
