package Mail;

import User.User;
import Vacation.Vacation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageConfirmedPurchase extends Message{

    private Vacation vacation;
    private String seller;
    private String buyer;

    MessageConfirmedPurchase(boolean isRead, String savedText, String usernameFrom, String usernameTo, int id) {
        super(isRead, savedText, usernameFrom, usernameTo, id);
        setTextFromSavedText(savedText);
    }

    public MessageConfirmedPurchase(Vacation vacation, Type type, String buyer) {
        super();
        this.vacation=vacation;
        this.buyer=buyer;
        this.seller=(vacation.getSeller());
        setText(type);
    }

    public enum Type{
        COMPLETEDTRANSACTION, FLIGHTNOTAVAILABLE,UNABLETOCOMPLETEPURCHASE, USERREGECTED
    }


    public String getType(){
        return "confirmation_message";
    }
    private void setText(Type type){
        String text=null;
        switch (type){
            case USERREGECTED:
                text="purchase of vacation: "+vacation.getVacationID()+" has been rejected by user: "+seller;
                break;
            case COMPLETEDTRANSACTION:
                text = "purchase of vacation: "+vacation.getVacationID()+" has been completed succesfully. " +
                        "User: "+buyer+" successfully purchased vacation from User: "+seller;
                break;
            case FLIGHTNOTAVAILABLE:
                text = "purchase of vacation has not been completed successfully, because flight is no longer available";
                break;
            case UNABLETOCOMPLETEPURCHASE:
                text = "purchase of vacation has not been completed successfully, because of a problem with the payment";
                break;
        }
        setText(text);
    }

    public void setTextFromSavedText(String savedText){
        List<String> savedTextSplit = new ArrayList<>();
        savedTextSplit.addAll(Arrays.asList(savedText.split("\n")));
        seller = (savedTextSplit.remove(0));
        buyer = (savedTextSplit.remove(0));
        String vacationKeys = savedTextSplit.remove(0);
        String[] vacationKeysSplit = vacationKeys.split("\t");
        if(vacationKeysSplit.length>=2)
            vacation = new Vacation(vacationKeysSplit[0],Integer.parseInt(vacationKeysSplit[1]));
        String text = "";
        for (int i = 0; i < savedTextSplit.size(); i++) {
            text+=savedTextSplit.get(i);
        }
        setText(text);
    }
    public String getTextToSave(){
        List<String> text = new ArrayList<>();
        text.add(seller);
        text.add(buyer);
        text.add(vacation.getSeller()+"\t"+vacation.getVacationID());
        text.add(getText());
        String toReturn = "";
        for (int i = 0; i < text.size(); i++) {
            toReturn+=text.get(i)+'\n';
        }
        toReturn = toReturn.substring(0,toReturn.length()-1);
        return toReturn;
    }
}
