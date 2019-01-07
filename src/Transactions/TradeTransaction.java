package Transactions;

import User.User;
import Vacation.Vacation;

public class TradeTransaction extends Transaction {
    public TradeTransaction(User buyer, User seller, Vacation vacation, double payment) {
        super(buyer,seller,vacation,payment);
    }

    @Override
    public String getPayment() {
        return "trade="+(int)payment;
    }
}
