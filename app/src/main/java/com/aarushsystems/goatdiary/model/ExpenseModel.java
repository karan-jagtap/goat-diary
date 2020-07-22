package com.aarushsystems.goatdiary.model;

import java.io.Serializable;

public class ExpenseModel implements Serializable, Comparable<ExpenseModel> {

    private int srno, head, chequeNo;
    private String date;
    private String amount;
    private String cash_cheque;
    private String bank;
    private String remarks;

    public String getPaidTo() {
        return paidTo;
    }

    public void setPaidTo(String paidTo) {
        this.paidTo = paidTo;
    }

    private String paidTo;


    public ExpenseModel() {
    }

    public int getSrno() {
        return srno;
    }

    public void setSrno(int srno) {
        this.srno = srno;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(int chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCash_cheque() {
        return cash_cheque;
    }

    public void setCash_cheque(String cash_cheque) {
        this.cash_cheque = cash_cheque;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public int compareTo(ExpenseModel expenseModel) {
        return ((Integer) this.getSrno()).compareTo(expenseModel.getSrno());
    }
}
