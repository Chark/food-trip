package io.chark.food.domain.audit;

import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.extras.Color;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
public class AuditMessage extends BaseEntity {

    @OneToOne
    private Account account;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Date creationDate = new Date();

    @Column(nullable = false)
    private Color color = Color.DEFAULT;

    public AuditMessage() {
    }

    public AuditMessage(Account account,
                        String message,
                        Color color) {

        this.account = account;
        this.message = message;
        this.color = color == null ? Color.DEFAULT : color;
    }

    public Account getAccount() {
        return account;
    }

    public String getMessage() {
        return message;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Color getColor() {
        return color;
    }
}