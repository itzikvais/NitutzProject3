package DataBase;

import Mail.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class dbMessages extends Adb {

    public boolean insertToDataBase(Message message){
        int messageID = message.getId();
        int isRead = message.isRead() ? 1 : 0;
        String messageType = message.getType();
        String usernameFrom = message.getUserNameFrom();
        String userNameTo = message.getTo().getUsername();
        String messageText = message.getTextToSave();
        String insertCommand = "INSERT INTO messages(messageID,isRead,messageType," +
                "userNameFrom,userNameTo,messageText) VALUES(?,?,?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(insertCommand)) {
            pstmt.setInt(1,messageID);
            pstmt.setInt(2,isRead);
            pstmt.setString(3,messageType);
            pstmt.setString(4,usernameFrom);
            pstmt.setString(5,userNameTo);
            pstmt.setString(6,messageText);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean updateInDataBase(Message message){
        int messageID = message.getId();
        int isRead = message.isRead() ? 1 : 0;
        String messageType = message.getType();
        String usernameFrom = message.getUserNameFrom();
        String userNameTo = message.getTo().getUsername();
        String messageText = message.getTextToSave();
        String insertCommand = "UPDATE messages SET isRead=?,messageType=?,userNameFrom=?,userNameTo=?,messageText=? " +
                "WHERE messageID=?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(insertCommand)) {
            pstmt.setInt(1,isRead);
            pstmt.setString(2,messageType);
            pstmt.setString(3,usernameFrom);
            pstmt.setString(4,userNameTo);
            pstmt.setString(5,messageText);
            pstmt.setInt(6,messageID);
            pstmt.executeUpdate();
            System.out.println("check");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public List<Message> getAllReceived(String userNameReceived){
        String query = "SELECT messageID,isRead,messageType,userNameFrom," +
                "userNameTo,messageText FROM messages WHERE usernameTo=?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1,userNameReceived);
            ResultSet rs = pstmt.executeQuery();
            List<Message> toReturn = new ArrayList<>();
            while (rs.next()){
                Message message = Message.CreateMessage(rs.getInt(2),rs.getString(6),rs.getString(4),rs.getString(5),rs.getInt(1),rs.getString(3));
                if(message!=null) {
                    toReturn.add( message );
                }
            }
            return toReturn;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }





}
