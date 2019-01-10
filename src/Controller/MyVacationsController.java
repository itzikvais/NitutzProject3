package Controller;

import DataBase.VacationTable;
import Mail.Mailbox;
import Mail.Message;
import Mail.MessageRequestToConfirm;
import Vacation.Vacation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import Model.*;
import java.util.ArrayList;
import java.util.Collection;

public class MyVacationsController {
    public javafx.scene.control.Button closeButton;
    public javafx.scene.control.Button filter;
    public DatePicker departure_datePicker;
    private Group group=null;
    private int height=70;
    private Parent root;
    private boolean toTrade;
    private Vacation offeredVacation;
    private Button buy;
    private Button trade;

    /**
     * set the controler model
     * @param myModel
     */
    public void setMyModel(ModelInt myModel) {
        this.myModel = myModel;
    }

    private ModelInt myModel;

    public Stage getStage() {
        return stage;
    }

    private Stage stage;
    // close the current stage
    public void closeButtonAction() {
        if(buy!=null&&trade!=null) {
            buy.setDisable( false );
            trade.setDisable( false );
        }
        stage.close();
    }

    /**
     * show the user vacations
     * @param vacations a collection of all user vacations
     */
    public void setVacations(Collection<Vacation> vacations){
        for (Vacation v:vacations) {
            addVacation(v);
        }
        ScrollBar sc = new ScrollBar();
        sc.setLayoutX(900 - sc.getWidth());
        sc.setMin(0);
        sc.setLayoutY( 70 );
        sc.setLayoutX( 960 );
        sc.setOrientation( Orientation.VERTICAL);
        sc.setPrefHeight(750);
        sc.setMax(750 );
        sc.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                group.setLayoutY(-new_val.doubleValue());
            }
        });
        if(group!=null) {
            ScrollPane sp= new ScrollPane(  );
            Button exit=getExitButton();
            group=new Group( group,exit );
            group.getStylesheets().add("/View/MyStyle.css");
            sp.setContent( group );
            Scene scene = new Scene( sp );
            stage.setScene( scene );
        }
        else{
            Scene scene=new Scene( root );
            stage.setScene( scene );
        }


    }
    // create an exit button
    private Button getExitButton() {
        Button Exit = new Button( "exit" );
        Exit.setOnAction( e->closeButtonAction() );
        return Exit;
    }

    /**
     * get the available user vacations from the DB
     * @param username
     */
    public void setVacations(String username) {
        VacationTable table = new VacationTable();
        ArrayList<Vacation> vacations = table.getMyVacations( username );
        setVacations(vacations);
    }
    // add a specific vacation to the group
    private void addVacation(Vacation v) {
        Label parameters=new Label( v.toString() );
        Button chooseToTrade=new Button( "choose vacation" );
        chooseToTrade.setOnAction( e->tradeVacations(v) );
        HBox hb= new HBox(  );
        hb.setSpacing( 10 );
        hb.setMargin( parameters, new Insets(20, 20, 20, 20) );
        hb.setLayoutX( 40 );
        hb.setLayoutY( height );
        hb.setPrefWidth( 750 );
        ObservableList list = hb.getChildren();
        if(toTrade)
            list.addAll(parameters,chooseToTrade );
        else
            list.addAll(parameters );
        height+=120;
        if(group==null){
            group=new Group(root, hb );
        }
        else
            group=new Group( group,hb );
        group.getStylesheets().add("/View/MyStyle.css");

    }

    /**
     * send the current offer to the model and sen a message to the seller
     * @param trade
     */
    private void tradeVacations(Vacation trade) {
        myModel.setVacationToTrade(trade);
        Mailbox mailbox = Mailbox.recreateMailBox( myModel.getUser() );
        Message message = new MessageRequestToConfirm( myModel.getVacation(),myModel.getVacationToTrade() );
        mailbox.sendMessage( message, myModel.getVacation().getSeller() );
        Alert alert=new Alert( Alert.AlertType.INFORMATION );
        alert.setContentText( "your request has been sent to the seller "+ "\n"+ "check your mailbox for an answer" );
        alert.showAndWait();
        stage.close();
    }
    //set the current stage
    public void setStage(Stage stage,Parent root) {
        this.stage=stage;
        this.root=root;
    }

    /**
     * set a vacation to trade
     * @param b
     * @param v
     */
    public void setToTrade(boolean b,Vacation v ){
        this.toTrade=b;
        this.offeredVacation=v;
    }
    //set a buttons if this is a trade window
    public void setButtons(Button buy, Button trade) {
        this.buy=buy;
        this.trade =trade;
    }
}
