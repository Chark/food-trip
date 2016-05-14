package io.chark.food.app.administrate;

import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.authentication.account.AccountRepository;
import io.chark.food.util.exception.NotFoundException;
import io.chark.food.util.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static io.chark.food.domain.authentication.permission.Permission.Authority.ROLE_ADMIN;

@Service
public class AccountAdministrationService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountAdministrationService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Get a user account by id.
     *
     * @return user account optional.
     */
    public Optional<Account> getAccount(long id) {
        return Optional.of(accountRepository.getOne(id));
    }

    /**
     * Get all accounts.
     *
     * @return list of accounts, never null.
     */
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Lock or unlock user account.
     *
     * @param id     account id.
     * @param locked should the account be locked or not.
     * @return updated account.
     */
    public Account setLocked(long id, boolean locked) {
        Account account = getAccount(id)
                .orElseThrow(() -> new NotFoundException(Account.class, id));

        // You cannot lock admin accounts.
        if (account.hasPermission(ROLE_ADMIN)) {
            throw new UnauthorizedException("You cannot alter locked state of an admin account");
        }

        account.setLocked(locked);
        accountRepository.save(account);
        return account;
    }
}