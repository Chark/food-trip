package io.chark.food.app.administrate;

import io.chark.food.FoodTripIntegrationTest;
import io.chark.food.app.account.AccountService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.authentication.account.AccountRepository;
import io.chark.food.domain.authentication.permission.Permission;
import io.chark.food.domain.authentication.permission.PermissionRepository;
import io.chark.food.util.authentication.AuthenticationUtils;
import io.chark.food.util.exception.NotFoundException;
import io.chark.food.util.exception.UnauthorizedException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private AccountAdministrationService service;

    @Before
    public void setUp() {
        service = new AccountAdministrationService(accountRepository, accountService);
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

        account.setEnabled(false);
        account = service.saveAccount(account.getId(), account, Permission.Authority.ROLE_ADMIN)
                .get();

        assertThat(account.isEnabled()).isFalse();
        assertThat(account.getAuthorities())
                .containsExactly(permissionRepository.findByAuthority(Permission.Authority.ROLE_ADMIN));
    }

    @Test
    public void delete() {

        long initial = accountRepository.count();
        long id = accountService.register(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD)
                .get().getId();

        service.delete(id);
        assertThat(accountRepository.count()).isEqualTo(initial);
    }

    @Test(expected = UnauthorizedException.class)
    public void deleteFailed() {
        Account account = accountService.register(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD)
                .get();

        AuthenticationUtils.setAccount(account);

        // Attempt to delete self.
        service.delete(account.getId());
    }
}