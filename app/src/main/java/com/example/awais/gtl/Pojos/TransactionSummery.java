package com.example.awais.gtl.Pojos;

/**
 * Created by awais on 12/8/2017.
 */

public class TransactionSummery {
    long cashDebit;
    long cashCredit;
    long salesDebit;
    long salesCredit;
    long accountsReceivableDebit;
    long accountsReceivableCredit;
    long accountPayableCredit;
    long accountPayableDebit;

    public TransactionSummery() {
    }

    public long getCashDebit() {
        return cashDebit;
    }

    public void setCashDebit(long cashDebit) {
        this.cashDebit = cashDebit;
    }

    public long getCashCredit() {
        return cashCredit;
    }

    public void setCashCredit(long cashCredit) {
        this.cashCredit = cashCredit;
    }

    public long getSalesDebit() {
        return salesDebit;
    }

    public void setSalesDebit(long salesDebit) {
        this.salesDebit = salesDebit;
    }

    public long getSalesCredit() {
        return salesCredit;
    }

    public void setSalesCredit(long salesCredit) {
        this.salesCredit = salesCredit;
    }

    public long getAccountsReceivableDebit() {
        return accountsReceivableDebit;
    }

    public void setAccountsReceivableDebit(long accountsReceivableDebit) {
        this.accountsReceivableDebit = accountsReceivableDebit;
    }

    public long getAccountsReceivableCredit() {
        return accountsReceivableCredit;
    }

    public void setAccountsReceivableCredit(long accountsReceivableCredit) {
        this.accountsReceivableCredit = accountsReceivableCredit;
    }

    public long getAccountPayableCredit() {
        return accountPayableCredit;
    }

    public void setAccountPayableCredit(long accountPayableCredit) {
        this.accountPayableCredit = accountPayableCredit;
    }

    public long getAccountPayableDebit() {
        return accountPayableDebit;
    }

    public void setAccountPayableDebit(long accountPayableDebit) {
        this.accountPayableDebit = accountPayableDebit;
    }
}
