package io.chark.food.domain.authentication.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.chark.food.domain.BaseEntity;
import io.chark.food.domain.authentication.permission.Permission;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account extends BaseEntity implements UserDetails {

    /**
     * Default length for longer string fields.
     */
    public static final int DEFAULT_LONG_LENGTH = 1024;

    /**
     * Default length for various string values.
     */
    public static final int DEFAULT_LENGTH = 64;

    /**
     * Default min length for various string values.
     */
    public static final int MIN_LENGTH = 4;

    @Column(length = DEFAULT_LENGTH, nullable = false, unique = true)
    private String prettyUsername;

    @Column(length = DEFAULT_LENGTH, nullable = false, unique = true)
    private String username;

    @Column(length = DEFAULT_LENGTH, nullable = false, unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(length = DEFAULT_LENGTH * 32, nullable = false)
    private String password;

    @Column(length = DEFAULT_LENGTH)
    private String lastName;

    @Column(length = DEFAULT_LENGTH)
    private String name;

    @Column(length = DEFAULT_LONG_LENGTH)
    private String bio;

    @Column(length = DEFAULT_LENGTH)
    private String website;

    @Column(length = DEFAULT_LENGTH)
    private String phone;

    @Column(nullable = false)
    private Date registrationDate;

    // Karma points?
    private int points;

    private boolean enabled;
    private boolean locked;

    @Column(nullable = false)
    @ManyToMany(fetch = FetchType.EAGER)
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

    /**
     * Enable or disable a user account, disabled accounts cannot login.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public int getPoints() {
        return points;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * Check if this account has a permission based on authority.
     *
     * @param authority authority to check against.
     * @return true if this account has this permission or false otherwise.
     */
    public boolean hasPermission(Permission.Authority authority) {
        for (Permission permission : permissions) {
            if (permission.getAuthority().equals(authority.toString())) {
                return true;
            }
        }
        return false;
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