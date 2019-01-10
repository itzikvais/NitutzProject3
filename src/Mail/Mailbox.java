package Mail;

import DataBase.dbMessages;
import Transactions.Transaction;
import User.*;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * represents a mailbox
 */
public class Mailbox {

    /**
     * owner username
     */
    private String owner;
    /**
     * messages in mailbox
     */
    private List<Message> messages;

    /**
     * Constructor for the malibox
     * @param owner owner of mailbox
     */
    public Mailbox(User owner){
        this.owner=owner.getUsername();
        this.messages = new ArrayList<>();
    }

    /**
     * get messages
     * @return
     */
    public List<Message> getMessages(){return messages;}

    /**
     * send chat message
     * @param userOtherName other
     * @param messageText chat message
     */
    public void sendMessage(String userOtherName,String messageText){
        Message toSend = new Message();
        toSend.setText(messageText);
        sendMessage(toSend,userOtherName);
    }

    /**
     * Send message
     * @param toSend  message
     * @param otherUserName user to send message to
     */
    public void sendMessage(Message toSend,String otherUserName){
        toSend.setUserNameFrom(owner);
        toSend.setUserNameTo(otherUserName);
        toSend.addToDataBase();
    }


    private void addMessage(Message message){
        this.messages.add(0,message);
    }

    //private static Message createFromEntry()

    /**
     * creates mailbox from database
     * @param user to recreate mailbox of
     * @return user's mailbox
     */
    public static Mailbox recreateMailBox (User user){
        Mailbox mailbox = new Mailbox(user);
        dbMessages db = new dbMessages();
        mailbox.setMessages(db.getAllReceived(user.getUsername()));
        return mailbox;
    }

    /**
     * sets messages for mailbox
     * @param allReceived
     */
    public void setMessages(List<Message> allReceived) {
        for (Message m:allReceived
             ) {
            this.messages.add( m );
        }
    }

    /**
     * get username
     * @return
     */
    public String getOwnerUserName(){return owner;}

    @Override
    public String toString() {
        return this.owner;
    }

    void removeMessage(Message message){
        messages.remove(message);
    }
}
