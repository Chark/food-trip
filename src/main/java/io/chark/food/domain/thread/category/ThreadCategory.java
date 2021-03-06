package io.chark.food.domain.thread.category;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.article.Article;
import io.chark.food.domain.comment.Comment;
import io.chark.food.domain.thread.Thread;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class ThreadCategory extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Size(max = DEFAULT_LONG_LENGTH)
    @Column(length = DEFAULT_LONG_LENGTH)
    private String description;

    @Column(nullable = false)
    private Date creationDate;
    private Date editDate;

    @JsonBackReference
    @OneToMany(mappedBy = "threadCategory")
    @OrderBy("title ASC")
    private List<Thread> threads = new ArrayList<>();

    public ThreadCategory() {
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getEditDate() {
        return editDate;
    }

    public List<Thread> getThreads() {
        return threads;
    }

    public ThreadCategory(String name, String description) {
        this.name = name;
        this.creationDate = new Date();
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }
}