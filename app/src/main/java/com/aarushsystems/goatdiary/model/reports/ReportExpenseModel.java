package com.aarushsystems.goatdiary.model.reports;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportExpenseModel implements Comparable<ReportExpenseModel> {

    private String date, cashCheque, paidTo, head;
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

    public String getPaidTo() {
        return paidTo;
    }

    public void setPaidTo(String paidTo) {
        this.paidTo = paidTo;
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
    public int compareTo(ReportExpenseModel o) {
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
