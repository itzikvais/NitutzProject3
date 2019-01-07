package Controller;

import Model.Model;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

import User.*;
import Model.*;


public class Controller implements Observer {
    public javafx.scene.control.Button closeButton;
    public boolean isConnected;
    private Button logout;
    private Button myVacations;
    private Button addVacation;
    private Button profile;
    private Button mailbox;
    private Button showVacations;
    private Button register;
    private Button login;
    private Stage primaryStage;
    private Parent root;
    private ModelInt myModel;
    private ImageView image;
    public Controller(){
        createButtons();
    }

    private void createButtons() {
        logout = new Button("Logout");
        logout.setOnAction( e -> logout(  ) );
        logout.setLayoutX(630);
        logout.setLayoutY(20);
        logout.setPrefWidth(150);
        myVacations=new Button( "my vacations" );
        myVacations.setOnAction( e->myVacations() );
        myVacations.setLayoutX(280);
        myVacations.setLayoutY(330);
        myVacations.setPrefWidth(250);
        myVacations.setPrefHeight( 50 );
        addVacation=new Button( "add new vacation" );
        addVacation.setOnAction( e -> addVacation() );
        addVacation.setLayoutX(280);
        addVacation.setLayoutY(170);
        addVacation.setPrefWidth(250);
        addVacation.setPrefHeight( 50 );
        profile=new Button( "my profile" );
        profile.setOnAction( e -> updateUser(  ) );
        profile.setLayoutX(480);
        profile.setLayoutY(20);
        mailbox=new Button( "my mailbox" );
        mailbox.setOnAction( e->getMailBox() );
        mailbox.setLayoutX(330);
        mailbox.setLayoutY(20);
    }

    private void myVacations() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/MyVacation.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            MyVacationsController vacationsController=fxmlLoader.getController();
            Stage stage = new Stage();
            stage.initModality( Modality.APPLICATION_MODAL);
            stage.initStyle( StageStyle.UNDECORATED);
            stage.setTitle("my vacations");
            vacationsController.setStage(stage,root1);
            vacationsController.setVacations(myModel.getUser().getUsername());
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent windowEvent) {
                    windowEvent.consume();
                    stage.close();
                }
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addVacation() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/AddVacation.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality( Modality.APPLICATION_MODAL);
            stage.initStyle( StageStyle.UNDECORATED);
            stage.setTitle("Add vacation");
            AddVacationController view = fxmlLoader.getController();
            view.setUser( myModel.getUser( ));
            //view.setModel(myModel);
            stage.setScene(new Scene(root1));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent windowEvent) {
                    windowEvent.consume();
                    stage.close();
                }
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        isConnected=false;
        myModel.setUser(null);
        Group group=new Group( image,root,register,login,showVacations );
        Scene scene = new Scene(group, 800, 700);
        scene.getStylesheets().add("/View/MyStyle.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setResizeEvent(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if(isConnected){
            Label label = new Label("Hello " + myModel.getUser().getName());
            label.setLayoutX(230);
            label.setLayoutY(20);
            Group group=new Group(image, root,label,logout,showVacations,addVacation,myVacations,profile,mailbox );
            Scene scene = new Scene(group, 800, 700);
            scene.getStylesheets().add("/View/MyStyle.css");
            primaryStage.setScene(scene);
            primaryStage.show();

        }

    }
    public void login(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Login.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality( Modality.APPLICATION_MODAL);
            stage.initStyle( StageStyle.UNDECORATED);
            stage.setTitle("Login");
            LoginUserController view = fxmlLoader.getController();
            view.setModel(myModel);
            stage.setScene(new Scene(root1));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent windowEvent) {
                    windowEvent.consume();
                    stage.close();
                }
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void register(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/CreateUser.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality( Modality.APPLICATION_MODAL);
            stage.initStyle( StageStyle.UNDECORATED);
            stage.setTitle("Register");
            stage.setScene(new Scene(root1));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent windowEvent) {
                    windowEvent.consume();
                    stage.close();
                }
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void updateUser(){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/UpdateUser.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            UpdateUserController update=fxmlLoader.getController();
            update.setText( myModel.getUser( ));
            update.setModel( myModel );
            Stage stage = new Stage();
            stage.initModality( Modality.APPLICATION_MODAL);
            stage.initStyle( StageStyle.UNDECORATED);
            stage.setTitle("update profile");
            stage.setScene(new Scene(root1));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent windowEvent) {
                    windowEvent.consume();
                    stage.close();
                }
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showVacations() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/ShowVacations.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            ShowVacationsController vacationsController=fxmlLoader.getController();
            if(isConnected)
                vacationsController.setUser(myModel.getUser());
            vacationsController.setMyModel( myModel );
            Stage stage = new Stage();
            stage.initModality( Modality.APPLICATION_MODAL);
            stage.initStyle( StageStyle.UNDECORATED);
            stage.setTitle("show vacations");
            vacationsController.setStage(stage,root1);
            vacationsController.setVacations();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent windowEvent) {
                    windowEvent.consume();
                    stage.close();
                }
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUIObjects(ImageView image,Button register, Button login, Parent root, Button vacations) {
        this.image=image;
        this.register=register;
        this.login=login;
        this.root=root;
        showVacations=vacations;
    }

    public void setModel(Model model) {
        myModel=model;
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage=primaryStage;
    }

    public void exit(ActionEvent actionEvent) {
        showExitMessage();
    }
    private void showExitMessage(){
        Alert exit = myModel.getExitMessage();
        Optional<ButtonType> result = exit.showAndWait();

    }
    public void closeButtonAction(ActionEvent actionEvent) {
        //Stage stage = (Stage) closeButton.getScene().getWindow();
        primaryStage.close();
    }

    public void getMailBox() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/MailBox.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            MailboxController mbc=new MailboxController();
            mbc.setParent(root1);
            mbc.setUserName(myModel.getUser().getUsername());
            Stage stage = new Stage();
            mbc.setStage(stage,root1);
            stage.initModality( Modality.APPLICATION_MODAL);
            stage.initStyle( StageStyle.UNDECORATED);
            stage.setTitle("my mailbox");
            mbc.setMessages();
            if(mbc.isEmpty)
                return;
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent windowEvent) {
                    windowEvent.consume();
                    stage.close();
                }
            });
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
