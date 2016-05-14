package io.chark.food.domain.authentication.permission;

import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.extras.Color;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Permission extends BaseEntity implements GrantedAuthority {

    public enum Authority {
        ROLE_USER("USER"),
        ROLE_WORKER("WORKER"),
        ROLE_MODERATOR("MODERATOR"),
        ROLE_ADMIN("ADMIN");

        String name;

        Authority(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private Authority authority;

    @Column(nullable = false)
    private Color color = Color.DEFAULT;

    public Permission() {
    }

    public Permission(String name, Authority authority, Color color) {
        this.name = name;
        this.authority = authority;
        this.color = color;
    }

    @Override
    public String getAuthority() {
        return authority.toString();
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;

        Permission that = (Permission) o;

        return name != null ?
                name.equals(that.name) :
                that.name == null && authority == that.authority;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (authority != null ? authority.hashCode() : 0);
        return result;
    }
}