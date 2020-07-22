package com.aarushsystems.goatdiary.model.reports;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportIncomeModel implements Comparable<ReportIncomeModel> {

    private String date, cashCheque, receivedFrom, head;
    private float amount;
    private int itemType;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCashCheque() {
        return cashCheque;
    }

    public void setCashCheque(String cashCheque) {
        this.cashCheque = cashCheque;
    }

    public String getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int compareTo(ReportIncomeModel o) {
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = sf.parse(this.date);
            Date odate = sf.parse(o.getDate());
            return date.compareTo(odate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
