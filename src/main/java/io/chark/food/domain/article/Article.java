package io.chark.food.domain.article;

import io.chark.food.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Article extends BaseEntity {

    @Column(nullable = false)
    private String title;

    private String description;

    private String shortDescription;

    private String metaKeywords;

    private String metaDescription;

    @Column(nullable = false)
    private Date creationDate;

    private Date editedDate;

    private long viewsNumber;

    @Column(nullable = false)
    @OneToMany(fetch = FetchType.EAGER)
    private Set<ArticleCategory> categories = new HashSet<>();

    public Article() {
    }

    public Article(String title, String description, String shortDescription, String metaKeywords, String metaDescription) {
        this.title = title;
        this.description = description;
        this.shortDescription = shortDescription;
        this.metaKeywords = metaKeywords;
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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getEditedDate() {
        return editedDate;
    }

    public void setEditedDate(Date editedDate) {
        this.editedDate = editedDate;
    }

    public long getViewsNumber() {
        return viewsNumber;
    }

    public void setViewsNumber(long viewsNumber) {
        this.viewsNumber = viewsNumber;
    }

    public Set<ArticleCategory> getCategories() {
        return categories;
    }

    public void setCategories(Set<ArticleCategory> categories) {
        this.categories = categories;
    }

    public void addCategory(ArticleCategory category) {
        categories.add(category);
    }
}
