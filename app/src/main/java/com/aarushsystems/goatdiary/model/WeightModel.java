package com.aarushsystems.goatdiary.model;

import java.io.Serializable;

public class WeightModel implements Serializable, Comparable<WeightModel> {

    private int srno, tagId;
    private String date, weight;

    public WeightModel() {
    }

    public int getSrno() {
        return srno;
    }

    public void setSrno(int srno) {
        this.srno = srno;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(WeightModel weightModel) {
        return ((Integer) this.getSrno()).compareTo(weightModel.getSrno());
    }
}
