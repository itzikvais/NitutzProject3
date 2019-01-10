package Controller;

import Vacation.Vacation;
import javafx.event.ActionEvent;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.stage.*;
import User.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddVacationController {

    public javafx.scene.control.Button closeButton;
    public javafx.scene.control.TextField aviation;
    public javafx.scene.control.DatePicker depTime;
    public javafx.scene.control.DatePicker launchTime;
    public javafx.scene.control.DatePicker backDepTime;
    public javafx.scene.control.DatePicker backLaunchTime;
    public javafx.scene.control.TextField bagSize;
    public javafx.scene.control.TextField tickets;
    public javafx.scene.control.TextField sourceCountry;
    public javafx.scene.control.TextField destCountry;
    public javafx.scene.control.TextField ticketType;
    public javafx.scene.control.TextField price;
    private User user;
    //closing the current stage
    public void closeButtonAction(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * set the current login user
      */
    public void setUser(User user){
        this.user=user;
    }

    /**
     * check the vacation input and create new vacation if the inputs are valid
     * @param actionEvent
     */
    public void addVacation(ActionEvent actionEvent) {
        String username=user.getUsername();
        String aviationComp=aviation.getText();
        String departureTime="";
        if(depTime.getValue()!=null) {
            departureTime = depTime.getValue().format( DateTimeFormatter.ofPattern( "yyyy-MM-dd" ) );
        }
        String launTime="";
        if(launchTime.getValue()!=null)
            launTime=launchTime.getValue().format( DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String backDepartureTime="";
        if(backDepTime.getValue()!=null)
            backDepartureTime=backDepTime.getValue().format( DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String backLaunch="";
        if(backLaunchTime.getValue()!=null)
            backLaunch=backLaunchTime.getValue().format( DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String bag=bagSize.getText();
        String ticket=tickets.getText();
        String source=sourceCountry.getText();
        String destination=destCountry.getText();
        String type=ticketType.getText();
        String amount=price.getText();
        if(aviationComp.length()<1||departureTime.length()<1||launTime.length()<1||bag.length()<1||ticket.length()<1||source.length()<1||destination.length()<1
        ||type.length()<1||amount.length()<1||!isInteger( bag )||!isInteger( ticket )||!isIDouble( amount )||backDepartureTime.length()<1||backLaunch.length()<1){
            Alert result=new Alert( Alert.AlertType.WARNING );
            result.setTitle( "wrong inputs" );
            result.setContentText( "one or more of your inputs are incorect" );
            result.showAndWait();
        }
        else{
            Vacation vac= new Vacation( username,aviationComp,Date.valueOf( departureTime ),Date.valueOf( launTime ),Date.valueOf(backDepartureTime),Date.valueOf(backLaunch),Integer.parseInt( bag ),Integer.parseInt( ticket ),source,destination,type,Double.parseDouble( amount ) );
            Alert alert=new Alert( Alert.AlertType.INFORMATION );
            alert.setContentText( "Vacation added succesfuly" );
            alert.showAndWait();
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * @param num
     * @return true if a string is a number
     */
    private boolean isInteger(String num){
        try{
            Integer.parseInt( num );
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
    /**
     * @return true if a string is a time
     */
    private boolean isTime(String time){
        try {
            String number="";
            if(time.length()>5||!time.contains( ":" ))
                return false;
            for (int i = 0; i < time.length(); i++) {
                if(time.charAt( i )!=':')
                    number+=time.charAt( i );
                else{
                    int num=Integer.parseInt( number );
                    if(num<0||num>23)
                        return false;
                    number="";
                }
            }
            int num=Integer.parseInt( number );
            if(num<0||num>59)
                return false;
        }
        catch(Exception e){
            return false;
        }
        return true;
    }
    /**
     * @return true if a string is a double
     */
    private boolean isIDouble(String num){
        try{
            Double.parseDouble( num );
        }
        catch (Exception e){
            return false;
        }
        return true;
    }
}
