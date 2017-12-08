package com.example.awais.gtl.Pojos;

/**
 * Created by awais on 12/8/2017.
 */

public class Transaction {
    String transactionDate;
    String accountType;
    String accountTitle;
    long debitBalance;
    long creditBalance;

    public Transaction() {
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public long getDebitBalance() {
        return debitBalance;
    }

    public void setDebitBalance(long debitBalance) {
        this.debitBalance = debitBalance;
    }

    public long getCreditBalance() {
        return creditBalance;
    }

    public void setCreditBalance(long creditBalance) {
        this.creditBalance = creditBalance;
    }
}
