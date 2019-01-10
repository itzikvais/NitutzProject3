package Controller;

import Model.ModelInt;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import User.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Model.*;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class UpdateUserController {
    public javafx.scene.control.Button closeButton;
    public javafx.scene.control.TextField txtfld_oldPass;
    public javafx.scene.control.TextField txtfld_password;
    public javafx.scene.control.TextField txtfld_email;
    public javafx.scene.control.TextField txtfld_first_name;
    public javafx.scene.control.TextField txtfld_last_name;
    public javafx.scene.control.DatePicker BD;
    private ModelInt model;
    private User user;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public void closeButtonAction(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    public void setModel(ModelInt model) {
        this.model = model;
    }
    //set the current logged in user
    public void setUser(User user){
        this.user=user;
    }

    /**
     * get the updated user parameters
     * @param user
     */
    public void setText(User user){
        this.user=user;
        txtfld_email.setText( user.getEmail() );
        txtfld_first_name.setText( user.getName() );
        txtfld_last_name.setText( user.getLastname() );
        String date=user.getBirthDath();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        BD.setValue( localDate );
    }
    public void updateUser(ActionEvent actionEvent) {
        String email= txtfld_email.getText();
        String date="";
        if(BD.getValue()!=null)
            date=BD.getValue().format( DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String firstName = txtfld_first_name.getText();
        String lastName = txtfld_last_name.getText();
        if(checkInputs( email,date,firstName,lastName )) {
            if(model.updateUser( user, email, date, firstName, lastName )) {
                Alert result = new Alert( Alert.AlertType.INFORMATION );
                result.setContentText( "The user information has been updated" );
                result.showAndWait();
            }
        }

    }
    //open an update password view
    public void updatePassword(ActionEvent actionEvent) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/UpdatePassword.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            UpdateUserController update=fxmlLoader.getController();
            update.setUser( user );
            update.setModel( model );
            Stage stage = new Stage();
            stage.initModality( Modality.APPLICATION_MODAL);
            stage.initStyle( StageStyle.UNDECORATED);
            stage.setTitle("update password");
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
    // check the user inputs
    private boolean checkInputs(String email, String date, String firstName, String lastName) {
        Alert result=new Alert( Alert.AlertType.WARNING );
        result.setTitle( "wrong input" );
        if(!validateEmail( email )){
            result.setContentText( "wrong email input" );
            result.showAndWait();
            return false;
        }
        else if(firstName.length()<1){
            result.setContentText( "please enter a first name" );
            result.showAndWait();
            return false;
        }
        else if(lastName.length()<1){
            result.setContentText( "please enter a last name" );
            result.showAndWait();
            return false;
        }
        else if(date.length()<1){
            result.setContentText( "please enter a date" );
            result.showAndWait();
            return false;
        }
        return true;
    }

    /**
     *  validate email input
      */
    private boolean validateEmail(String email){
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    /**
     * update a password and check user inputs
     * @param actionEvent
     */
    public void updatePas(ActionEvent actionEvent) {
        String oldPass=txtfld_oldPass.getText();
        String newPass=txtfld_password.getText();
        if(oldPass.length()<1){
            Alert result= new Alert( Alert.AlertType.WARNING );
            result.setContentText( "please enter an old password" );
            result.showAndWait();
        }
        else if(newPass.length()<4){
            Alert result= new Alert( Alert.AlertType.WARNING );
            result.setContentText( "a password must contain at least 4 characters!" );
            result.showAndWait();
        }
        else {
            if (model.updatePassword( user, oldPass, newPass )) {
                Alert result = new Alert( Alert.AlertType.INFORMATION );
                result.setContentText( "The user password has been updated" );
                result.showAndWait();
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.close();
            }
            else{
                Alert result= new Alert( Alert.AlertType.WARNING );
                result.setContentText( "wrong old password" );
                result.showAndWait();
            }
        }
    }
}
