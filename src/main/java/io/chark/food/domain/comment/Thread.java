package io.chark.food.domain.comment;

import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.authentication.account.Account;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Thread extends BaseEntity {

    //TODO Add title column to entities diagram

    @ManyToOne
    private Account account;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Date creationDate;

    private String description;

    private int viewsCount;

    private Date editDate;

    private boolean registrationRequired;

    private int currentlyViewing;

    private String threadLink;

    @OneToMany
    private List<Comment> comments;

    public Thread(){

    }

    public Thread(Account account,String title, String description, boolean registrationRequired) {
        this.account = account;
        this.title = title;
        this.description = description;
        this.creationDate = new Date();
        this.viewsCount = 0;
        this.registrationRequired = registrationRequired;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public Date getEditDate() {
        return editDate;
    }

    public boolean isRegistrationRequired() {
        return registrationRequired;
    }

    public int getCurrentlyViewing() {
        return currentlyViewing;
    }

    public String getThreadLink() {
        return threadLink;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}