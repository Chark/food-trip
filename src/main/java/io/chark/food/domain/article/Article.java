package io.chark.food.domain.article;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.article.category.ArticleCategory;
import io.chark.food.domain.article.photo.ArticlePhoto;
import io.chark.food.domain.comment.Comment;
import io.chark.food.domain.restaurant.Restaurant;

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
    private Date creationDate = new Date();

    private long viewsNumber;

    @ManyToMany
    @JsonBackReference
    private List<ArticleCategory> categories = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    private List<ArticlePhoto> photos = new ArrayList<>();

    @ManyToOne
    private Restaurant restaurant;

    public Article() {
    }

    @OneToMany(orphanRemoval = true)
    @OrderBy("rating DESC")
    private List<Comment> comments = new ArrayList<>();

    public Article(String title,
                   String description,
                   String shortDescription,
                   String metaKeywords,
                   String metaDescription) {

        this.restaurant = restaurant;
        this.title = title;
        this.description = description;
        this.shortDescription = shortDescription;
        this.metaKeywords = metaKeywords;
        this.metaDescription = metaDescription;
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void addComment(Comment c){
        comments.add(c);
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
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

    public long getViewsNumber() {
        return viewsNumber;
    }

    public void setViewsNumber(long viewsNumber) {
        this.viewsNumber = viewsNumber;
    }

    public List<ArticleCategory> getCategories() {
        return categories;
    }

    public ArticleCategory getCategory(int id) {
        return categories.get(id);
    }

    public void setCategories(List<ArticleCategory> categories) {
        this.categories = categories;
    }

    public void addCategory(ArticleCategory category) {
        categories.add(category);
    }

    @JsonIgnore
    public List<ArticlePhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ArticlePhoto> photos) {
        this.photos = photos;
    }

    public void addPhoto(ArticlePhoto photo) {
        photos.add(photo);
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    /**
     * Check if article has this article category.
     *
     * @param articleCategory article category to check.
     * @return true if article has this category or false otherwise.
     */
    public boolean hasCategory(ArticleCategory articleCategory) {
        for (ArticleCategory category : categories) {
            if (category.equals(articleCategory)) {
                return true;
            }
        }
        return false;
    }
}
