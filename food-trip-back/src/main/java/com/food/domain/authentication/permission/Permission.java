package com.food.domain.authentication.permission;

import com.food.domain.BaseEntity;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Permission extends BaseEntity implements GrantedAuthority {

    public enum Authority {
        ROLE_USER,
        ROLE_WORKER,
        ROLE_MODERATOR,
        ROLE_ADMIN
    }

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private Authority authority;

    public Permission() {
    }

    public Permission(String name, Authority authority) {
        this.name = name;
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority.toString();
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