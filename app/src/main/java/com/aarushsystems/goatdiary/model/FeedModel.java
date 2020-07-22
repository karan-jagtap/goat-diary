package com.aarushsystems.goatdiary.model;

import java.io.Serializable;

public class FeedModel implements Serializable, Comparable<FeedModel> {

    private String date, weight, remarks;
    private int srno, action, time, feedType;

    public FeedModel() {
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getSrno() {
        return srno;
    }

    public void setSrno(int srno) {
        this.srno = srno;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getFeedType() {
        return feedType;
    }

    public void setFeedType(int feedType) {
        this.feedType = feedType;
    }

    @Override
    public int compareTo(FeedModel feedModel) {
        return ((Integer) this.getSrno()).compareTo(feedModel.getSrno());
    }
}
