package io.chark.food.domain.offer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.restaurant.Restaurant;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Offer extends BaseEntity {

    private String validThrough;

    private String description;

    private String headline;

    private Date publicationDate;

    public Offer(String validThrough, String description, String headline) {
        this.validThrough = validThrough;
        this.description = description;
        this.headline = headline;
        this.publicationDate = new Date();
    }

    public Offer() {
    }

    @ManyToOne
    @JsonBackReference
    private Restaurant restaurant;

    public String getValidThrough() {
        return validThrough;
    }

    public void setValidThrough(String validThrough) {
        this.validThrough = validThrough;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Date getValidThroughDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.parse(validThrough);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }
}
