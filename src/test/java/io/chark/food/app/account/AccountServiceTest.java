package io.chark.food.app.account;

import io.chark.food.FoodTripIntegrationTest;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.authentication.account.AccountRepository;
import io.chark.food.domain.authentication.permission.PermissionRepository;
import io.chark.food.domain.authentication.permission.Permission;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@FoodTripIntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class AccountServiceTest {

    private static final String TEST_USERNAME = "Username";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_EMAIL = "Test@test.com";

    @Resource
    private PermissionRepository permissionRepository;

    @Resource
    private AccountRepository accountRepository;

    private AccountService service;

    @Before
    public void setUp() {
        service = new AccountService(
                permissionRepository,
                accountRepository,
                new BCryptPasswordEncoder());
    }

    @After
    public void tearDown() {
        accountRepository.deleteAll();
    }

    @Test
    public void registerAccount() {
        Optional<Account> registered = service.register(
                TEST_USERNAME,
                TEST_EMAIL,
                TEST_PASSWORD);

        Account account = registered.get();
        assertThat(account.getAuthorities()).containsExactly(permissionRepository
                .findByAuthority(Permission.Authority.ROLE_USER));

        assertThat(account.getRegistrationDate()).isNotNull();
        assertThat(account.getPassword()).isNotEqualTo(TEST_PASSWORD);
        assertThat(account.getPrettyUsername()).isEqualTo(TEST_USERNAME);
        assertThat(account.getEmail()).isEqualTo(TEST_EMAIL.trim().toLowerCase());
        assertThat(account.getUsername()).isEqualTo(TEST_USERNAME.trim().toLowerCase());
    }

    @Test
    public void registerFailed() {
        service.register(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        Optional<Account> registered = service
                .register(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD);

        assertThat(registered.isPresent()).isFalse();
    }

    @Test
    public void updateFailed() {
        String otherEmail = "other@other.com";
        Account account = service.register(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD)
                .get();

        service.register("someUser", otherEmail, TEST_PASSWORD);

        // Email taken.
        Account updateDetails = new Account();
        updateDetails.setEmail(otherEmail);

        assertThat(service.update(account, updateDetails).isPresent()).isFalse();
    }

    @Test
    public void updateDetails() {
        Account account = service.register(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD)
                .get();

        Account updateDetails = new Account();
        updateDetails.setEmail("other@other.com");
        updateDetails.setName("name");
        updateDetails.setLastName("lastName");
        updateDetails.setPhone("1234");
        updateDetails.setWebsite("www.google.com");
        updateDetails.setBio("bio");

        service.update(account, updateDetails);

        // Get account from authentication.
        account = service.getAccount()
                .get();

        // Test if required fields have been updated.
        assertThat(account.getEmail()).isEqualTo(updateDetails.getEmail());
        assertThat(account.getName()).isEqualTo(updateDetails.getName());
        assertThat(account.getLastName()).isEqualTo(updateDetails.getLastName());
        assertThat(account.getBio()).isEqualTo(updateDetails.getBio());
        assertThat(account.getPhone()).isEqualTo(updateDetails.getPhone());
        assertThat(account.getWebsite()).isEqualTo(updateDetails.getWebsite());
    }
}