package com.example.myapp.Controllers;

import com.example.myapp.Client;
import com.example.myapp.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Objects;

public class UserMenuActiveController {

    //FXML stuff

    //region Fields
    @FXML
    private TextField balanceField;

    @FXML
    private TextField moneyField;

    @FXML
    private Label errorLabel;
    //endregion

    //region onMethods
    @FXML
    public void initialize() {

        errorLabel.setVisible(false);
        moneyField.setDisable(true);
    }

    @FXML
    protected void onFillBalanceButtonClick() {
        errorLabel.setVisible(false);
        moneyField.setDisable(false);
    }

    @FXML
    protected void onFillMoneyButtonClick() {
        errorLabel.setVisible(false);

        String clientMessage = "addCash/" + SceneLoader.getName();

        if (Objects.equals(moneyField.getText(), "")) {
            errorLabel.setVisible(true);
            errorLabel.setText("Ошибка! Введите сумму.");
            errorLabel.setStyle("-fx-text-fill: red;");
        }
        else {
            clientMessage += '/' + moneyField.getText();

            Client client = new Client("0.0.0.0", 2525);

            client.startConnection(clientMessage);
            String serverAnswer = client.getServerAnswer();

            if (serverAnswer.equals("success")) {
                errorLabel.setVisible(true);
                errorLabel.setText("Деньги успешно добавлены!");
                errorLabel.setStyle("-fx-text-fill: green;");
            }

            moneyField.setDisable(true);
        }
    }

    @FXML
    protected void onRemainsButtonClick() {
        errorLabel.setVisible(false);

        String clientMessage = "viewCash/" + SceneLoader.getName();

        Client client = new Client("0.0.0.0", 2525);

        client.startConnection(clientMessage);
        String serverAnswer = client.getServerAnswer();

        if (!serverAnswer.equals("fail")) {
            balanceField.setText(serverAnswer);
        }
        else {
            errorLabel.setVisible(true);
            errorLabel.setText("Ошибка! Что-то пошло не так.");
            errorLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    protected void onViewActivesButtonClick() {
        errorLabel.setVisible(false);
        try {
            SceneLoader.loadScene("View/userMenuActiveTable.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
