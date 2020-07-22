package com.aarushsystems.goatdiary.model;

import java.io.Serializable;

public class VaccinationModel implements Serializable, Comparable<VaccinationModel> {

    private int srno, tagId, vaccine, dose, booster;
    private String date, proposedDate;

    public VaccinationModel() {
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

    public int getVaccine() {
        return vaccine;
    }

    public void setVaccine(int vaccine) {
        this.vaccine = vaccine;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }

    public int getBooster() {
        return booster;
    }

    public void setBooster(int booster) {
        this.booster = booster;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProposedDate() {
        return proposedDate;
    }

    public void setProposedDate(String proposedDate) {
        this.proposedDate = proposedDate;
    }

    @Override
    public int compareTo(VaccinationModel vaccinationModel) {
        return ((Integer) this.getSrno()).compareTo(vaccinationModel.getSrno());
    }
}
