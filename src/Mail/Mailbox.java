package Mail;

import DataBase.dbMessages;
import Transactions.Transaction;
import User.*;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Mailbox {

    private String owner;
    private List<Message> messages;

    public Mailbox(User owner){
        this.owner=owner.getUsername();
        this.messages = new ArrayList<>();
    }

    public List<Message> getMessages(){return messages;}

    public void sendMessage(String userOtherName,String messageText){
        Message toSend = new Message();
        toSend.setText(messageText);
        sendMessage(toSend,userOtherName);
    }

    public void sendMessage(Message toSend,String otherUserName){
        toSend.setUserNameFrom(owner);
        toSend.setUserNameTo(otherUserName);
        toSend.addToDataBase();
    }


    private void addMessage(Message message){
        this.messages.add(0,message);
    }

    //private static Message createFromEntry()

    public static Mailbox recreateMailBox (User user){
        Mailbox mailbox = new Mailbox(user);
        dbMessages db = new dbMessages();
        mailbox.setMessages(db.getAllReceived(user.getUsername()));
        return mailbox;
    }

    public void setMessages(List<Message> allReceived) {
        for (Message m:allReceived
             ) {
            this.messages.add( m );
        }
    }

    public String getOwnerUserName(){return owner;}

    @Override
    public String toString() {
        return this.owner;
    }

    void removeMessage(Message message){
        messages.remove(message);
    }
}
