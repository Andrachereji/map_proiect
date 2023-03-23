package com.example.guiex1.controller;

import com.example.guiex1.HelloApplication;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.services.UtilizatorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LogInController {
    @FXML
    TextField fieldLogInEmail;
    @FXML
    PasswordField fieldLogInPassword;

    @FXML
    Button buttonLogIn;
    @FXML
    Button buttonSignUp;

    @FXML
    Label logIn;

    UtilizatorService service;
    private Stage stage;
    private Scene scene;
    private Long currentId;

    public LogInController() {
    }

    @FXML
    protected void onLogInButtonClick(ActionEvent event) throws IOException {

        String email = fieldLogInEmail.getText();
        String passwordText = fieldLogInPassword.getText();
        Iterable<Utilizator> users = service.getAll();

        for (Utilizator user : users) {
            if (Objects.equals(user.getEmail(), email) && Objects.equals(user.getPassword(), passwordText)) {
                currentId = user.getId();
                this.openApp(event);
            }

        }

    }

    private void openApp(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/ProfileView.fxml"));
        Parent root = fxmlLoader.load();
        ProfileController ctrl = fxmlLoader.getController();
        ctrl.setService(service, currentId);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setService(UtilizatorService service) {
        this.service = service;
    }

    public void onSignUpButtonClick(ActionEvent event) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/SignUpView.fxml"));
        Parent root = fxmlLoader.load();
        SignUpController ctrl = fxmlLoader.getController();
        ctrl.setService(service);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

