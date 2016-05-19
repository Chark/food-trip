package io.chark.food.domain.comment;

import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.thread.Thread;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    @OneToMany
    private List<Rating> ratings = new ArrayList<>();

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

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public String getMonthYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(this.creationDate.getTime());
        return cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR);
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public int getVotes() {
        int score = 0;
        for (Rating r : ratings) {
            if (r.isPositive())
                score++;
            else
                score--;
        }
        return score;
    }

    public void addRaiting(Rating rating) {
        this.ratings.add(rating);
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