package com.example.myapp.Controllers;

import com.example.myapp.Client;
import com.example.myapp.Operation;
import com.example.myapp.SceneLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class AdminMenuActiveOperationsController {

    //FXML stuff

    //region Fields
    @FXML
    private TableView<Operation> table;

    @FXML
    private TableColumn tc1;

    @FXML
    private TableColumn tc2;

    @FXML
    private TableColumn tc3;

    @FXML
    private TableColumn tc4;

    @FXML
    private TableColumn tc5;

    @FXML
    private TableColumn tc6;

    @FXML
    private ComboBox filter;

    @FXML
    private ComboBox sort;

    @FXML
    private Label errorLabel;

    ObservableList<String> typesFilter = FXCollections.observableArrayList("Показать успешные", "Показать отклоненные", "Показать покупки", "Показать продажи");

    ObservableList<String> typesSort = FXCollections.observableArrayList("По дате", "По пользователям");
    //endregion

    //region onMethods
    @FXML
    public void initialize() {

        errorLabel.setVisible(false);
        filter.setItems(typesFilter);
        sort.setItems(typesSort);
    }

    @FXML
    protected void onOpen() {
        errorLabel.setVisible(false);

        tc1.setCellValueFactory(new PropertyValueFactory<Operation, String>("userName"));
        tc2.setCellValueFactory(new PropertyValueFactory<Operation, String>("activeName"));
        tc3.setCellValueFactory(new PropertyValueFactory<Operation, Integer>("amount"));
        tc4.setCellValueFactory(new PropertyValueFactory<Operation, String>("operationType"));
        tc5.setCellValueFactory(new PropertyValueFactory<Operation, String>("isSuccess"));
        tc6.setCellValueFactory(new PropertyValueFactory<Operation, String>("date"));

        String clientMessage = "viewOperations/0";

        Client client = new Client("0.0.0.0", 2525);

        client.startConnection(clientMessage);

        table.setItems(client.getOperations());
    }

    @FXML
    protected void onFilterButtonClick() {

        String clientMessage = "viewOperations/";

        Object value = filter.getValue();

        switch ((String) value){
            case "Показать успешные" -> { clientMessage += "1";}
            case "Показать отклоненные" -> { clientMessage += "2";}
            case "Показать покупки" -> { clientMessage += "3";}
            case "Показать продажи" -> { clientMessage += "4"; }
        }

        Client client = new Client("0.0.0.0", 2525);

        client.startConnection(clientMessage);

        table.setItems(client.getOperations());
    }

    @FXML
    protected void onSortButtonClick() {
        String clientMessage = "viewOperations/";

        Object value = filter.getValue();

        clientMessage += (value.equals("По дате"))?"5":"6";

        Client client = new Client("0.0.0.0", 2525);

        client.startConnection(clientMessage);

        table.setItems(client.getOperations());
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
