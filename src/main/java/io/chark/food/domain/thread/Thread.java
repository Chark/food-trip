package io.chark.food.domain.thread;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.comment.Comment;
import io.chark.food.domain.thread.category.ThreadCategory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
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

    @ManyToOne(fetch = FetchType.EAGER)
    private ThreadCategory threadCategory;


    @OneToMany
    @OrderBy("rating DESC")
    private List<Comment> comments;

    public Thread() {
    }

    public Date getEditDate() {
        return editDate;
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }

    public int getCurrentlyViewing() {
        return currentlyViewing;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public Thread(Account account, String title, String description, boolean registrationRequired, ThreadCategory threadCategory) {
        this.account = account;
        this.title = title;
        this.description = description;
        this.registrationRequired = registrationRequired;
        this.threadCategory = threadCategory;
        this.comments = new ArrayList<>();
    }


    public void setComments(List<Comment> comments) {
        this.comments = comments;
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

    public void setThreadCategory(ThreadCategory threadCategory) {
        this.threadCategory = threadCategory;
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

    public String getMonthYear(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(this.creationDate.getTime());
        return cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR);
    }

    public void addComment(Comment c){
        comments.add(c);
    }

    public boolean isRegistrationRequired() {
        return registrationRequired;
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

    public int incremenetViewCount(){
        return ++this.viewCount;
    }
    public ThreadCategory getThreadCategory() {
        return threadCategory;
    }
}