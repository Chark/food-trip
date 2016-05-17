package io.chark.food.domain.article;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.article.category.ArticleCategory;
import io.chark.food.domain.article.photo.ArticlePhoto;

import javax.persistence.*;
import java.util.*;

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

    private long viewsNumber;

    @ManyToMany
    @JsonBackReference
    private List<ArticleCategory> categories = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    private List<ArticlePhoto> photos = new ArrayList<>();

    public Article() {
    }

    public Article(String title, String description, String shortDescription, String metaKeywords, String metaDescription) {
        this.title = title;
        this.description = description;
        this.shortDescription = shortDescription;
        this.metaKeywords = metaKeywords;
        this.metaDescription = metaDescription;
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

    public long getViewsNumber() {
        return viewsNumber;
    }

    public void setViewsNumber(long viewsNumber) {
        this.viewsNumber = viewsNumber;
    }

    public List<ArticleCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ArticleCategory> categories) {
        this.categories = categories;
    }

    public void addCategory(ArticleCategory category) {
        categories.add(category);
    }

    public List<ArticlePhoto> getPhotos() {
        return photos;
    }

    public ArticlePhoto getPhoto(int id) {
        return photos.get(id);
    }

    public void setPhotos(List<ArticlePhoto> photos) {
        this.photos = photos;
    }

    public void addPhoto(ArticlePhoto photo) {
        photos.add(photo);
    }
}
