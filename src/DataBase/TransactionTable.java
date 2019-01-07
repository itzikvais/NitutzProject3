package DataBase;

import Transactions.Transaction;
import User.User;
import Vacation.Vacation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionTable extends Adb {

    private String buyerUserName;
    private String sellerUserName;
    private int vacationID;
    private long time;
    private String insertCommand = "INSERT INTO transactions(buyerUserName,sellerUserName,vacationID,time) VALUES(?,?,?,?)";
    public TransactionTable(Transaction transaction){
        buyerUserName = transaction.getBuyer().getUsername();
        sellerUserName = transaction.getSeller().getUsername();
        vacationID = transaction.getVacation().getVacationID();
        //time = transaction.getDateUnixTime();
    }

    public boolean insert(Transaction transaction){
        String sBuyerUserName = transaction.getBuyer().getUsername();
        String sSellerUserName = transaction.getSeller().getUsername();
        int iVacationID = transaction.getVacation().getVacationID();
        java.sql.Date time = java.sql.Date.valueOf(transaction.getTime());
        String insertCommand = "INSERT INTO transactions(buyerUserName,sellerUserName,vacationID,time) VALUES(?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(insertCommand)) {
             pstmt.setString(1,sBuyerUserName);
             pstmt.setString(2,sSellerUserName);
             pstmt.setInt(3,iVacationID);
             pstmt.setDate(4,time);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
    public TransactionTable(){

    }
    public List<Transaction> getAllTransactions(User user){
        String querry = "SELECT * FROM transactions WHERE buyerUserName=? OR sellerUserName=?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(querry)){
            pstmt.setString(1,user.getUsername());
            pstmt.setString(2,user.getUsername());
            //
            ResultSet rs  = pstmt.executeQuery();
            List<Transaction> transactions = getTransactionListFromResultSet(rs);

            return transactions;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private List<Transaction> getTransactionListFromResultSet(ResultSet rs) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        while (rs.next()) {
            String buyerUserName = rs.getString(1);
            String sellerUserName = rs.getString(2);
            int vacationID = rs.getInt(3);
            Transaction transaction = new Transaction(buyerUserName,sellerUserName,vacationID);
            transactions.add(transaction);
        }

        return transactions;
    }
    List<Transaction> getAllBoughtInLastDay(User user){
        long time24HoursAgo = (new Date().getTime())-86400000;
        String querry = "SELECT * FROM transactions WHERE time>?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(querry)){
            pstmt.setLong(1,time24HoursAgo);
            //
            ResultSet rs  = pstmt.executeQuery();
            List<Transaction> transactions = getTransactionListFromResultSet(rs);
            return transactions;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void cancelTransaction(Transaction t){
        String delete = "DELETE FROM transactions WHERE buyerUserName=? AND sellerUserName=? AND vacationID=?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(delete)){
            pstmt.setString(1,t.getBuyer().getUsername());
            pstmt.setString(2,t.getSeller().getUsername());
            pstmt.setInt(3,t.getVacation().getVacationID());
            pstmt.executeUpdate();
            t.getVacation().setAvalible(false);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private List<Vacation> getAllVacations(String userName,ResultSet resultSet) throws SQLException {
        List<Vacation> vacations = new ArrayList<>();
        while (resultSet.next()){
            Vacation vacation = new Vacation(userName,resultSet.getInt("vacationID"));
            vacations.add(vacation);
        }
        return vacations;
    }

    private List<Vacation> getAllVacations(ResultSet resultSet) throws SQLException {
        List<Vacation> vacations = new ArrayList<>();
        while (resultSet.next()){
            Vacation vacation = new Vacation(resultSet.getString("sellerUserName"),resultSet.getInt("vacationID"));
            vacations.add(vacation);
        }
        return vacations;
    }
    public List<Vacation> getAllVacationsSold(String userName){
        String query = "SELECT vacationID FROM transactions WHERE sellerUserName=?";
        try (Connection conn = this.connect();
            PreparedStatement pstmt  = conn.prepareStatement(query)){
            pstmt.setString(1,userName);
            ResultSet resultSet=pstmt.executeQuery();
            return getAllVacations(resultSet);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<Vacation>();
    }

    public List<Vacation> getAllBought(String userName){
        String query = "SELECT vacationID,sellerUserName FROM transactions WHERE buyerUserName=?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt  = conn.prepareStatement(query)){
            pstmt.setString(1,userName);
            ResultSet resultSet=pstmt.executeQuery();
            return getAllVacations(userName,resultSet);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<Vacation>();
    }

    public String getBuyerUserName() {
        return buyerUserName;
    }

    public String getSellerUserNAme() {
        return sellerUserName;
    }

    public int getVacationID() {
        return vacationID;
    }


}
