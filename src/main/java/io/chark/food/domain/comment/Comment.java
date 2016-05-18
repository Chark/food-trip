package io.chark.food.domain.comment;

import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.thread.Thread;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Comment extends BaseEntity {

    @ManyToOne
    private Thread thread;


    private String text;
    private String link;

    @Column(nullable = false)
    private Date creationDate = new Date();
    private Date editDate = new Date();

    private boolean hidden;

    public Comment(Thread thread, String text, boolean hidden) {
        this.thread = thread;
        this.text = text;
        this.hidden = hidden;
    }

    public Thread getThread() {
        return thread;
    }

    public String getText() {
        return text;
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
}