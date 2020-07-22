package com.aarushsystems.goatdiary.model;

import java.io.Serializable;

public class AddAnimalModel implements Serializable, Comparable<AddAnimalModel> {

    private String gender, date, weight, motherId, price, dDate, releasePrice, releaseWeight, remarks;
    private int srno, tagId, animalType, aquisation, breed, purpose, release, deleted;

    public AddAnimalModel() {
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getMotherId() {
        return motherId;
    }

    public void setMotherId(String motherId) {
        this.motherId = motherId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getdDate() {
        return dDate;
    }

    public void setdDate(String dDate) {
        this.dDate = dDate;
    }

    public String getReleasePrice() {
        return releasePrice;
    }

    public void setReleasePrice(String releasePrice) {
        this.releasePrice = releasePrice;
    }

    public String getReleaseWeight() {
        return releaseWeight;
    }

    public void setReleaseWeight(String releaseWeight) {
        this.releaseWeight = releaseWeight;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getAnimalType() {
        return animalType;
    }

    public void setAnimalType(int animalType) {
        this.animalType = animalType;
    }

    public int getAquisation() {
        return aquisation;
    }

    public void setAquisation(int aquisation) {
        this.aquisation = aquisation;
    }

    public int getBreed() {
        return breed;
    }

    public void setBreed(int breed) {
        this.breed = breed;
    }

    public int getPurpose() {
        return purpose;
    }

    public void setPurpose(int purpose) {
        this.purpose = purpose;
    }

    public int getRelease() {
        return release;
    }

    public void setRelease(int release) {
        this.release = release;
    }


    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    @Override
    public int compareTo(AddAnimalModel addAnimalModel) {
        return ((Integer) this.getSrno()).compareTo(addAnimalModel.getSrno());
    }
}
