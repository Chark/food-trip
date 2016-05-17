package io.chark.food.domain.article.photo;

import io.chark.food.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
public class ArticlePhoto extends BaseEntity {

    private byte[] photo;
    private String description;
    private String alt;

    @Column(nullable = false)
    private Date creationDate;

    public ArticlePhoto() {
    }

    public ArticlePhoto(byte[] photo, String description, String alt) {
        this.photo = photo;
        this.description = description;
        this.alt = alt;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
