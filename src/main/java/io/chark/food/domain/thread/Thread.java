package io.chark.food.domain.thread;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.comment.Comment;
import io.chark.food.domain.thread.category.ThreadCategory;

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

    private String description;
    private String threadLink;

    @Column(nullable = false)
    private Date creationDate = new Date();
    private Date editDate = new Date();

    private int currentlyViewing;
    private int viewCount;

    private boolean registrationRequired;

    @JsonBackReference
    @ManyToOne
    private ThreadCategory threadCategory;

    @OneToMany
    @Column(nullable = false)
    private List<Comment> comments;

    public Thread() {
    }

    public Thread(Account account, String title, String description, boolean registrationRequired, ThreadCategory threadCategory) {
        this.account = account;
        this.title = title;
        this.description = description;
        this.registrationRequired = registrationRequired;
        this.threadCategory = threadCategory;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public void setThreadLink(String threadLink) {
        this.threadLink = threadLink;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setRegistrationRequired(boolean registrationRequired) {
        this.registrationRequired = registrationRequired;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public Account getAccount() {
        return account;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getDescription() {
        return description;
    }

    public int getViewCount() {
        return viewCount;
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

    public String getTitle() {
        return title;
    }

    public ThreadCategory getThreadCategory() {
        return threadCategory;
    }
}