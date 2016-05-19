package io.chark.food.domain.comment;

import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.thread.Thread;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Comment extends BaseEntity {


    private String text;
    private String link;

    @ManyToOne
    private Account account;

    @ManyToOne
    private Thread thread;

    @Column(nullable = false)
    private Date creationDate = new Date();
    private Date editDate = new Date();

    private boolean hidden;

    public Comment() {
    }

    public Comment(Account account, String text, boolean hidden) {
        this.text = text;
        this.hidden = hidden;
        this.account = account;
    }


    public String getText() {
        return text;
    }

    public void setThread(Thread thread){
        this.thread = thread;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public boolean isHidden() {
        return hidden;
    }

    public Date getEditDate() {
        return editDate;
    }

    public String getLink() {
        return link;
    }

    public Account getAccount() {
        return account;
    }
}