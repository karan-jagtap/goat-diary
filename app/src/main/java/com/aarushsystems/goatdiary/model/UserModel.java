package com.aarushsystems.goatdiary.model;

import java.io.Serializable;

public class UserModel implements Serializable {

    private String name, email, phone, city, country;

    public UserModel() {

    }

    public UserModel(String name, String email, String phone, String city, String country) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.city = city;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
