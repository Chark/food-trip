package io.chark.food.domain.restaurant;


import io.chark.food.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

@Entity
public class Bank extends BaseEntity {

    @Size(max = DEFAULT_LENGTH)
    @Column(unique = true, nullable = false, length = DEFAULT_LENGTH)
    private String pavadinimas;

    public Bank(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }

    public String getPavadinimas() {
        return pavadinimas;
    }

    public void setPavadinimas(String pavadinimas) {
        this.pavadinimas = pavadinimas;
    }
}
