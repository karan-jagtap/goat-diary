package com.aarushsystems.goatdiary.model.reports;

public class ReportVaccinationModel {
    private String date, vaccine, animalTypeString, breedString;
    private int tagId, dose, animalType, breed;

    public String getBreedString() {
        return breedString;
    }

    public void setBreedString(String breedString) {
        this.breedString = breedString;
    }

    public String getAnimalTypeString() {
        return animalTypeString;
    }

    public void setAnimalTypeString(String animalTypeString) {
        this.animalTypeString = animalTypeString;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getDose() {
        return dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }

    public int getAnimalType() {
        return animalType;
    }

    public void setAnimalType(int animalType) {
        this.animalType = animalType;
    }

    public int getBreed() {
        return breed;
    }

    public void setBreed(int breed) {
        this.breed = breed;
    }
}
