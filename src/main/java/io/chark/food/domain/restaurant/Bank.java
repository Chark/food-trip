package io.chark.food.domain.restaurant;


import io.chark.food.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

@Entity
public class Bank extends BaseEntity {

    @Size(max = DEFAULT_LENGTH)
    @Column(unique = true, nullable = false, length = DEFAULT_LENGTH)
    private String name;

    public Bank(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
