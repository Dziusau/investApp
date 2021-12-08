package com.example.myapp.Controllers;

import com.example.myapp.Client;
import com.example.myapp.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Objects;

public class UserMenuProfileController {

    //FXML stuff

    //region Fields
    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private Label errorLabel;

    private boolean isDeleted = false;
    //endregion

    //region onMethods
    @FXML
    public void initialize() {

        errorLabel.setVisible(false);
        emailField.setDisable(true);
    }

    @FXML
    protected void onViewButtonClick() {
        if (!isDeleted) {
            errorLabel.setVisible(false);

            String clientMessage = "viewUser/" + SceneLoader.getName();

            Client client = new Client("0.0.0.0", 2525);

            client.startConnection(clientMessage);
            String serverAnswer = client.getServerAnswer();

            String[] words;
            if (!serverAnswer.equals("fail")){
                words = serverAnswer.split("/");
                nameField.setText(words[0]);
                passwordField.setText(words[1]);
                emailField.setText(SceneLoader.getName());
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
            String clientMessage = "editUser/" + SceneLoader.getName();

            if (Objects.equals(nameField.getText(), "") &&
                    Objects.equals(passwordField.getText(), "")) {
                errorLabel.setVisible(true);
                errorLabel.setText("Ошибка! Измените хотя бы одно поле.");
                errorLabel.setStyle("-fx-text-fill: red;");
            } else {
                if (!Objects.equals(nameField.getText(), "")) {
                    clientMessage += '/' + nameField.getText();
                } else clientMessage += '/';
                if (!Objects.equals(passwordField.getText(), "")) {
                    clientMessage += '/' + passwordField.getText();
                } else clientMessage += "/.";

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

            String clientMessage = "deleteUser/" + SceneLoader.getName();

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
                SceneLoader.loadScene("View/userMenu.fxml");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion
}
