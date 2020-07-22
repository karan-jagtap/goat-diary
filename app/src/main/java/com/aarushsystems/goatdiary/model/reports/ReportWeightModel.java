package com.aarushsystems.goatdiary.model.reports;

import android.util.Log;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportWeightModel implements Comparable<ReportWeightModel>{
    private int tagId;
    private String firstDate, date, lastDate, animalType, breed;
    private float firstWeight, weight, lastWeight, perChange, weightGainPerDay, totalWeight;

    public float getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(float totalWeight) {
        this.totalWeight = totalWeight;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(String firstDate) {
        this.firstDate = firstDate;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public float getFirstWeight() {
        return firstWeight;
    }

    public void setFirstWeight(float firstWeight) {
        this.firstWeight = firstWeight;
    }

    public float getLastWeight() {
        return lastWeight;
    }

    public void setLastWeight(float lastWeight) {
        this.lastWeight = lastWeight;
    }

    public float getPerChange() {
        return perChange;
    }

    public void setPerChange(float perChange) {
        this.perChange = perChange;
    }

    public float getWeightGainPerDay() {
        return weightGainPerDay;
    }

    public void setWeightGainPerDay(float weightGainPerDay) {
        this.weightGainPerDay = weightGainPerDay;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ReportWeightModel)) {
            return false;
        }
        ReportWeightModel rm = (ReportWeightModel) obj;
        /*if (action.equals("SELECT")) {
            return true;
        }*/
        if (this.tagId == rm.getTagId() &&
                this.lastDate.equals(rm.getLastDate()) &&
                this.weight == rm.getWeight()) {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(ReportWeightModel o) {
        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            if (o.getDate()!=null && this.date!=null) {
                Date date = sf.parse(this.date);
                Date odate = sf.parse(o.getDate());
                return date.compareTo(odate);
            }
            return 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
