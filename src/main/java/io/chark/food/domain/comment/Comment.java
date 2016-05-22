package io.chark.food.domain.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
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


    @Column(nullable = false)
    private Date creationDate = new Date();
    private Date editDate = new Date();

    private int rating;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany
    private List<Rating> ratings = new ArrayList<>();

    private boolean hidden;

    public Comment() {
    }

    public Comment(Account account, String text, boolean hidden) {
        this.text = text;
        this.hidden = hidden;
        this.account = account;
        this.rating = 0;
    }


    public String getText() {
        return text;
    }


    public void setText(String text) {
        this.text = text;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public String getMonthYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(this.creationDate.getTime());
        return cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.YEAR);
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public int getRating() {
        return rating;
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
        if(rating.isPositive()){
            this.rating++;
        }else{
            this.rating--;
        }

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

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getLink() {
        return link;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        return getId() == comment.getId();

    }

    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }
}