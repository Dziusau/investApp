package com.example.myapp.Controllers;

import com.example.myapp.Active;
import com.example.myapp.Client;
import com.example.myapp.SceneLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.Objects;

public class AdminMenuActiveTableController {

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
    private TableColumn tc4;

    @FXML
    private ComboBox open_close;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField costField;

    ObservableList<String> types = FXCollections.observableArrayList("Открыть", "Закрыть");
    //endregion

    //region onMethods
    @FXML
    public void initialize() {

        errorLabel.setVisible(false);
        open_close.setValue("Открыть");
        open_close.setItems(types);
    }

    @FXML
    protected void onOpen() {
        errorLabel.setVisible(false);

        tc1.setCellValueFactory(new PropertyValueFactory<Active, String>("activeName"));
        tc2.setCellValueFactory(new PropertyValueFactory<Active, String>("activeType"));
        tc3.setCellValueFactory(new PropertyValueFactory<Active, Integer>("price"));
        tc4.setCellValueFactory(new PropertyValueFactory<Active, String>("access"));

        String clientMessage = "viewActiveAdmin";

        Client client = new Client("0.0.0.0", 2525);

        client.startConnection(clientMessage);

        table.setItems(client.getActives());
    }

    @FXML
    protected void onOpen_CloseButtonClick() {
        errorLabel.setVisible(false);

        Object value = open_close.getValue();
        Active active = table.getSelectionModel().getSelectedItem();

        if (active.getAccess().equals("не доступен") && value.equals("Закрыть")) {
            errorLabel.setVisible(true);
            errorLabel.setText("Ошибка! Актив уже не доступен");
            errorLabel.setStyle("-fx-text-fill: red;");
        }
        else if (active.getAccess().equals("доступен") && value.equals("Открыть")) {
            errorLabel.setVisible(true);
            errorLabel.setText("Ошибка! Актив уже доступен");
            errorLabel.setStyle("-fx-text-fill: red;");
        }
        else {
            String clientMessage = (value.equals("Закрыть") ? "closeActive/" : "openActive/");

            clientMessage += active.getActiveName();

            Client client = new Client("0.0.0.0", 2525);

            client.startConnection(clientMessage);
            String serverAnswer = client.getServerAnswer();

            if (serverAnswer.equals("success")) {
                onOpen();
                errorLabel.setVisible(true);
                errorLabel.setText("Статус актива успешно изменен!");
                errorLabel.setStyle("-fx-text-fill: green;");
            }
        }
    }

    @FXML
    protected void onAddCostButtonClick() {
        errorLabel.setVisible(false);

        if (Objects.equals(costField.getText(), "")) {
            errorLabel.setVisible(true);
            errorLabel.setText("Ошибка! Введите стоимость.");
            errorLabel.setStyle("-fx-text-fill: red;");
        }
        else {
            Active active = table.getSelectionModel().getSelectedItem();

            String clientMessage = "addCostActive/" + active.getActiveName() + '/' + costField.getText();

            Client client = new Client("0.0.0.0", 2525);

            client.startConnection(clientMessage);
            String serverAnswer = client.getServerAnswer();

            if (serverAnswer.equals("success")) {
                onOpen();
                errorLabel.setVisible(true);
                errorLabel.setText("Котировка актива успешно добавлена!");
                errorLabel.setStyle("-fx-text-fill: green;");
            }
        }
    }

    @FXML
    protected void onExitButtonClick() {
        try {
            SceneLoader.loadScene("View/adminMenuActive.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //endregion
}
