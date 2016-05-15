package io.chark.food.util.authentication;

import io.chark.food.domain.authentication.account.Account;
import io.chark.food.util.exception.UnauthorizedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

public class AuthenticationUtils {

    /**
     * Get account id or throw an exception if no authentication is found.
     *
     * @return currently authenticated accounts id.
     * @throws UnauthorizedException if no authentication is found.
     */
    public static long getIdOrThrow() {
        return getAccountOrThrow().getId();
    }

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
     * Get currently authenticated user account or thrown a exception is there is not authentication.
     *
     * @return user account.
     * @throws UnauthorizedException if no authentication is found.
     */
    public static Account getAccountOrThrow() {
        Account account = getAccount();
        if (account == null) {
            throw new UnauthorizedException("No authentication is found");
        }
        return account;
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

    /**
     * Put new account entity to current authentication context.
     *
     * @param account account to put to context.
     */
    public static void setAccount(Account account) {
        Assert.notNull(account);
        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(
                        account, account.getPassword(),
                        account.getAuthorities()));
    }
}