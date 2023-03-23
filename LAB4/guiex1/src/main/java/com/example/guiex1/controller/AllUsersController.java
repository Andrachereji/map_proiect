package com.example.guiex1.controller;

import com.example.guiex1.HelloApplication;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.services.NetworkService;
import com.example.guiex1.services.UtilizatorService;
import com.example.guiex1.utils.events.UtilizatorEntityChangeEvent;
import com.example.guiex1.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AllUsersController implements Observer<UtilizatorEntityChangeEvent> {
    public Button backToProfile;
    public Button UtilizatorDetails;
    public TextField textSearchFirstName;
    public TextField textSearchLastName;
    NetworkService service;
    UtilizatorService UtilizatorsService;
    Long UtilizatorID;
    ObservableList<Utilizator> model = FXCollections.observableArrayList();

    @FXML
    TableView<Utilizator> tableViewAllUtilizators;
    @FXML
    TableColumn<Utilizator, String> tableColumnFirstNameAllUtilizators;
    @FXML
    TableColumn<Utilizator, String> tableColumnLastNameAllUtilizators;


    public void setService(NetworkService service, UtilizatorService UtilizatorService, Long UtilizatorID) {
        this.service = service;
        this.UtilizatorsService = UtilizatorService;
        this.UtilizatorID = UtilizatorID;
        UtilizatorService.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        tableColumnFirstNameAllUtilizators.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastNameAllUtilizators.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableViewAllUtilizators.setItems(model);

        textSearchFirstName.textProperty().addListener(o -> handleFilter());
        textSearchLastName.textProperty().addListener(o -> handleFilter());

    }

    private void initModel() {
        Iterable<Utilizator> UtilizatorsI = UtilizatorsService.getAll();
        List<Utilizator> UtilizatorList = StreamSupport.stream(UtilizatorsI.spliterator(), false)
                .collect(Collectors.toList());
        UtilizatorList.removeIf(u -> Objects.equals(u.getId(), UtilizatorID));
        model.setAll(UtilizatorList);
    }


    private void handleFilter() {
        Predicate<Utilizator> p1 = n -> n.getFirstName().startsWith(textSearchFirstName.getText());
        Predicate<Utilizator> p2 = n -> n.getLastName().startsWith(textSearchLastName.getText());


        Iterable<Utilizator> UtilizatorsI = UtilizatorsService.getAll();
        List<Utilizator> UtilizatorList = StreamSupport.stream(UtilizatorsI.spliterator(), false)
                .collect(Collectors.toList());
        UtilizatorList.removeIf(u -> Objects.equals(u.getId(), UtilizatorID));
        model.setAll(UtilizatorList
                .stream()
                .filter(p1.and(p2))
                .collect(Collectors.toList()));
    }


    @Override
    public void update(UtilizatorEntityChangeEvent UtilizatorEvent) {
        initModel();
    }

    public void onBackToProfile(ActionEvent actionEvent) throws IOException {
        this.openAppBack(actionEvent, UtilizatorID);
    }

    private void openAppBack(ActionEvent event, Long id) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/ProfileView.fxml"));
        Parent root = fxmlLoader.load();
        ProfileController ctrl = fxmlLoader.getController();
        ctrl.setService(UtilizatorsService, id);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onUtilizatorDetails(ActionEvent actionEvent) throws IOException {
        Utilizator Utilizator = tableViewAllUtilizators.getSelectionModel().getSelectedItem();
        openAppDetails(actionEvent, Utilizator.getId());

    }

    private void openAppDetails(ActionEvent event, Long id) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/UtilizatorView.fxml"));
        Parent root = fxmlLoader.load();
        UtilizatorController ctrl = fxmlLoader.getController();
        ctrl.setService(UtilizatorsService, id, UtilizatorID);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

