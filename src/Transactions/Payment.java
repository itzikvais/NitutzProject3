package Transactions;
public class Payment {

    String from;
    String to;
    double amount;

    public static Payment createPayment(String from, String to, double amount){
        PaypalInterface paypalInterface = PaypalInterface.getInstance();

        if (paypalInterface.makePayment(from,to, amount)){
            return new Payment(from,to,amount);
        }

        return null;
    }

    private Payment(String from, String to, double amount){
        this.from=from;
        this.to = to;
        this.amount = amount;
    }
}
