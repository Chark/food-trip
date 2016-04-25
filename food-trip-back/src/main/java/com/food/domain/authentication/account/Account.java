package com.food.domain.authentication.account;

import com.food.domain.BaseEntity;
import com.food.domain.authentication.permission.Permission;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.*;

@Entity
public class Account extends BaseEntity implements UserDetails {

    public static final int DEFAULT_LENGTH = 64;

    @Column(length = DEFAULT_LENGTH, nullable = false, unique = true)
    private String prettyUsername;

    @Column(length = DEFAULT_LENGTH, nullable = false, unique = true)
    private String username;

    @Column(length = DEFAULT_LENGTH, nullable = false, unique = true)
    private String email;

    @Column(length = DEFAULT_LENGTH * 32, nullable = false)
    private String password;

    @Column(length = DEFAULT_LENGTH)
    private String lastName;

    @Column(length = DEFAULT_LENGTH)
    private String name;
    private String bio;

    @Column(nullable = false)
    private Date registrationDate;

    private int points;

    private boolean enabled;
    private boolean locked;

    @ManyToMany
    @Column(nullable = false)
    private Set<Permission> permissions = new HashSet<>();

    public Account() {
    }

    public Account(String username, String email, String password) {
        this.registrationDate = new Date();
        this.setUsername(username);
        this.setPassword(password);
        this.setEmail(email);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public void setUsername(String username) {
        this.prettyUsername = username;
        this.username = username.toLowerCase();
    }

    public String getPrettyUsername() {
        return prettyUsername;
    }

    public String getEmail() {
        return email;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void addPermission(Permission permission) {
        permissions.add(permission);
    }

    @Override
    public Set<Permission> getAuthorities() {
        return permissions;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}