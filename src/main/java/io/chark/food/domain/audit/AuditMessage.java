package io.chark.food.domain.audit;

import com.fasterxml.jackson.annotation.JsonView;
import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.extras.Color;

import javax.persistence.*;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
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

    /**
     * Helper method to get accounts pretty username, which cause this message.
     *
     * @return account username or null.
     */
    @JsonView(MinimalView.class)
    public String getUsername() {
        if (account == null) {
            return null;
        }
        return account.getPrettyUsername();
    }

    @JsonView(MinimalView.class)
    public String getMessage() {
        return message;
    }

    @JsonView(MinimalView.class)
    public Date getCreationDate() {
        return creationDate;
    }

    @JsonView(MinimalView.class)
    public Color getColor() {
        return color;
    }

    @Override
    @JsonView(MinimalView.class)
    public long getId() {
        return super.getId();
    }
}