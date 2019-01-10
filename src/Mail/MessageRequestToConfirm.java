package Mail;

import Transactions.Transaction;
import User.*;
import Vacation.Vacation;
import DataBase.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageRequestToConfirm extends Message{

    Vacation vacationToConfirm;
    Vacation vacationToTrade;


    MessageRequestToConfirm(boolean isRead, String savedText, String usernameFrom, String usernameTo, int id) {
        super(isRead, savedText, usernameFrom, usernameTo, id);
        setTextFromSavedText(savedText);
    }

    public MessageRequestToConfirm(Vacation vacation,int id){
        super(id);
        this.vacationToConfirm = vacation;
    }
    public MessageRequestToConfirm(Vacation vacation){
        super();
        this.vacationToConfirm = vacation;
        isRead=false;
    }

    public MessageRequestToConfirm(Vacation vacation, Vacation vacationToTrade) {
        this(vacation);
        this.vacationToTrade=vacationToTrade;
    }

    private MessageConfirmedPurchase.Type accept(){
        if(vacationToConfirm.isAvalible()){
            boolean isCash = vacationToTrade==null;
            double payment = isCash?vacationToConfirm.getPrice():vacationToTrade.getVacationID();
            Transaction transaction = Transaction.createTransaction(getFromUser(),getTo(),vacationToConfirm,payment,isCash);
            if(transaction==null)
                return MessageConfirmedPurchase.Type.UNABLETOCOMPLETEPURCHASE;
            transaction.addToDataBase();
            if(!isCash)
                vacationToTrade.setAvalible(false);
            vacationToConfirm.setAvalible(false);
            return MessageConfirmedPurchase.Type.COMPLETEDTRANSACTION;
        }
        else
            return MessageConfirmedPurchase.Type.FLIGHTNOTAVAILABLE;
    }

    public void confirm(boolean action){
        isRead=true;
        MessageConfirmedPurchase.Type type;
        dbMessages db=new dbMessages();
        db.updateInDataBase( this );
        if (action==true){
            type = accept();
        }
        else {
            type = MessageConfirmedPurchase.Type.USERREGECTED;
        }

        Mailbox sellerMailBox = Mailbox.recreateMailBox(getFromUser());
        Mailbox buyerMailBox = Mailbox.recreateMailBox(getTo());
        Message message = new MessageConfirmedPurchase(vacationToConfirm,type,getUserNameFrom());
        sellerMailBox.sendMessage(message,buyerMailBox.getOwnerUserName());
        buyerMailBox.sendMessage(message,sellerMailBox.getOwnerUserName());
    }



    public String getType(){
        return "request_confirmation";
    }



    @Override
    public String getText() {
        if(isRead && vacationToConfirm.isAvalible()){
            return "You can no longer confirm sale of message. The vacation may no longer be available or you have already responded to this message";
        }
        else if(vacationToTrade==null) {
            return "User: "+getUserNameFrom()+" wants to purchase vacation:" + vacationToConfirm.getVacationID()+"\n" +
                    "Please Contact the user at their email address: " + getFromUser().getEmail() + ".\nPlease Confirm once you have received the money,\nor deny if you are not interested\n"+
                    "Choose your response";
        }
        else{
            return "User: "+getUserNameFrom()+" wants to trade vacation:" + vacationToConfirm.getVacationID()+"for -"+vacationToTrade.getVacationID()+"\n" +
                    "Choose your response";
        }
    }




    public void setTextFromSavedText(String savedText){
        List<String> splitText = new ArrayList<>();
        splitText.addAll(Arrays.asList(savedText.split("\n")));
        String haveResponded = splitText.get(0);
        String vacationKeys = splitText.get(1);
        String tradeVacation = splitText.get(2);
        if(tradeVacation.equals("null"))
            vacationToTrade=null;
        else {
            String[] splitTradeVacation = tradeVacation.split("=");
            vacationToTrade = new Vacation(Integer.parseInt(splitTradeVacation[1]));
        }


        //set vacation
        String[] vacationKeysSplit = vacationKeys.split("\t");
        this.vacationToConfirm = new Vacation(vacationKeysSplit[0],Integer.parseInt(vacationKeysSplit[1]));

        //set have read
        this.isRead = !vacationToConfirm.isAvalible() || isRead;
    }


    public String getTextToSave(){
        String vacation = vacationToConfirm.getSeller()+"\t"+vacationToConfirm.getVacationID();
        String responded = this.isRead ? "t" : "f";
        String trade = vacationToTrade==null?"null":"vacationID="+vacationToTrade.getVacationID();

        return responded+"\n"+vacation+"\n"+trade;
    }
}
