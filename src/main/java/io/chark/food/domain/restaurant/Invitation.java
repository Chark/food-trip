package io.chark.food.domain.restaurant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.authentication.account.Account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Invitation extends BaseEntity {

    @Column(nullable = false, length = DEFAULT_LENGTH)
    private String username;

    @ManyToOne(optional = false)
    private Restaurant restaurant;

    @ManyToOne
    private Account account;

    public Invitation() {
    }

    public Invitation(String username, Account account, Restaurant restaurant) {
        this.restaurant = restaurant;
        this.username = username;
        this.account = account;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    @JsonIgnore
    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @JsonIgnore
    public Account getAccount() {
        return account;
    }
}