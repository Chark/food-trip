package io.chark.food.util.authentication;

import io.chark.food.domain.authentication.account.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtils {

    /**
     * Get currently authenticated users id.
     *
     * @return currently authenticated users id or an id of 0 if something failed.
     */
    public static long getId() {
        Account account = getAccount();
        if (account == null) {
            return 0;
        }
        return account.getId();
    }

    /**
     * Get currently authenticated user account.
     *
     * @return currently authenticated user account or null if something failed.
     */
    public static Account getAccount() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null) {
            return null;
        }

        return (Account) authentication
                .getPrincipal();
    }
}