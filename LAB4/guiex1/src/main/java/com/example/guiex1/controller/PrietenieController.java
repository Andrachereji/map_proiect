package com.example.guiex1.controller;

import com.example.guiex1.HelloApplication;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.services.PrietenieService;;
import com.example.guiex1.services.UtilizatorService;
import com.example.guiex1.utils.events.PrietenieEntityChangeEvent;
import com.example.guiex1.utils.observer.Observer;
import com.example.guiex1.services.NetworkService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrietenieController implements Observer<PrietenieEntityChangeEvent> {
    public Button backToProfile;
    public Button message;
    NetworkService networkService;
    PrietenieService friendshipsService;
    ObservableList<Utilizator> model = FXCollections.observableArrayList();

    Long UtilizatorID;

    @FXML
    TableView<Utilizator> tableView;
    @FXML
    TableColumn<Utilizator, String> tableColumnFirstName;
    @FXML
    TableColumn<Utilizator, String> tableColumnLastName;
    private UtilizatorService UsersService;

    public void setService(UtilizatorService UtilizatorsService, NetworkService service, PrietenieService friendshipsService, Long Utilizator) {
        this.UsersService = UtilizatorsService;
        this.networkService = service;
        this.friendshipsService = friendshipsService;
        this.UtilizatorID = Utilizator;
        friendshipsService.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableView.setItems(model);
    }

    private void initModel() {
        Map<Utilizator, LocalDate> friendshipsOfAnUtilizator = networkService.friendshipsOfAnUser(UtilizatorID);
        List<Utilizator> keys = new ArrayList<>(friendshipsOfAnUtilizator.keySet());
        model.setAll(keys);
    }

    @Override
    public void update(PrietenieEntityChangeEvent friendshipEvent) {
        initModel();
    }

    public void handleDeleteFriend(ActionEvent actionEvent) {
        Utilizator Utilizator = tableView.getSelectionModel().getSelectedItem();

        if (Utilizator != null) {
            this.friendshipsService.deletePrietenie(UtilizatorID, Utilizator.getId());
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete",
                    "The friendship has been deleted");
        } else {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete",
                    "You haven't selected anything!");
        }
    }

    public void onBackToProfile(ActionEvent actionEvent) throws IOException {
        this.openApp(actionEvent);
    }

    private void openApp(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/ProfileView.fxml"));
        Parent root = fxmlLoader.load();
        ProfileController ctrl = fxmlLoader.getController();
        ctrl.setService(UsersService, UtilizatorID);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
