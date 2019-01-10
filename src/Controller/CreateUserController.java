package Controller;


import DataBase.DataBase;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import Model.*;
import User.*;
import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.time.format.DateTimeFormatter;

public class CreateUserController {

    ModelInt myModel = new Model();
    //DataBaseController dataBaseController;
    private boolean created=false;

    public javafx.scene.control.Button closeButton;
    public javafx.scene.control.TextField txtfld_user_name;
    public javafx.scene.control.TextField txtfld_password;
    public javafx.scene.control.TextField txtfld_email;
    public javafx.scene.control.TextField txtfld_first_name;
    public javafx.scene.control.TextField txtfld_last_name;
    public javafx.scene.control.DatePicker BD;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * create a new user with the user inputs
     * @param actionEvent
     */
    public void createUser(ActionEvent actionEvent) {
        //initialize all objects to be used later
        boolean success = false;
        Alert showResult = null;
        User toCreate = null;

        //get all values
        String userName=txtfld_user_name.getText();
        String password=txtfld_password.getText();
        String email= txtfld_email.getText();
        String date="";
        if(BD.getValue()!=null)
            date=BD.getValue().format( DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String firstName = txtfld_first_name.getText();
        String lastName = txtfld_last_name.getText();
        //check values are valid
        if(checkInputs(userName,password,email,date,firstName,lastName)) {
            try {
                success = myModel.createUser( userName, password, email, date, firstName, lastName );
            } catch (Exception e) {
                success = false;
            }
        }
        if(success){
            txtfld_user_name.clear();
            txtfld_first_name.clear();
            txtfld_last_name.clear();
            txtfld_password.clear();
            txtfld_email.clear();
            BD.setValue(null);
            Alert result=new Alert( Alert.AlertType.INFORMATION );
            result.setTitle( "User created" );
            result.setContentText( "User created succesfuly" );
            result.showAndWait();
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        }
        else{
            Alert result=new Alert( Alert.AlertType.WARNING );
            result.setTitle( "Failed to create user" );
            result.setContentText( "Username allready exist" );
        }


    }

    /**
     * check the user inserted inputs and send an alert if there are some wrong input
     * @param userName
     * @param password
     * @param email
     * @param date
     * @param firstName
     * @param lastName
     * @return
     */
    private boolean checkInputs(String userName, String password, String email, String date, String firstName, String lastName) {
        Alert result=new Alert( Alert.AlertType.WARNING );
        result.setTitle( "wrong input" );
        if(userName.length()<1){
            result.setContentText( "please enter an username" );
            result.showAndWait();
            return false;
        }
        else if(password.length()<4){
            result.setContentText( "password must contain at least 4 characters" );
            result.showAndWait();
            return false;
        }
        else if(!validateEmail( email )){
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
     * check an email input
     * @param email
     * @return
     */
    private boolean validateEmail(String email){
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    /**
     * close the current stage
     * @param actionEvent
     */
    public void closeButtonAction(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
