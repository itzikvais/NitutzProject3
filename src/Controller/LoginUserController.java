package Controller;

import Model.Model;
import Model.ModelInt;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import User.*;
public class LoginUserController {
    ModelInt myModel;
    public javafx.scene.control.Button closeButton;
    public javafx.scene.control.TextField txtfld_user_name;
    public javafx.scene.control.TextField txtfld_password;

    /**
     * login to a user if the username and password are matched
     * @param actionEvent
     */
    public void login(ActionEvent actionEvent) {
        boolean success = false;
        String userName=txtfld_user_name.getText();
        String password=txtfld_password.getText();
        User user=null;
        if(checkInputs(userName,password)){
            user=myModel.login(userName,password);
            if(user.checkUser())
                success=true;
        }
        if(success){
            myModel.loginSucces(user);
            Alert result=new Alert( Alert.AlertType.INFORMATION );
            result.setHeaderText( "login succesful" );
            result.showAndWait();
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        }
        else{
            Alert result=new Alert( Alert.AlertType.WARNING );
            result.setTitle( "Failed to login" );
            result.setContentText( "Incorrect username or password." );
            result.showAndWait();
        }

    }

    /**
     * check the user inputs and send an alerts if there are wrong inputs
     * @param userName
     * @param password
     * @return
     */
    private boolean checkInputs(String userName, String password) {
        Alert result=new Alert( Alert.AlertType.WARNING );
        result.setTitle( "wrong input" );
        if(userName.length()<1){
            result.setContentText( "please enter an username" );
            result.showAndWait();
            return false;
        }
        else if(password.length()<1){
            result.setContentText( "please enter a password" );
            result.showAndWait();
            return false;
        }
        return true;
    }
    // close the current stage
    public void closeButtonAction(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    public void setModel(ModelInt model){
        myModel=model;
    }
}
