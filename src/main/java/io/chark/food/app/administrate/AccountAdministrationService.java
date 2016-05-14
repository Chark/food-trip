package io.chark.food.app.administrate;

import io.chark.food.app.account.AccountService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.authentication.account.AccountRepository;
import io.chark.food.domain.authentication.permission.Permission;
import io.chark.food.util.authentication.AuthenticationUtils;
import io.chark.food.util.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountAdministrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountAdministrationService.class);

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @Autowired
    public AccountAdministrationService(AccountRepository accountRepository,
                                        AccountService accountService) {

        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    /**
     * Create a new account or update an existing one based on id.
     *
     * @param id            account id.
     * @param updateDetails details used in creation or updating.
     * @param authorities   account authorities to set or update.
     * @return account optional.
     */
    public Optional<Account> saveAccount(long id,
                                         Account updateDetails,
                                         Permission.Authority[] authorities) {

        // Below or equals means this is a new account.
        Optional<Account> optional;
        if (id <= 0) {

            // Reuse the register method to save a new account.
            optional = accountService.register(
                    updateDetails.getUsername(),
                    updateDetails.getEmail(),
                    updateDetails.getPassword());

        } else {

            // Existing account.
            optional = Optional.of(accountRepository.findOne(id));
        }

        // No account found, error.
        if (!optional.isPresent()) {
            return Optional.empty();
        }

        // Update account details.
        optional = accountService.update(optional.get(), updateDetails);

        // Update other details editable only by admins.
        Account account = optional.get();
        account.setEnabled(updateDetails.isEnabled());
        account.setPermissions(accountService.getPermissions(authorities));

        try {
            optional = Optional.of(accountRepository.save(account));
            LOGGER.debug("Save account Account{id={}}", account.getId());
            return optional;
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not save account", e);
            return Optional.empty();
        }
    }

    /**
     * Get a user account by id.
     *
     * @return user account optional.
     */
    public Optional<Account> getAccount(long id) {
        return Optional.of(accountRepository.findOne(id));
    }

    /**
     * Get all accounts.
     *
     * @param includeSelf should currently authenticated users account be included.
     * @return list of accounts, never null.
     */
    public List<Account> getAccounts(boolean includeSelf) {
        if (!includeSelf) {
            return accountRepository.findByIdNotIn(AuthenticationUtils.getId());
        }
        return accountRepository.findAll();
    }

    /**
     * Delete specified user account.
     *
     * @param id user account id.
     */
    public void delete(long id) {
        if (AuthenticationUtils.getId() == id) {
            throw new UnauthorizedException("You cannot delete your own account");
        }
        accountRepository.delete(id);
    }
}