package io.chark.food.domain.restaurant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.article.Article;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.offer.Offer;
import io.chark.food.domain.restaurant.location.Location;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Restaurant extends BaseEntity {

    @Size(max = DEFAULT_LENGTH)
    @Column(unique = true, nullable = false, length = DEFAULT_LENGTH)
    private String email;

    @Size(min = MIN_LENGTH, max = DEFAULT_LENGTH)
    @Column(unique = true, nullable = false, length = DEFAULT_LENGTH)
    private String name;

    @Size(max = DEFAULT_LONG_LENGTH)
    @Column(length = DEFAULT_LONG_LENGTH)
    private String description;

    private int rating;
    private int hygieneLevel;

    private long viewCount;

    @Column(nullable = false)
    private Date creationDate = new Date();

    @Fetch(FetchMode.SELECT)
    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    private List<Account> accounts;

    // Invited accounts to this restaurant.
    @OneToMany(mappedBy = "restaurant", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Invitation> invitations = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", orphanRemoval = true)
    private List<Article> articles;

    @OneToMany
    private List<Offer> offers;


    @OneToOne
    private Location location;


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @OneToOne()
    private RestaurantDetails restaurantDetails;

    public Restaurant() {
    }

    public Restaurant(String email, String name, String description) {
        this.email = email;
        this.name = name;
        this.description = description;
    }



    public void setRestaurantDetails(RestaurantDetails restaurantDetails) {
        this.restaurantDetails = restaurantDetails;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getRating() {
        return rating;
    }

    public int getHygieneLevel() {
        return hygieneLevel;
    }

    public long getViewCount() {
        return viewCount;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setEmail(String email) {
        if (email != null) {
            this.email = email.toLowerCase();
        } else {
            this.email = null;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setHygieneLevel(int hygieneLevel) {
        this.hygieneLevel = hygieneLevel;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<Invitation> getInvitations() {
        return invitations;
    }

    @JsonIgnore
    public List<Article> getArticles() {
        return articles;
    }

    public RestaurantDetails getRestaurantDetails() {
        return restaurantDetails;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public void addOffer(Offer offer)
    {
        offers.add(offer);
    }

}