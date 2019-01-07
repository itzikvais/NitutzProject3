package Controller;

import Mail.Mailbox;
import Mail.Message;
import Mail.MessageRequestToConfirm;
import Model.ModelInt;
import User.User;
import Vacation.Vacation;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.*;
import DataBase.*;

import java.io.IOException;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

import javafx.geometry.Insets;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.beans.value.*;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


public class ShowVacationsController {
    private Parent root;
    public javafx.scene.control.Button closeButton;
    public javafx.scene.control.Button filter;
    public DatePicker departure_datePicker;
    public DatePicker launchBack_datePicker;
    @FXML
    private TextField user_name;
    @FXML
    private TextField pass;
    public String userName="";
    public String password="";
    private Group group=null;
    private int height=70;
    private User user;
    private ShowVacationsController controller;
    private boolean filt;

    public void setMyModel(ModelInt myModel) {
        this.myModel = myModel;
    }

    private ModelInt myModel;

    public Stage getStage() {
        return stage;
    }

    private Stage stage;
    public void filter(ActionEvent actionEvent) {
        filter.setDisable(true);
        try{
            filt=true;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/FilterByDate.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        ShowVacationsController svc=fxmlLoader.getController();
        svc.setController(this);
        Scene scene = new Scene( root1 );
        Stage stage = new Stage();
        stage.setScene( scene );
        stage.initModality( Modality.APPLICATION_MODAL);
        stage.initStyle( StageStyle.UNDECORATED);
        stage.setTitle("filter vacations");
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                windowEvent.consume();
                stage.close();
            }
        });
        stage.show();
    } catch (
    IOException e) {
        e.printStackTrace();
    }
        filter.setDisable(false);
    }

    private void setController(ShowVacationsController svc) {
        controller=svc;
    }

    public void filterByDate(){
        java.sql.Date departure = java.sql.Date.valueOf(departure_datePicker.getValue());
        boolean result =false;
        if(!result){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Yakir learn GUI");
            alert.setContentText("content");
            alert.showAndWait();
            return;
        }

    }
    public void closeButtonAction(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void setVacations(Collection<Vacation> vacations){
        if(filt) {
            height=70;
            group = null;
        }
        for (Vacation v:vacations) {
            addVacation(v);
        }
        ScrollBar sc = new ScrollBar();
        sc.setLayoutX(900 - sc.getWidth());
        sc.setMin(0);
        sc.setLayoutY( 70 );
        sc.setLayoutX( 960 );
        sc.setOrientation(Orientation.VERTICAL);
        sc.setPrefHeight(750);
        sc.setMax(750 );
        sc.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                group.setLayoutY(-new_val.doubleValue());
            }
        });
        if(group!=null) {
            group.getChildren().add( sc );
            Scene scene = new Scene( group );
            stage.setScene( scene );
            stage.setMaxHeight( 800 );
        }
        else if(!filt){
            Scene scene=new Scene( root );
            stage.setScene( scene );
        }
        if(filt){
            filt=false;
            stage.show();
        }

    }
    public void setVacations() {
        VacationTable table = new VacationTable();
        ArrayList<Vacation> vacations=null;
        if(user==null)
            vacations = table.getAllAvailableVacations();
        else
            vacations=table.getAllAvailableVacations(user.getUsername());
        setVacations(vacations);
    }

    private void addVacation(Vacation v) {
        Label parameters=new Label( v.toString() );
        Button vacations = new Button("buy vacation");
        vacations.setOnAction( e->payment(v) );
        Button contact = new Button("trade vacation");
        contact.setOnAction( e->contactSeller(v.getSeller()) );
        HBox hb= new HBox(  );
        hb.setSpacing( 10 );
        hb.setMargin( parameters, new Insets(20, 20, 20, 20) );
        hb.setMargin( vacations, new Insets(0, 0, 0, 0) );
        hb.setMargin( contact, new Insets(0, 0, 0, 0) );
        hb.setLayoutX( 40 );
        hb.setLayoutY( height );
        hb.setPrefWidth( 900 );
        ObservableList list = hb.getChildren();
        list.addAll(parameters,vacations,contact  );
        height+=120;
        if(group==null){
            group=new Group(root, hb );

        }
        else
            group=new Group( group,hb );
        group.getStylesheets().add("/View/MyStyle.css");

    }

    private void contactSeller(String seller) {
        if(myModel.getUser()==null){
            Alert alert=new Alert( Alert.AlertType.WARNING );
            alert.setContentText( "you need to log in before contact seller" );
            alert.showAndWait();
        }
    }

    private void payment(Vacation vacation) {
        if(myModel.getUser()==null){
            Alert alert=new Alert( Alert.AlertType.WARNING );
            alert.setContentText( "you need to log in before purchase" );
            alert.showAndWait();

        }
        else {
            myModel.setVacation( vacation );
            sendRequest();
        }

    }

    public void setStage(Stage stage, Parent root) {
        this.stage=stage;
        this.root=root;
    }

    public void setUser(User user) {
        this.user=user;
    }

    public void filterVacations(ActionEvent actionEvent) {
        String departue="";
        String launchBack="";
        if((departure_datePicker.getValue()!=null)&&(launchBack_datePicker.getValue()!=null)){
            departue=departure_datePicker.getValue().format( DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            launchBack=launchBack_datePicker.getValue().format( DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
            VacationTable db= new VacationTable();
            ArrayList<Vacation> vac= db.selectByDatesWithBackFlights( Date.valueOf(departue),Date.valueOf(launchBack) );
            controller.setVacations( vac );
        }
    }

    public void sendRequest() {
        Mailbox mailbox = Mailbox.recreateMailBox( myModel.getUser() );
        Message message = new MessageRequestToConfirm( myModel.getVacation() );
        mailbox.sendMessage( message, myModel.getVacation().getSeller() );
    }
}
