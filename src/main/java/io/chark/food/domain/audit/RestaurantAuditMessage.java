package io.chark.food.domain.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.extras.Color;
import io.chark.food.domain.restaurant.Restaurant;

import javax.persistence.*;

@Entity
public class RestaurantAuditMessage extends AuditMessage {

    @Column(nullable = false)
    private String action;

    @OneToOne
    private Restaurant restaurant;

    public RestaurantAuditMessage() {
    }

    public RestaurantAuditMessage(Account account,
                                  String message,
                                  Color color,
                                  String action,
                                  Restaurant restaurant) {

        super(account, message, color);
        this.action = action;
        this.restaurant = restaurant;
    }

    public String getAction() {
        return action;
    }

    @JsonIgnore
    public Restaurant getRestaurant() {
        return restaurant;
    }
}