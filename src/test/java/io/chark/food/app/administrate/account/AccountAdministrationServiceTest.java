package io.chark.food.app.administrate.account;

import io.chark.food.FoodTripIntegrationTest;
import io.chark.food.app.account.AccountService;
import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.authentication.account.AccountRepository;
import io.chark.food.domain.authentication.permission.Permission;
import io.chark.food.domain.authentication.permission.PermissionRepository;
import io.chark.food.domain.restaurant.Invitation;
import io.chark.food.domain.restaurant.InvitationRepository;
import io.chark.food.domain.restaurant.Restaurant;
import io.chark.food.domain.restaurant.RestaurantRepository;
import io.chark.food.util.authentication.AuthenticationUtils;
import io.chark.food.util.exception.NotFoundException;
import io.chark.food.util.exception.UnauthorizedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

@FoodTripIntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class AccountAdministrationServiceTest {

    private static final String TEST_USERNAME = "Username";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_EMAIL = "Test@test.com";


    @Resource
    private PermissionRepository permissionRepository;

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private AccountService accountService;

    @Resource
    private SessionRegistry sessionRegistry;

    private AccountAdministrationService service;

    @Before
    public void setUp() {
        service = new AccountAdministrationService(
                accountRepository,
                sessionRegistry,
                accountService,
                Mockito.mock(AuditService.class));
    }

    @After
    public void tearDown() {
        accountRepository.deleteAll();
        SecurityContextHolder.clearContext();
    }

    @Test(expected = NotFoundException.class)
    public void getAccount() {
        service.getAccount(-1);
    }

    @Test
    public void createNew() {

        long preCreation = accountRepository.count();
        Account account = new Account(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        account.setEnabled(false);
        account = service.saveAccount(0, account, Permission.Authority.ROLE_ADMIN)
                .get();

        // New account created, increased by one.
        assertThat(accountRepository.count()).isEqualTo(preCreation + 1);
        assertThat(account.isEnabled()).isFalse();
        assertThat(account.getAuthorities())
                .containsExactly(permissionRepository.findByAuthority(Permission.Authority.ROLE_ADMIN));
    }

    @Test
    public void updateExisting() {
        Account account = accountService.register(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD)
                .get();

        String newUsername = "toptest";
        account.setEnabled(false);
        account.setUsername(newUsername);
        account = service.saveAccount(account.getId(), account, Permission.Authority.ROLE_ADMIN)
                .get();

        assertThat(account.getUsername()).isEqualTo(newUsername);
        assertThat(account.isEnabled()).isFalse();
        assertThat(account.getAuthorities())
                .containsExactly(permissionRepository.findByAuthority(Permission.Authority.ROLE_ADMIN));
    }

    @Test
    public void lock() {
        Account account = accountService.register(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD)
                .get();

        service.setLocked(account.getId(), true);
        assertThat(accountRepository.findOne(account.getId()).isAccountNonLocked()).isFalse();
    }

    @Test(expected = UnauthorizedException.class)
    public void lockSelf() {
        Account account = accountService.register(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD)
                .get();

        AuthenticationUtils.setAccount(account);

        // Attempt to setLocked self.
        service.setLocked(account.getId(), true);
    }
}