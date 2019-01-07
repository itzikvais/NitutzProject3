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
    public boolean haveResponded=false;


    MessageRequestToConfirm(boolean isRead, String savedText, String usernameFrom, String usernameTo, int id) {
        super(isRead, savedText, usernameFrom, usernameTo, id);
        setTextFromSavedText(savedText);
    }

    public MessageRequestToConfirm(Vacation vacation,int id){
        super(id);
        this.vacationToConfirm = vacation;
        haveResponded=false;
    }
    public MessageRequestToConfirm(Vacation vacation){
        super();
        this.vacationToConfirm = vacation;
        haveResponded=false;
    }

    private MessageConfirmedPurchase.Type accept(){
        if(vacationToConfirm.isAvalible()){
            Transaction transaction = Transaction.createTransaction(getFromUser(),getTo(),vacationToConfirm);
            if(transaction==null)
                return MessageConfirmedPurchase.Type.UNABLETOCOMPLETEPURCHASE;
            transaction.addToDataBase();
            vacationToConfirm.setAvalible(false);
            return MessageConfirmedPurchase.Type.COMPLETEDTRANSACTION;
        }
        else
            return MessageConfirmedPurchase.Type.FLIGHTNOTAVAILABLE;
    }

    public void confirm(boolean action){
        System.out.println("check");
        isRead=true;
        haveResponded = true;
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
        if(haveResponded && vacationToConfirm.isAvalible()){
            return "You can no longer confirm sale of message. The vacation may no longer be available or you have already responded to this message";
        }
        else {
            return "User: "+getUserNameFrom()+" wants to purchase vacation:" + vacationToConfirm.getVacationID()+"\n" +
                    "Choose your response";
        }
    }




    public void setTextFromSavedText(String savedText){
        List<String> splitText = new ArrayList<>();
        splitText.addAll(Arrays.asList(savedText.split("\n")));
        String haveResponded = splitText.remove(0);
        String vacationKeys = splitText.remove(splitText.size()-1);


        //set vacation
        String[] vacationKeysSplit = vacationKeys.split("\t");
        this.vacationToConfirm = new Vacation(vacationKeysSplit[0],Integer.parseInt(vacationKeysSplit[1]));

        //set have read
        this.haveResponded = vacationToConfirm.isAvalible() && haveResponded.equals("t");
    }

    public boolean haveResponded(){
        haveResponded = vacationToConfirm.isAvalible() && haveResponded;
        return haveResponded;
    }

    public String getTextToSave(){
        String vacation = vacationToConfirm.getSeller()+"\t"+vacationToConfirm.getVacationID();
        String responded = this.haveResponded ? "t" : "f";

        return responded+"\n"+vacation;
    }
}
