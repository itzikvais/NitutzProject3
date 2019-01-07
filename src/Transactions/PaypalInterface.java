package Transactions;

public class PaypalInterface {

    private static PaypalInterface instance;

    private PaypalInterface(){

    }

    public static PaypalInterface getInstance(){
        if(instance==null)
            instance = new PaypalInterface();
        return instance;
    }

    public boolean makePayment(String userNameFrom, String userNameTo, double amount){
        return true;
    }

}
