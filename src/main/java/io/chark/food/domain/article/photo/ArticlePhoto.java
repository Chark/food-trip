package io.chark.food.domain.article.photo;

import io.chark.food.domain.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ArticlePhoto extends BaseEntity {

    @Lob
//    @Basic(fetch = FetchType.EAGER) todo Ugnius maybe use file path instead of blob? Its causing issues on postgres
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