package io.chark.food.domain.sandwich;

import io.chark.food.domain.BaseEntity;

import javax.persistence.Entity;

@Entity
public class Sandwich extends BaseEntity {

    private String name;
    private int weight;

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