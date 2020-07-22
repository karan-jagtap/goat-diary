package com.aarushsystems.goatdiary.model;

import java.io.Serializable;

public class IncomeModel implements Serializable, Comparable<IncomeModel> {

    private int srno, head, chequeNo;
    private String date;
    private String amount;
    private String cash_cheque;
    private String bank;
    private String remarks;
    private String receivedFrom;

    public IncomeModel() {
    }

    public String getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
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
    public int compareTo(IncomeModel incomeModel) {
        return ((Integer) this.getSrno()).compareTo(incomeModel.getSrno());
    }
}
