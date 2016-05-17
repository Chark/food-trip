package io.chark.food.app.administrate.account;

import io.chark.food.app.account.AccountService;
import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.authentication.account.AccountRepository;
import io.chark.food.domain.authentication.permission.Permission;
import io.chark.food.util.authentication.AuthenticationUtils;
import io.chark.food.util.exception.NotFoundException;
import io.chark.food.util.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountAdministrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountAdministrationService.class);

    private final AccountRepository accountRepository;
    private final SessionRegistry sessionRegistry;
    private final AccountService accountService;
    private final AuditService auditService;

    @Autowired
    public AccountAdministrationService(AccountRepository accountRepository,
                                        SessionRegistry sessionRegistry,
                                        AccountService accountService,
                                        AuditService auditService) {

        this.accountRepository = accountRepository;
        this.sessionRegistry = sessionRegistry;
        this.accountService = accountService;
        this.auditService = auditService;
    }

    /**
     * Create a new account or update an existing one based on id.
     *
     * @param id             account id.
     * @param accountDetails details used in creation or updating.
     * @param authorities    account authorities to set or update.
     * @return account optional.
     */
    Optional<Account> saveAccount(long id,
                                  Account accountDetails,
                                  Permission.Authority... authorities) {

        // Below or equals means this is a new account.
        Optional<Account> optional;
        if (id <= 0) {

            // Reuse the register method to save a new account.
            optional = accountService.register(
                    accountDetails.getUsername(),
                    accountDetails.getEmail(),
                    accountDetails.getPassword());

        } else {

            // Existing account.
            optional = Optional.of(accountRepository.findOne(id));
        }

        // No account found, error.
        if (!optional.isPresent()) {
            return Optional.empty();
        }

        // Update account details.
        optional = accountService.update(optional.get(), accountDetails);

        // Update other details editable only by admins.
        Account account = optional.get();
        account.setUsername(accountDetails.getUsername());
        account.setEnabled(accountDetails.isEnabled());
        account.setPermissions(accountService.getPermissions(authorities));

        try {

            account = accountRepository.save(account);
            LOGGER.debug("Saved Account{id={}}", account.getId());

            auditService.debug("%s Account with id: %d via admin panel",
                    id <= 0 ? "Created new" : "Updated", account.getId());

            return Optional.of(account);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not save account", e);

            auditService.error("Failed to save Account");
            return Optional.empty();
        }
    }

    /**
     * Get a user account by id.
     *
     * @return user account optional.
     * @throws NotFoundException if the account is not found.
     */
    public Account getAccount(long id) {
        Account account = accountRepository.findOne(id);
        if (account == null) {

            auditService.warn("Attempted to query non-existing Account with id: %d", id);
            throw new NotFoundException(Account.class, id);
        }
        return account;
    }

    /**
     * Get all accounts.
     *
     * @param includeSelf should currently authenticated users account be included.
     * @return list of accounts, never null.
     */
    List<Account> getAccounts(boolean includeSelf) {
        if (!includeSelf) {
            return accountRepository.findByIdNotIn(AuthenticationUtils.getId());
        }
        return accountRepository.findAll();
    }

    /**
     * Set locked state for the specified account.
     *
     * @param locked should the account be locked or unlocked.
     * @param id     user account id.
     */
    void setLocked(long id, boolean locked) {
        if (AuthenticationUtils.getId() == id) {
            auditService.warn("Attempted to set locked on own account");
            throw new UnauthorizedException("You cannot changed the locked state your own account");
        }
        Account account = accountRepository.findOne(id);
        account.setLocked(locked);
        accountRepository.save(account);

        // If locked is true, log-out that user.
        if (locked) {
            LOGGER.debug("Logging out user: {}", account.getUsername());

            // Expire all sessions of a user.
            sessionRegistry
                    .getAllSessions(account, true)
                    .forEach(SessionInformation::expireNow);
        }
        auditService.info("Set locked Account with id: %d, to locked: %s",
                id, locked);
    }
}