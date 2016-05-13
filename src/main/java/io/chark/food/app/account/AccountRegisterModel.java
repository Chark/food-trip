package io.chark.food.app.account;

import io.chark.food.domain.authentication.account.Account;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.Errors;

import javax.validation.constraints.Size;

public class AccountRegisterModel {

    @Size(min = Account.MIN_LENGTH, max = Account.DEFAULT_LENGTH)
    private String username;

    @Size(min = Account.MIN_LENGTH, max = Account.DEFAULT_LENGTH)
    private String password;

    @Size(min = Account.MIN_LENGTH, max = Account.DEFAULT_LENGTH)
    private String repeatPassword;

    @NotEmpty
    private String email;

    public AccountRegisterModel() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public void validate(Errors errors) {
        if (!password.equals(repeatPassword)) {
            errors.rejectValue("repeatPassword", "Password mismatch", "passwords must match");
        }
    }
}