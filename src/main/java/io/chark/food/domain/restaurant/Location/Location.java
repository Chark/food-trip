package io.chark.food.domain.restaurant.location;


import io.chark.food.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Location extends BaseEntity {

    private String street;
    private String location;
    private double dangerIndex;


    @ManyToOne
    private City city;

    public Location() {
    }



    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getDangerIndex() {
        return dangerIndex;
    }

    public void setDangerIndex(double dangerIndex) {
        this.dangerIndex = dangerIndex;
    }
}
