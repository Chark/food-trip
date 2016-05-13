package io.chark.food.domain.article;

import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.article_category.ArticleCategory;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Article extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column
    private String shortDescription;

    @Column
    private String metaKeywords;

    @Column
    private String metaDescription;

    @Column(nullable = false)
    private Date creationDate;

    @Column
    private Date editedDate;

    @Column
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