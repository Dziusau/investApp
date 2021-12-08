package com.example.myapp.Controllers;

import com.example.myapp.Client;
import com.example.myapp.SceneLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class RegistrationController {

    //FXML stuff

    //region Fields
    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> accessType;

    @FXML
    private TextField emailField;

    @FXML
    private Label emailLabel;

    @FXML
    private Label errorLabel;

    ObservableList<String> types = FXCollections.observableArrayList("Пользователь", "Администратор");
    //endregion

    //region onMethods
    @FXML
    public void initialize() {
        errorLabel.setVisible(false);

        accessType.setValue("Пользователь");
        accessType.setItems(types);
    }

    @FXML
    protected void onTypeAccessSet(){
        Object value = accessType.getValue();
        emailLabel.setVisible("Пользователь".equals(value));
        emailField.setVisible("Пользователь".equals(value));
    }

    @FXML
    protected void onRegisterButtonClick() {
        String clientMessage = "reg/";
        Object value = accessType.getValue();
        if ("Пользователь".equals(value)) {
            clientMessage += "1" + '/' +
                    nameField.getText() + '/' +
                    passwordField.getText() + '/' +
                    emailField.getText();
        }
        else {
            clientMessage += "2" + '/' +
                    nameField.getText() + '/' +
                    passwordField.getText();
        }

        Client client = new Client("0.0.0.0", 2525);

        client.startConnection(clientMessage);
        String serverAnswer = client.getServerAnswer();

        if (serverAnswer.equals("success")){
            try {
                SceneLoader.setRole("Пользователь".equals(value)?1:2);
                SceneLoader.setName(
                        (SceneLoader.getRole() == 1)?emailField.getText():nameField.getText()
                );
                SceneLoader.loadScene(
                        (SceneLoader.getRole() == 1)?"View/userMenu.fxml":"View/adminMenu.fxml"
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            errorLabel.setVisible(true);
            errorLabel.setText("Ошибка регистрации! Такой пользователь уже есть.");
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
