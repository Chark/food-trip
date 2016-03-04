package com.food.domain.sandwich;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.food.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Sandwich extends BaseEntity {

    private String name;
    private int weight;

    @ManyToOne
    @JsonBackReference
    private Basket basket;

    public Sandwich() {
    }

    public Sandwich(int weight, String name) {
        this.weight = weight;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }
}