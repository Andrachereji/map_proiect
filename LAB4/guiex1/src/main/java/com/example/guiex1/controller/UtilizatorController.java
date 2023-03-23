package com.example.guiex1.controller;

import com.example.guiex1.HelloApplication;
import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.domain.PrietenieValidator;
import com.example.guiex1.domain.UtilizatorValidator;
import com.example.guiex1.repository.Repository;
import com.example.guiex1.repository.dbrepo.PrietenieDbRepository;
import com.example.guiex1.repository.dbrepo.UtilizatorDbRepository;
import com.example.guiex1.services.PrietenieService;
import com.example.guiex1.services.NetworkService;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class UtilizatorController {
    public Label profileFirstName;

    public Label profileSecondName;
    public Label profileMail;
    public Button backToProfile;
    public Button friendStatus;
    public ImageView profileImage;

    Long userID;
    Long openProfileId;
    UtilizatorService service;

    Repository<Long, Utilizator> userRepository = new UtilizatorDbRepository(Constants.url, Constants.username,
            Constants.password, new UtilizatorValidator());
    Repository<Long, Prietenie> friendshipRepository = new PrietenieDbRepository(Constants.url, Constants.username,
            Constants.password, new PrietenieValidator());
    PrietenieService friendshipsService = new PrietenieService(friendshipRepository, userRepository);
    NetworkService worldService = new NetworkService(friendshipRepository, userRepository);

    public void setService(UtilizatorService service, Long openProfileId, Long user) {
        this.service = service;
        this.openProfileId = openProfileId;
        this.userID = user;
        setData();
    }


    @FXML
    private void setData() {
        service.findOne(openProfileId).ifPresent(theUser -> profileFirstName.setText(theUser.getFirstName()));
        service.findOne(openProfileId).ifPresent(theUser -> profileSecondName.setText(theUser.getLastName()));
        service.findOne(openProfileId).ifPresent(theUser -> profileMail.setText(theUser.getEmail()));

        int friend = worldService.fridshipE(userID, openProfileId);

        if (friend == 1)
            friendStatus.setText("delete friend");
        else {
            if (friend == 0) {
                    friendStatus.setText("add friend");

            }
        }
    }


    public void onBackToProfile(ActionEvent actionEvent) throws IOException {
        this.openApp(actionEvent);
    }

    private void openApp(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/AllUserView.fxml"));
        Parent root = fxmlLoader.load();
        AllUsersController ctrl = fxmlLoader.getController();
        ctrl.setService(new
                        NetworkService(friendshipRepository, userRepository),
                new UtilizatorService(userRepository), userID);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onStatus(ActionEvent actionEvent) {
        String text = friendStatus.getText();

        if (text.equals("delete friend")) {
            friendStatus.setText("add friend");
            this.friendshipsService.deletePrietenie(userID, openProfileId);
        }
    }
}