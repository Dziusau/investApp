package com.example.myapp.Controllers;

import com.example.myapp.Active;
import com.example.myapp.Client;
import com.example.myapp.SceneLoader;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.Objects;

public class UserMenuActiveTableController {

    //FXML stuff

    //region Fields
    @FXML
    private TableView<Active> table;

    @FXML
    private TableColumn tc1;

    @FXML
    private TableColumn tc2;

    @FXML
    private TableColumn tc3;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField amountField;
    //endregion

    //region onMethods
    @FXML
    public void initialize() {

        errorLabel.setVisible(false);
    }

    @FXML
    protected void onOpen() {
        errorLabel.setVisible(false);

        tc1.setCellValueFactory(new PropertyValueFactory<Active, String>("activeName"));
        tc2.setCellValueFactory(new PropertyValueFactory<Active, String>("activeType"));
        tc3.setCellValueFactory(new PropertyValueFactory<Active, Integer>("price"));

        String clientMessage = "viewActiveUser";

        Client client = new Client("0.0.0.0", 2525);

        client.startConnection(clientMessage);

        table.setItems(client.getActives());
    }

    @FXML
    protected void onGraphicButtonClick(){
        errorLabel.setVisible(false);
    }

    @FXML
    protected void onBuyButtonClick(){
        errorLabel.setVisible(false);

        Active active = table.getSelectionModel().getSelectedItem();

        String clientMessage = "buyActive/" + SceneLoader.getName();

        if (Objects.equals(amountField.getText(), "")) {
            errorLabel.setVisible(true);
            errorLabel.setText("Ошибка! Введите количество.");
            errorLabel.setStyle("-fx-text-fill: red;");
        }
        else {
            clientMessage += '/' + active.getActiveName() +
                    '/' + amountField.getText();

            Client client = new Client("0.0.0.0", 2525);

            client.startConnection(clientMessage);
            String serverAnswer = client.getServerAnswer();

            if (serverAnswer.equals("success")) {
                errorLabel.setVisible(true);
                errorLabel.setText("Актив успешно приобретен!");
                errorLabel.setStyle("-fx-text-fill: green;");
            } else {
                errorLabel.setVisible(true);
                errorLabel.setText("Ошибка! Недостаточно средств. Пополните баланс");
                errorLabel.setStyle("-fx-text-fill: red;");
            }
        }
    }

    @FXML
    protected void onExitButtonClick() {
        try {
            SceneLoader.loadScene("View/userMenuActive.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion
}
