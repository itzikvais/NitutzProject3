package Controller;

import Mail.Mailbox;
import Mail.Message;
import Mail.MessageRequestToConfirm;
import User.User;
import Vacation.Vacation;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MailboxController {

    private Parent root;
    private Group group=null;
    private int height=70;
    private Parent p;

    public Stage getStage() {
        return stage;
    }

    private Stage stage;
    public javafx.scene.control.Button closeButton;
    public javafx.scene.control.Button reloadButton;
    private Mailbox mailbox;
    private String userName;

    public MailboxController(){
        //this.userName = userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public void closeButtonAction() {
        stage.close();
    }
    public void setMessages(){
        mailbox = Mailbox.recreateMailBox(new User(userName));
        setMessages(mailbox.getMessages());
    }
    public void setMessages(Collection<Message> messages){
        if(p==null && (messages==null || messages.isEmpty()))
            return;
        for (Message m:messages) {
            addMessage(m);
        }
        Scene scene;
        if(group!=null) {
            ScrollPane sp= new ScrollPane(  );
            Button exit=getExitButton();
            group=new Group( group,exit );
            group.getStylesheets().add("/View/MyStyle.css");
            sp.setContent(group);
            sp.setVbarPolicy(ScrollBarPolicy.ALWAYS);
            scene = new Scene(sp,1000,800);

        }
        else {
            ScrollPane sp= new ScrollPane(  );
            Button exit=getExitButton();
            group=new Group(exit);
            group.getStylesheets().add("/View/MyStyle.css");
            scene = new Scene(group );
        }
        stage.setScene( scene );
    }

    private Button getExitButton() {
        Button Exit = new Button( "exit" );
        Exit.setOnAction( e->closeButtonAction() );
        return Exit;
    }

    public void setParent(Parent p){
        this.p=p;
    }

    private void addMessage(Message m) {
        javafx.scene.control.Label parameters=new javafx.scene.control.Label( m.getText() );
        javafx.scene.control.Button accept = new javafx.scene.control.Button("Accept Purchase");
        javafx.scene.control.Button deny = new Button("Deny Purchase");
        accept.setOnAction(e->{
            accept.setDisable(true);
            deny.setDisable(true);
            handlePress(true, (MessageRequestToConfirm) m);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("You chose to accept the sale of the vacation");
            alert.setHeaderText("Check your mail-box for confirmation");
            alert.setContentText("");
            alert.showAndWait();
        });
        deny.setOnAction(e->{
            accept.setDisable(true);
            deny.setDisable(true);
            handlePress(false, (MessageRequestToConfirm) m);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("You chose to deny the sale of the vacation");
            alert.setHeaderText("");
            alert.setContentText("");
            alert.showAndWait();
        });
        HBox hb=null;
        if(m instanceof MessageRequestToConfirm&&!m.isRead() ){
            hb= new HBox( parameters,accept,deny );
        }
        else if(!(m instanceof MessageRequestToConfirm)) {
            hb= new HBox( parameters);
        }
        if(hb!=null) {
            hb.setSpacing( 10 );
            hb.setMargin( parameters, new javafx.geometry.Insets( 20, 20, 20, 20 ) );
            hb.setMargin( accept, new javafx.geometry.Insets( 0, 0, 0, 0 ) );
            hb.setMargin( deny, new Insets( 0, 0, 0, 0 ) );
            hb.setLayoutX( 40 );
            hb.setLayoutY( height );
            hb.setPrefWidth( 900 );
            height += 120;
            if (group == null) {
                group = new Group( hb );
            } else
                group = new Group( group, hb );
        }
    }

    private void handlePress(boolean b, MessageRequestToConfirm m) {
        m.confirm(b);
    }

    public void reload(){
        mailbox = Mailbox.recreateMailBox(new User(userName));
        this.group=null;
        setMessages(mailbox.getMessages());
    }
    public void setStage(Stage stage) {
        this.stage=stage;
    }
}
