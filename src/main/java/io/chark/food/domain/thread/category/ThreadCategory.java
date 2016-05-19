package io.chark.food.domain.thread.category;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.article.Article;
import io.chark.food.domain.comment.Comment;
import io.chark.food.domain.thread.Thread;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class ThreadCategory extends BaseEntity {

    @Column(nullable = false)
    private String name;

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
}