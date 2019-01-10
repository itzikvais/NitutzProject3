package Mail;

import User.User;
import DataBase.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * message of mailbox
 */
public class Message {

    /**
     * whether or not responded to message or not
     */
    protected boolean isRead;
    /**
     * message name
     */
    protected String text;
    /**
     * username of user whose mailbox sent message
     */
    private String userNameFrom;
    /**
     * username of user who received message
     */
    private String userNameTo;
    /**
     * message id
     */
    private int id;


    protected Message(String from, String to){
        this.userNameFrom = from;
        this.userNameTo = to;
        dbMessages mdb = new dbMessages();
        this.id = (Math.random()*Integer.MAX_VALUE+ "").hashCode();
    }

    /**
     * message id
     * @param id
     */
    Message(int id){
        this.id=id;
    }

    Message(){
        dbMessages mdb = new dbMessages();
        this.id = (Math.random()*Integer.MAX_VALUE+ "").hashCode();
    }

    /**
     * true if responded to message false otherwise
     * @return
     */
    public boolean isRead() {
        return isRead;
    }

    /**
     * get message text
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * set message text
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * user who sent message
     * @return
     */
    public User getFromUser() {
        return new User(userNameFrom);
    }

    /**
     * username of user who sent message
     * @return
     */
    public String getUserNameFrom(){return userNameFrom;}

    /**
     * set user who sent message
     * @param from
     */
    public void setFrom(User from) {
        this.userNameFrom = from.getUsername();
    }

    /**
     * set username of user who sent message
     * @param userNameFrom
     */
    public void setUserNameFrom(String userNameFrom){this.userNameFrom=userNameFrom;}

    /**
     * get user who received message
     * @return
     */
    public User getTo() {
        return new User(userNameTo);
    }

    public String getUserNameTo(){return userNameTo;}

    /**
     * set user who received message
     * @param to
     */
    public void setTo(User to) {
        this.userNameTo = to.getUsername();
    }

    /**
     * set username of user who received message
     * @param userNameTo
     */
    public void setUserNameTo(String userNameTo) {
        this.userNameTo = userNameTo;
    }

    /**
     *
     * @return message type
     */
    public String getType(){
        return "chat";
    }

    /**
     * add messsage to database
     */
    protected void addToDataBase(){
        dbTableUsers db = new dbTableUsers();
        Connection connection = db.connect();
        try{
            String sql = "INSERT INTO messages " +
                    "(messageID,isREad,messageType,userNameFrom,userNameTo,messageText) " +
                    "Values(?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            int isRead = this.isRead==true ? 1 : 0;
            preparedStatement.setInt(1,getId());
            preparedStatement.setInt(2,isRead);
            preparedStatement.setString(3,getType());
            preparedStatement.setString(4,userNameFrom);
            preparedStatement.setString(5,userNameTo);
            preparedStatement.setString(6,getTextToSave());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * get text to save in database
     * @return
     */
    public String getTextToSave(){
        return getText();
    }

    /**
     *
     * @return message id
     */
    public int getId(){
        return id;
    }

    /**
     * recreate message from textbox
     * @param isRead
     * @param savedText
     * @param usernameFrom
     * @param usernameTo
     * @param id
     * @param type
     * @return
     */
   public static Message CreateMessage(int isRead, String savedText, String usernameFrom, String usernameTo, int id, String type) {
        boolean bIsRead = isRead==1 ? true : false;
        if(type.equals("chat"))
            return new Message(bIsRead,savedText,usernameFrom,usernameTo,id);
        else if(type.equals("request_confirmation")) {
            return new MessageRequestToConfirm( bIsRead, savedText, usernameFrom, usernameTo, id );
        }
        else if(type.equals("confirmation_message"))
            return new MessageConfirmedPurchase(bIsRead,savedText,usernameFrom,usernameTo,id);
        return null;
    }

    /**
     * constructor for message, to be used when recreating
     * @param isRead
     * @param savedText
     * @param usernameFrom
     * @param usernameTo
     * @param id
     */
    protected Message(boolean isRead, String savedText, String usernameFrom, String usernameTo, int id){
        this.isRead = isRead;
        this.text=savedText;
        this.userNameFrom = usernameFrom;
        this.userNameTo = usernameTo;
        this.id = id;
    }

    /**
     * set the text and other paramaters of message from saved text
     * @param savedText
     */
    protected void setTextFromSavedText(String savedText){
        this.text=savedText;
    }

    /**
     * returns true if other object is message and with the same id
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(obj!=null && obj instanceof Message){
            Message object = (Message)obj;
            return this.id == object.id;
        }
        return false;
    }


    public String toString(){
        return "Message "+getType()+" ,ID:"+getId()+" Text: "+getText();
    }
}
