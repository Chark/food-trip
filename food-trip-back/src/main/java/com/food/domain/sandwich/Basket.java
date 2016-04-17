package com.food.domain.sandwich;

import com.food.domain.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Basket extends BaseEntity {

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Sandwich> sandwiches = new ArrayList<>();

    public Basket() {
    }

    public void addSandwich(Sandwich item) {
        this.sandwiches.add(item);
    }

    public List<Sandwich> getSandwiches() {
        return sandwiches;
    }
}