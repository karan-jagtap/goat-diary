package com.aarushsystems.goatdiary.model;

import java.io.Serializable;

public class BreedingModel implements Serializable, Comparable<BreedingModel> {
    private int srno, maleId, femaleId, abortion, stillborn, childMale, childFemale;
    private String matingFlag, matingDate, confFlag, confDate, deliveryFlag, deliveryDate;

    public BreedingModel() {
    }

    public int getSrno() {
        return srno;
    }

    public void setSrno(int srno) {
        this.srno = srno;
    }

    public int getMaleId() {
        return maleId;
    }

    public void setMaleId(int maleId) {
        this.maleId = maleId;
    }

    public int getFemaleId() {
        return femaleId;
    }

    public void setFemaleId(int femaleId) {
        this.femaleId = femaleId;
    }

    public int getAbortion() {
        return abortion;
    }

    public void setAbortion(int abortion) {
        this.abortion = abortion;
    }

    public int getStillborn() {
        return stillborn;
    }

    public void setStillborn(int stillborn) {
        this.stillborn = stillborn;
    }

    public int getChildMale() {
        return childMale;
    }

    public void setChildMale(int childMale) {
        this.childMale = childMale;
    }

    public int getChildFemale() {
        return childFemale;
    }

    public void setChildFemale(int childFemale) {
        this.childFemale = childFemale;
    }

    public String getMatingFlag() {
        return matingFlag;
    }

    public void setMatingFlag(String matingFlag) {
        this.matingFlag = matingFlag;
    }

    public String getMatingDate() {
        return matingDate;
    }

    public void setMatingDate(String matingDate) {
        this.matingDate = matingDate;
    }

    public String getConfFlag() {
        return confFlag;
    }

    public void setConfFlag(String confFlag) {
        this.confFlag = confFlag;
    }

    public String getConfDate() {
        return confDate;
    }

    public void setConfDate(String confDate) {
        this.confDate = confDate;
    }

    public String getDeliveryFlag() {
        return deliveryFlag;
    }

    public void setDeliveryFlag(String deliveryFlag) {
        this.deliveryFlag = deliveryFlag;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Override
    public int compareTo(BreedingModel t) {
        return ((Integer) this.getSrno()).compareTo(t.getSrno());
    }
}
