package com.aarushsystems.goatdiary.model.reports;

import androidx.annotation.Nullable;

public class ReportLiveStockModel {
    private String animalType, breed, aquisation, release, action, gender, date;
    private int maleCount, femaleCount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAnimalType() {
        return animalType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public String getAquisation() {
        return aquisation;
    }

    public void setAquisation(String aquisation) {
        this.aquisation = aquisation;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getMaleCount() {
        return maleCount;
    }

    public void setMaleCount(int maleCount) {
        this.maleCount = maleCount;
    }

    public int getFemaleCount() {
        return femaleCount;
    }

    public void setFemaleCount(int femaleCount) {
        this.femaleCount = femaleCount;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return false;
        }
        if (!(obj instanceof ReportLiveStockModel)) {
            return false;
        }
        ReportLiveStockModel rm = (ReportLiveStockModel) obj;
        if (action.equals("SELECT")) {
            return true;
        }
        if (animalType.equals(rm.getAnimalType()) &&
                breed.equals(rm.getBreed()) &&
                action.equals(rm.getAction()) &&
                maleCount == rm.getMaleCount() &&
                femaleCount == rm.getFemaleCount() ) {
            return true;
        }
        return false;
    }
}
