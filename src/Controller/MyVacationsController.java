package Controller;

import DataBase.VacationTable;
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

import java.util.ArrayList;
import java.util.Collection;

public class MyVacationsController {
    private Parent root;
    public javafx.scene.control.Button closeButton;
    public javafx.scene.control.Button filter;
    public DatePicker departure_datePicker;
    private Group group=null;
    private int height=70;

    public Stage getStage() {
        return stage;
    }

    private Stage stage;

    public void closeButtonAction(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

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
            group.getChildren().add( sc );
            Scene scene = new Scene( group );
            stage.setScene( scene );
        }
        else{
            Scene scene=new Scene( root );
            stage.setScene( scene );
        }


    }
    public void setVacations(String username) {
        VacationTable table = new VacationTable();
        ArrayList<Vacation> vacations = table.getMyVacations( username );
        setVacations(vacations);
    }

    private void addVacation(Vacation v) {
        Label parameters=new Label( v.toString() );
        HBox hb= new HBox(  );
        hb.setSpacing( 10 );
        hb.setMargin( parameters, new Insets(20, 20, 20, 20) );
        hb.setLayoutX( 40 );
        hb.setLayoutY( height );
        hb.setPrefWidth( 750 );
        ObservableList list = hb.getChildren();
        list.addAll(parameters );
        height+=120;
        if(group==null){
            group=new Group(root, hb );
        }
        else
            group=new Group( group,hb );
        group.getStylesheets().add("/View/MyStyle.css");

    }

    public void setStage(Stage stage, Parent root) {
        this.stage=stage;
        this.root=root;
    }
}
