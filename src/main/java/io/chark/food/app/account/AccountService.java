package io.chark.food.app.account;

import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.authentication.account.AccountRepository;
import io.chark.food.domain.authentication.permission.Permission;
import io.chark.food.domain.authentication.permission.PermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

import static io.chark.food.domain.authentication.permission.Permission.Authority.ROLE_USER;

@Service
public class AccountService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    private final PermissionRepository permissionRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(PermissionRepository permissionRepository,
                          AccountRepository accountRepository,
                          PasswordEncoder passwordEncoder) {

        this.permissionRepository = permissionRepository;

        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Initialize roles used by the system.
     */
    @PostConstruct
    public void init() {
        addPermission("User", ROLE_USER);
        addPermission("Worker", Permission.Authority.ROLE_WORKER);
        addPermission("Moderator", Permission.Authority.ROLE_MODERATOR);
        addPermission("Administrator", Permission.Authority.ROLE_ADMIN);

        // Init default admin account if there are no accounts defined.
        initAdmin();
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

        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Failed while creating new user Account{username='{}', email='{}'}",
                    username, email, e);

            return Optional.empty();
        }
        return Optional.of(account);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("Account not found");
        }
        return account;
    }

    /**
     * Initialize an admin account if there are no account accounts.
     */
    public void initAdmin() {
        if (accountRepository.count() == 0) {
            Account account = register("admin", "admin@admin.com", "admin")
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
    private Permission getPermission(Permission.Authority authority) {
        return permissionRepository.findByAuthority(authority);
    }

    /**
     * Create a new permission in the system.
     *
     * @param name      name of the permission.
     * @param authority permission authority.
     */
    private void addPermission(String name, Permission.Authority authority) {
        LOGGER.info("Creating new Permission{name='{}', authority='{}'}",
                name, authority);

        permissionRepository.save(new Permission(name, authority));
    }
}