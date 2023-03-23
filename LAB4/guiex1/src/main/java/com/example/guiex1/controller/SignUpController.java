package com.example.guiex1.controller;

import com.example.guiex1.HelloApplication;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.domain.ValidationException;
import com.example.guiex1.services.UtilizatorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SignUpController {
    public TextField fieldSignUpFirstName;
    public TextField fieldSignUpLastName;
    public PasswordField passwordSignUp;
    public TextField fieldSignUpEmail;
    public PasswordField confirmPasswordSignUp;
    public Button createAnAccount;
    public Button backToLogIn;
    public Label statusLogIn;
    UtilizatorService service;
    private Stage stage;
    private Scene scene;
    private Long currentId;
    private Utilizator currentUser;


    public void setService(UtilizatorService service) {
        this.service = service;
    }

    public void onCreateAnAccountButtonClick(ActionEvent event) throws IOException {
        String firstName = fieldSignUpFirstName.getText();
        String lastName = fieldSignUpLastName.getText();
        String email = fieldSignUpEmail.getText();
        String password = passwordSignUp.getText();
        String confirmPassword = confirmPasswordSignUp.getText();
        if (!Objects.equals(password, confirmPassword)) {
            statusLogIn.setText("the passwords need to be the same");
        }else
            try {
                currentUser = new Utilizator(firstName, lastName, email, password);
                service.addUtilizator(currentUser);
                this.openApp(event);
            } catch (ValidationException | IllegalArgumentException validationException) {
                statusLogIn.setText(validationException.getMessage());
            }
    }

    private void openApp(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/ProfileView.fxml"));
        Parent root = fxmlLoader.load();
        ProfileController ctrl = fxmlLoader.getController();
        Iterable<Utilizator> users = service.getAll();
        for (Utilizator user : users) {
            if (Objects.equals(user.getEmail(), currentUser.getEmail())) {
                currentId = user.getId();
            }

        }
        ctrl.setService(service, currentId);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onBackToLogInButtonClick(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/LogInView.fxml"));
        Parent root = fxmlLoader.load();
        LogInController ctrl = fxmlLoader.getController();
        ctrl.setService(service);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
