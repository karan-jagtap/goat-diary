package com.aarushsystems.goatdiary.model.reports;

import android.util.Log;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportMilkModel implements Comparable<ReportMilkModel> {

    private String date;
    private float quantity, totalQuantity;
    private int action;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public float getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(float totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ReportMilkModel)) {
            return false;
        }
        ReportMilkModel rm = (ReportMilkModel) obj;
        Log.i("CUSTOM","equals = "+this.date+" - "+rm.getDate());
        return this.date.equals(rm.getDate());
    }

    @Override
    public int compareTo(ReportMilkModel o) {
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
