package io.chark.food.domain.article.category;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.article.Article;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
public class ArticleCategory extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Size(max = DEFAULT_LONG_LENGTH)
    @Column(length = DEFAULT_LONG_LENGTH)
    private String description;

    @Column(nullable = false)
    private Date creationDate;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "categories")
    private List<Article> articles = new ArrayList<>();

    public ArticleCategory() {
    }

    public ArticleCategory(String title, String description) {
        this.title = title;
        this.description = description;
        this.creationDate = new Date();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
