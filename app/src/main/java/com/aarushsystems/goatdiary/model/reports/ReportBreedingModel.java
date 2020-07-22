package com.aarushsystems.goatdiary.model.reports;

public class ReportBreedingModel {

    private String date, matingDate, confDate, delDate;
    private int femaleId, maleId, stillBorn, abortion, maleChild, femaleChild;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMatingDate() {
        return matingDate;
    }

    public void setMatingDate(String matingDate) {
        this.matingDate = matingDate;
    }

    public String getConfDate() {
        return confDate;
    }

    public void setConfDate(String confDate) {
        this.confDate = confDate;
    }

    public String getDelDate() {
        return delDate;
    }

    public void setDelDate(String delDate) {
        this.delDate = delDate;
    }

    public int getFemaleId() {
        return femaleId;
    }

    public void setFemaleId(int femaleId) {
        this.femaleId = femaleId;
    }

    public int getMaleId() {
        return maleId;
    }

    public void setMaleId(int maleId) {
        this.maleId = maleId;
    }

    public int getStillBorn() {
        return stillBorn;
    }

    public void setStillBorn(int stillBorn) {
        this.stillBorn = stillBorn;
    }

    public int getAbortion() {
        return abortion;
    }

    public void setAbortion(int abortion) {
        this.abortion = abortion;
    }

    public int getMaleChild() {
        return maleChild;
    }

    public void setMaleChild(int maleChild) {
        this.maleChild = maleChild;
    }

    public int getFemaleChild() {
        return femaleChild;
    }

    public void setFemaleChild(int femaleChild) {
        this.femaleChild = femaleChild;
    }
}
