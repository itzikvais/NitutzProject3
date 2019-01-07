
package Transactions;

import DataBase.*;
import User.*;
import Vacation.Vacation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Transaction {


//    private Payment payment;
    private User buyer;
    private User seller;
    private Vacation vacation;
    private LocalDate time;
    private boolean isInDB = false;
    protected double payment;


    public static Transaction createTransaction(User buyer, User seller, Vacation vacation, double payment, boolean isCash){
        Transaction transaction = null;
        if(isCash)
            transaction = new Transaction(buyer,seller,vacation,payment);
        else
            transaction = new TradeTransaction(buyer,seller,vacation,payment);
        vacation.setAvalible(false);
        return transaction;
    }

    public Transaction(User buyer, User seller, Vacation vacation, LocalDate time, boolean isInDB) {
        this.buyer = buyer;
        this.seller = seller;
        this.vacation = vacation;
        this.time = time;
        this.isInDB = isInDB;
    }

    protected Transaction(User buyer, User seller, Vacation vacation, double payment){
        this.buyer=buyer;
        this.seller=seller;
        this.vacation=vacation;
        this.payment=payment;
        time = LocalDate.now();

    }

    public void addToDataBase(){
        if(!isInDB) {
            TransactionTable tb = new TransactionTable();
            tb.insert(this);
            isInDB = true;
        }
    }

    public void removeFromDataBase(){
        if(isInDB){
            TransactionTable tb = new TransactionTable();
            tb.cancelTransaction(this);
            isInDB = false;
        }
    }

    public Transaction(String buyerUserName,String sellerUserName, int vacationID){
        TransactionTable transactionTableEntry = new TransactionTable();
        String querry = "SELECT buyerUserName,sellerUserName,vacationID,time FROM transactions WHERE buyerUserName=? AND sellerUserName=? AND vacationID=?";
        try (Connection conn = transactionTableEntry.connect();
             PreparedStatement pstmt  = conn.prepareStatement(querry)){
             pstmt.setString(1,buyerUserName);
             pstmt.setString(2,sellerUserName);
             pstmt.setInt(3,vacationID);
            //
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                this.vacation = new Vacation(sellerUserName,vacationID);
                this.seller = new User(sellerUserName);
                this.buyer = new User(buyerUserName);
                //this.dateUnixTime = rs.getLong(4);
                this.isInDB=true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public User getBuyer() {
        return buyer;
    }

    public User getSeller() {
        return seller;
    }

    public Vacation getVacation() {
        return vacation;
    }

   public LocalDate getTime(){return time;}

    public String toString(){
        return "Transaction: "+this.buyer.getUsername()+" bought vacation:"+this.vacation.getVacationID()+" from "+this.seller.getUsername();
    }


    public boolean isInDB(){return isInDB;}

    public String getPayment(){
        return "cash="+payment;
    }
}
