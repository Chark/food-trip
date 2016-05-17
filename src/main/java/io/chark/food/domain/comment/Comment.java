package io.chark.food.domain.comment;

import io.chark.food.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Comment extends BaseEntity {

    @ManyToOne
    private Thread thread;

    private String text;

    @Column(nullable = false)
    private Date creationDate;

    private boolean isHidden;

    private Date editDate;

    private String link;


    public Comment(Thread thread, String text, Date creationDate, boolean isHidden) {
        this.thread = thread;
        this.text = text;
        this.creationDate = creationDate;
        this.isHidden = isHidden;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
