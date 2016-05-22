package io.chark.food.domain.restaurant.location;


import io.chark.food.domain.BaseEntity;

import javax.persistence.Entity;

@Entity
public class City extends BaseEntity {

    private String name;
    private String country;

    public City() {
    }

    public City(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }
}
