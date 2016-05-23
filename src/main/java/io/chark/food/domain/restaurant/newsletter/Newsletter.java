package io.chark.food.domain.restaurant.newsletter;


import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.offer.Offer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Newsletter extends BaseEntity {

    private Date creationDate;

    private boolean published;


    @Size(max = DEFAULT_LONG_LENGTH)
    @Column(length = DEFAULT_LONG_LENGTH)
    private String description;

    private String title;

    private int viewCount;

    private boolean edited;

    private String expirationDate;

    @ManyToOne
    private Account account;


    @ManyToMany()
    @Column(unique = true)
    private List<Offer> offers = new ArrayList<>();

    public Newsletter() {
    }

    public void removeOffer(Offer offer) {
        this.offers.remove(offer);
    }

    public Newsletter(String description, String title, String expirationDate, Account account, boolean published) {
        this.description = description;
        this.title = title;
        this.expirationDate = expirationDate;
        this.creationDate = new Date();
        this.account = account;
        this.published = published;
        this.edited = false;
    }

    public boolean hasOffer(long id) {
        for (Offer o : offers) {
            if (o.getId() == id)
                return true;
        }
        return false;
    }


    public void addOffer(Offer o) {
        if (!offers.contains(o))
            offers.add(o);
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public boolean isEdited() {
        return edited;
    }

    public boolean isPublished() {
        return published;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Date getCreationDate() {

        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }


}
