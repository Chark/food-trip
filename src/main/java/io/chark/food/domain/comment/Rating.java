package io.chark.food.domain.comment;

import io.chark.food.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Rating extends BaseEntity {


    @Column(nullable = false)
    private boolean isPositive;

    private Date ratingDate;


    public Rating() {
    }

    public Rating(boolean isPositive) {
        this.ratingDate = new Date();
        this.isPositive = isPositive;
    }


    public Date getRatingDate() {
        return ratingDate;
    }

    public boolean isPositive() {
        return isPositive;
    }
}
