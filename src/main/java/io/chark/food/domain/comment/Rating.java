package io.chark.food.domain.comment;

import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.authentication.account.Account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Rating extends BaseEntity {


    @Column(nullable = false)
    private boolean isPositive;

    @ManyToOne
    private Account account;

    private Date ratingDate;


    public Rating() {
    }

    public Account getAccount() {
        return account;
    }

    public Rating(boolean isPositive, Account account) {
        this.ratingDate = new Date();
        this.isPositive = isPositive;
        this.account = account;
    }


    public Date getRatingDate() {
        return ratingDate;
    }

    public boolean isPositive() {
        return isPositive;
    }
}
