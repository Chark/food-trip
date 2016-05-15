package io.chark.food.app.account;

import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.authentication.account.AccountRepository;
import io.chark.food.domain.authentication.permission.Permission;
import io.chark.food.domain.authentication.permission.PermissionRepository;
import io.chark.food.domain.extras.Color;
import io.chark.food.util.authentication.AuthenticationUtils;
import io.chark.food.util.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static io.chark.food.domain.authentication.permission.Permission.Authority.ROLE_USER;
import static io.chark.food.domain.extras.Color.*;

@Service
public class AccountService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    private final PermissionRepository permissionRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;

    @Autowired
    public AccountService(PermissionRepository permissionRepository,
                          AccountRepository accountRepository,
                          PasswordEncoder passwordEncoder,
                          AuditService auditService) {

        this.permissionRepository = permissionRepository;

        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditService = auditService;
    }

    /**
     * Initialize roles used by the system.
     */
    @PostConstruct
    public void init() {
        addPermission("User", ROLE_USER, DEFAULT);
        addPermission("Worker", Permission.Authority.ROLE_WORKER, BLUE);
        addPermission("Moderator", Permission.Authority.ROLE_MODERATOR, YELLOW);
        addPermission("Administrator", Permission.Authority.ROLE_ADMIN, RED);

        // Init default admin account if there are no accounts defined.
        initAdmin();

        // Register some dummy accounts.
        for (int i = 0; i < 10; i++) {
            register("dummy" + i, "dummy" + i + "@dummy.com", "password");
        }
    }

    /**
     * Register a new user Account.
     *
     * @param username pretty username.
     * @param email    email of the account.
     * @param password raw password.
     * @return created account or empty optional.
     */
    public Optional<Account> register(String username, String email, String password) {
        Account account = new Account(
                username,
                email,
                passwordEncoder.encode(password));

        account.addPermission(getPermission(ROLE_USER));

        // todo - enable only via email verification!
        account.setEnabled(true);
        try {
            account = accountRepository.save(account);
            LOGGER.debug("Created new user Account{username='{}', email='{}'}",
                    username, email);

            auditService.info("Created a new Account using username: %s, email: %s", username, email);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Failed while creating new user Account{username='{}', email='{}'}",
                    username, email, e);

            auditService.error("Failed to create a new Account using username: %s, email: %s", username, email);
            return Optional.empty();
        }
        return Optional.of(account);
    }

    /**
     * Update current logged in users account details.
     *
     * @return updated account optional.
     */
    Optional<Account> update(Account updateDetails) {
        Account account = AuthenticationUtils.getAccount();

        if (account == null) {
            auditService.warn("Attempted to update account details while being non authenticated");
            return Optional.empty();
        }

        // Update details regularly.
        Optional<Account> optional = update(accountRepository
                .findOne(account.getId()), updateDetails);

        // Update authentication, since we're using that for getting profile data.
        optional.ifPresent(a -> {
            AuthenticationUtils.setAccount(a);
            auditService.debug("Updated account details");
        });
        return optional;
    }

    /**
     * Update specified users account details.
     *
     * @param account       account which is to be updated.
     * @param updateDetails detail which are used in updating.
     * @return updated account optional.
     */
    public Optional<Account> update(Account account, Account updateDetails) {

        // Update password only if specified.
        if (updateDetails.getPassword() != null && !updateDetails.getPassword().isEmpty()) {
            account.setPassword(passwordEncoder.encode(updateDetails.getPassword()));
        }

        // Update other stuff.
        account.setEmail(updateDetails.getEmail());
        account.setBio(updateDetails.getBio());
        account.setName(updateDetails.getName());
        account.setLastName(updateDetails.getLastName());
        account.setWebsite(updateDetails.getWebsite());
        account.setPhone(updateDetails.getPhone());

        try {

            // Email might duplicate, or other db exceptions.
            LOGGER.debug("Updating Account{id={}} details", account.getId());
            return Optional.ofNullable(accountRepository.save(account));
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not update account", e);

            auditService.error("Failed to update account details");
            return Optional.empty();
        }
    }

    @Override
    public Account loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("Account not found");
        }
        return account;
    }

    /**
     * Initialize an admin account if there are no account accounts.
     */
    private void initAdmin() {
        if (accountRepository.count() == 0) {
            Account account = register("Admin", "admin@admin.com", "admin")
                    .get();

            account.setEnabled(true);

            // Admin has all authorities.
            for (Permission.Authority authority : Permission.Authority.values()) {
                account.addPermission(getPermission(authority));
            }

            LOGGER.info("Initializing default admin account");
            accountRepository.save(account);
        }
    }

    /**
     * Get permission by authority.
     *
     * @param authority authority to look the permission by.
     * @return the permission.
     */
    public Permission getPermission(Permission.Authority authority) {
        return permissionRepository.findByAuthority(authority);
    }

    /**
     * Get a set of permissions by querying them by provided authority list.
     *
     * @param authorities authorities to query the permissions by.
     * @return set of permissions, never null.
     */
    public Set<Permission> getPermissions(Permission.Authority... authorities) {
        if (authorities == null || authorities.length == 0) {
            return Collections.emptySet();
        }
        return permissionRepository.findByAuthorityIn(authorities);
    }

    /**
     * Save provided account instance and put in authentication context.
     *
     * @param account account to save.
     * @throws UnauthorizedException if unauthenticated user called this method.
     */
    public void save(Account account) {
        if (AuthenticationUtils.getAccount() == null) {
            throw new UnauthorizedException("Must be authenticated to call this method");
        }

        LOGGER.debug("Saving Account{id={}} and updating authentication", account.getId());
        account = accountRepository.save(account);
        AuthenticationUtils.setAccount(account);
    }

    /**
     * Create a new permission in the system.
     *
     * @param name      name of the permission.
     * @param authority permission authority.
     * @param color     permission name color.
     */
    private void addPermission(String name, Permission.Authority authority, Color color) {
        LOGGER.info("Creating new Permission{name='{}', authority='{}'}",
                name, authority);

        permissionRepository.save(new Permission(name, authority, color));
    }
}