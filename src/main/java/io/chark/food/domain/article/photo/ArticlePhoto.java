package io.chark.food.domain.article.photo;

import io.chark.food.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import java.util.Date;

@Entity
public class ArticlePhoto extends BaseEntity {

    @Lob
    @Column(nullable = false)
    private byte[] photo;

    private String description;
    private String alt;

    @Column(nullable = false)
    private Date creationDate = new Date();

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

    public String getDescription() {
        return description;
    }

    public String getAlt() {
        return alt;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}