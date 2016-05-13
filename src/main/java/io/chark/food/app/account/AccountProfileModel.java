package io.chark.food.app.account;

import io.chark.food.domain.authentication.account.Account;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.Errors;

import javax.validation.constraints.Size;

public class AccountProfileModel {

    @NotEmpty
    private String email;

    @Size(min = Account.MIN_LENGTH, max = Account.DEFAULT_LENGTH)
    private String password;

    @Size(min = Account.MIN_LENGTH, max = Account.DEFAULT_LENGTH)
    private String repeatPassword;

    @Size(max = Account.DEFAULT_LONG_LENGTH)
    private String bio;

    private String name;
    private String lastName;

    public AccountProfileModel() {
    }

    public AccountProfileModel(Account account) {

        // Password is not set for model.
        this.email = account.getEmail();
        this.bio = account.getBio();
        this.name = account.getName();
        this.lastName = account.getLastName();
    }

    /**
     * Convert this model to account entity.
     *
     * @return account entity.
     */
    public Account toAccount() {
        Account account = new Account();

        account.setEmail(email);
        account.setPassword(password);
        account.setBio(bio);
        account.setName(name);
        account.setLastName(lastName);

        return account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void validate(Errors errors) {
        if (password != null && !password.equals(repeatPassword)) {
            errors.rejectValue("repeatPassword", "Password mismatch", "passwords must match");
        }
    }
}