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
    // set the controler model
    public void setMyModel(ModelInt myModel) {
        this.myModel = myModel;
    }

    private ModelInt myModel;

    public Stage getStage() {
        return stage;
    }

    private Stage stage;

    /**
     * filter the vacations by departure time
     * @param actionEvent
     */
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

    /**
     * set the controler who created this controle
      */
    private void setController(ShowVacationsController svc) {
        controller=svc;
    }
    // close the specific window
    public void closeButtonAction(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * show all the choosed vacations
     * @param vacations
     */
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

    /**
     * get all the available vacations
     */
    public void setVacations() {
        VacationTable table = new VacationTable();
        ArrayList<Vacation> vacations=null;
        if(user==null)
            vacations = table.getAllAvailableVacations();
        else
            vacations=table.getAllAvailableVacations(user.getUsername());
        setVacations(vacations);
    }

    /**
     * add a specific vacation to the vacations group
     * @param v
     */
    private void addVacation(Vacation v) {
        Label parameters=new Label( v.toString() );
        Button vacations = new Button("buy vacation");
        Button contact = new Button("trade vacation");
        vacations.setOnAction( e->{
            vacations.setDisable( true );
            contact.setDisable( true );
            payment(v);
        } );
        contact.setOnAction( e->{
            vacations.setDisable( true );
            contact.setDisable( true );
            tradeVacations(v,vacations,contact);
        } );
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

    /**
     * open a myvacation after pushing the trade button
     * @param vacation
     * @param buy
     * @param trade
     * @return
     */
    private MyVacationsController tradeVacations(Vacation vacation, Button buy, Button trade) {
        if(myModel.getUser()==null){
            Alert alert=new Alert( Alert.AlertType.WARNING );
            alert.setContentText( "you need to log in before contact seller" );
            alert.showAndWait();
        }
        else{
            try{
                myModel.setVacation( vacation );
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/MyVacation.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                MyVacationsController vacationsController=fxmlLoader.getController();
                Stage stage = new Stage();
                stage.initModality( Modality.APPLICATION_MODAL);
                stage.initStyle( StageStyle.UNDECORATED);
                stage.setTitle("my vacations");
                vacationsController.setMyModel( myModel );
                vacationsController.setToTrade(true,vacation);
                vacationsController.setStage(stage,root1);
                vacationsController.setVacations(myModel.getUser().getUsername());
                vacationsController.setButtons(buy,trade);
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent windowEvent) {
                        windowEvent.consume();
                        stage.close();
                    }
                });
                stage.show();
                return vacationsController;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    /**nt if a connected user pushed the buy vacation button
     * create a payme
     * @param vacation
     */
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

    /**
     * set the current loged in user
     * @param user
     */
    public void setUser(User user) {
        this.user=user;
    }

    /**
     * filter the vacations by date
     * @param actionEvent
     */
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
    // send a purchase offer to the seller
    public void sendRequest() {
        Mailbox mailbox = Mailbox.recreateMailBox( myModel.getUser() );
        Message message = new MessageRequestToConfirm( myModel.getVacation() );
        mailbox.sendMessage( message, myModel.getVacation().getSeller() );
        Alert alert=new Alert( Alert.AlertType.INFORMATION );
        alert.setContentText( "your request has been sent to the seller "+ "\n"+ "check your mailbox for an answer" );
        alert.showAndWait();
        stage.close();
    }
}
