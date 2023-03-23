package com.example.guiex1.controller;

import com.example.guiex1.HelloApplication;
import com.example.guiex1.domain.*;
import com.example.guiex1.domain.PrietenieValidator;
import com.example.guiex1.domain.UtilizatorValidator;
import com.example.guiex1.repository.Repository;
import com.example.guiex1.repository.dbrepo.PrietenieDbRepository;
import com.example.guiex1.repository.dbrepo.UtilizatorDbRepository;
import com.example.guiex1.services.NetworkService;
import com.example.guiex1.services.PrietenieService;
import com.example.guiex1.services.UtilizatorService;
import com.example.guiex1.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class ProfileController {

    @FXML
    Button signOut;
    @FXML
    Button profileMyFriends;
    @FXML
    Button profileAllUsers;
    @FXML
    Button profileRequests;
    @FXML
    ImageView profileImage;
    @FXML
    Label profileFirstName;
    @FXML
    Label profileSecondName;
    @FXML
    Label profileMail;

    Long userID;
    UtilizatorService service;
    private Stage stage;
    private Scene scene;

    Repository<Long, Utilizator> userRepository = new UtilizatorDbRepository(Constants.url, Constants.username,
            Constants.password, new UtilizatorValidator());
    Repository<Long, Prietenie> friendshipRepository = new PrietenieDbRepository(Constants.url, Constants.username,
            Constants.password, new PrietenieValidator());

    public void setService(UtilizatorService service, Long user) {
        this.service = service;
        this.userID = user;
        setData();
    }

    @FXML
    private void initialize() {
    }

    @FXML
    private void setData() {
        service.findOne(userID).ifPresent(theUser -> profileFirstName.setText(theUser.getFirstName()));
        service.findOne(userID).ifPresent(theUser -> profileSecondName.setText(theUser.getLastName()));
        service.findOne(userID).ifPresent(theUser -> profileMail.setText(theUser.getEmail()));
    }

    public void onMyFriendsButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/PrietenieView.fxml"));
        Parent root = fxmlLoader.load();
        PrietenieController ctrl = fxmlLoader.getController();
        ctrl.setService(new UtilizatorService(userRepository), new
                        NetworkService(friendshipRepository, userRepository),
                new PrietenieService(friendshipRepository, userRepository), userID);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void onAllUsersButton(ActionEvent event) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/AllUserView.fxml"));
        Parent root = fxmlLoader.load();
        AllUsersController ctrl = fxmlLoader.getController();
        ctrl.setService(new
                        NetworkService(friendshipRepository, userRepository),
                new UtilizatorService(userRepository), userID);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onSignOut(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/LogInView.fxml"));
        Parent root = fxmlLoader.load();
        LogInController ctrl = fxmlLoader.getController();
        ctrl.setService(service);
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 550, 600);
        stage.setScene(scene);
        stage.show();
    }
}
