package com.example.guiex1;

import com.example.guiex1.controller.LogInController;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.domain.UtilizatorValidator;
import com.example.guiex1.repository.Repository;
import com.example.guiex1.repository.dbrepo.UtilizatorDbRepository;
import com.example.guiex1.services.UtilizatorService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    UtilizatorService service;
    @Override
    public void start(Stage primaryStage) throws IOException {

        System.out.println("Reading data from file");
        String username="postgres";
        String pasword="Andra.2002";
        String url="jdbc:postgresql://localhost:5432/socialnetwork";
         Repository<Long, Utilizator> utilizatorRepository =
                new UtilizatorDbRepository(url,username, pasword,  new UtilizatorValidator());

        service = new UtilizatorService(utilizatorRepository);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/LogInView.fxml"));
        Parent root = fxmlLoader.load();
        LogInController ctrl = fxmlLoader.getController();
        ctrl.setService(new UtilizatorService(utilizatorRepository));

        Scene scene = new Scene(root, 550, 600);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
    public static void main(String[] args) {
        launch();
    }

    private void initView(Stage primaryStage) throws IOException {

        // FXMLLoader fxmlLoader = new FXMLLoader();
        //fxmlLoader.setLocation(HelloApplication.class.getResource("com/example/guiex1/views/UtilizatorView.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/LogInView.fxml"));

        BorderPane userLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(userLayout));

        LogInController loginController = fxmlLoader.getController();
        loginController.setService(service);

    }
}


//
//    private void initPrietenieView(Stage primaryStage) throws IOException {
//
//       // FXMLLoader fxmlLoader = new FXMLLoader();
//        //fxmlLoader.setLocation(HelloApplication.class.getResource("com/example/guiex1/views/UtilizatorView.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/PrietenieView.fxml"));
//
//        AnchorPane prietenieLayout = fxmlLoader.load();
//        primaryStage.setScene(new Scene(prietenieLayout));
//
//        PrietenieController prietenieController = fxmlLoader.getController();
//        prietenieController.setPrietenieService(prietenieService);
//
//    }
//}
