package io.chark.food.domain.restaurant;

import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.authentication.account.Account;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Entity
public class Restaurant extends BaseEntity {

    @Column(unique = true, nullable = false, length = DEFAULT_LENGTH)
    private String email;

    @Column(unique = true, nullable = false, length = DEFAULT_LENGTH)
    private String name;

    @Column(nullable = false, length = DEFAULT_LONG_LENGTH)
    private String description;

    private int rating;
    private int hygieneLevel;

    private long viewCount;

    @Column(nullable = false)
    private Date creationDate = new Date();

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.EAGER)
    private List<Account> accounts;

    public Restaurant() {
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
        this.email = email;
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
}