package View;
import Model.*;
import Controller.*;
import DataBase.*;

import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
    public static Stage primaryStage;
    Model model;
    Controller view;
    public static void main(String[] args) {
        launch( args );
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage=primaryStage;
        model = new Model();
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("View.fxml").openStream());
        String current = new java.io.File( "." ).getCanonicalPath();
        Image image = new Image(new FileInputStream(current+"/src/View/VACATION.png"));
        //Setting the image view
        ImageView imageView = new ImageView(image);
        //Setting the position of the image
        //setting the fit height and width of the image view
        imageView.setFitHeight(700);
        imageView.setFitWidth(800);
        addRegister();
        Button register=addRegister();
        Button login =addLogin();
        Button vacations=showVacations();
        Group group= new Group(imageView, root,register,login,vacations );
        Scene scene = new Scene(group, 800, 700);
        scene.getStylesheets().add("/View/MyStyle.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Vacation4U");
        primaryStage.setScene(scene);

        view = fxmlLoader.getController();
        view.setResizeEvent(scene);
        view.setModel(model);
        view.setStage(primaryStage);
        model.addObserver(view);
        model.setController( view );
        view.setUIObjects(imageView,register,login,root,vacations);
        SetStageCloseEvent(primaryStage);
        primaryStage.show();
    }


    private Button addLogin() {
        Button login = new Button("Login");
        login.setOnAction( e -> view.login(  ) );
        login.setLayoutX(635);
        login.setLayoutY(20);
        login.setPrefWidth(150);
        return login;
    }

    private Button addRegister() {
        Button register = new Button("Register");
        register.setOnAction( e -> view.register(  ) );
        register.setLayoutX(480);
        register.setLayoutY(20);
        register.setPrefWidth(150);
        return register;

    }
    private Button showVacations() {
        Button vacations = new Button("Show vacations");
        vacations.setOnAction( e -> view.showVacations(  ) );
        vacations.setLayoutX(280);
        vacations.setLayoutY(250);
        vacations.setPrefWidth(250);
        vacations.setPrefHeight( 50 );
        return vacations;

    }

    private void SetStageCloseEvent(Stage primaryStage) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                windowEvent.consume();
                primaryStage.close();
            }
        });
    }

}