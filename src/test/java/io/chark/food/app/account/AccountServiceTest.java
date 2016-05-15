package io.chark.food.app.account;

import io.chark.food.FoodTripIntegrationTest;
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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private InvitationRepository invitationRepository;

    @Resource
    private RestaurantRepository restaurantRepository;

    @Resource
    private AccountRepository accountRepository;

    private AccountService service;

    @Before
    public void setUp() {
        service = new AccountService(
                permissionRepository,
                invitationRepository,
                accountRepository,
                new BCryptPasswordEncoder(),
                Mockito.mock(AuditService.class));
    }

    @After
    public void tearDown() {
        invitationRepository.deleteAll();
        accountRepository.deleteAll();
        restaurantRepository.deleteAll();
        SecurityContextHolder.clearContext();
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

        // Testing for authenticated users.
        service.register(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD)
                .ifPresent(AuthenticationUtils::setAccount);

        service.register("someUser", otherEmail, TEST_PASSWORD);

        // Email taken.
        Account updateDetails = new Account();
        updateDetails.setEmail(otherEmail);

        assertThat(service.update(updateDetails).isPresent()).isFalse();
    }

    @Test
    public void updateDetails() {

        // Testing for authenticated users.
        service.register(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD)
                .ifPresent(AuthenticationUtils::setAccount);

        Account updateDetails = new Account();
        updateDetails.setEmail("other@other.com");
        updateDetails.setName("name");
        updateDetails.setLastName("lastName");
        updateDetails.setPhone("1234");
        updateDetails.setWebsite("www.google.com");
        updateDetails.setBio("bio");

        service.update(updateDetails);

        // Get account from authentication.
        Account account = accountRepository.findOne(AuthenticationUtils.getId());

        // Test if required fields have been updated.
        assertThat(account.getEmail()).isEqualTo(updateDetails.getEmail());
        assertThat(account.getName()).isEqualTo(updateDetails.getName());
        assertThat(account.getLastName()).isEqualTo(updateDetails.getLastName());
        assertThat(account.getBio()).isEqualTo(updateDetails.getBio());
        assertThat(account.getPhone()).isEqualTo(updateDetails.getPhone());
        assertThat(account.getWebsite()).isEqualTo(updateDetails.getWebsite());
    }

    @Test
    public void acceptInvitation() {

        Account account = service.register(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD).get();
        AuthenticationUtils.setAccount(account);

        // Add restaurant to add the invitations to.
        Restaurant restaurant = restaurantRepository.save(new Restaurant("email@email.com", "name", "desc"));

        // Add invitations to this account.
        long id = invitationRepository.save(new Invitation(account.getUsername(), account, restaurant)).getId();
        invitationRepository.save(new Invitation(account.getUsername(), account, restaurant));

        service.acceptInvitation(id);

        // All invitations should be cleared from account.
        assertThat(accountRepository.findOne(account.getId()).hasRestaurant()).isTrue();
        assertThat(accountRepository.findOne(account.getId()).getInvitations()).isEmpty();
    }

    @Test
    public void ignoreInvitation() {

        Account account = service.register(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD).get();
        AuthenticationUtils.setAccount(account);

        // Add restaurant to add the invitations to.
        Restaurant restaurant = restaurantRepository.save(new Restaurant("email@email.com", "name", "desc"));

        // Add invitations to this account.
        long id = invitationRepository.save(new Invitation(account.getUsername(), account, restaurant)).getId();
        assertThat(accountRepository.findOne(account.getId()).getInvitations()).isNotEmpty();

        service.ignoreInvitation(id);

        // Ignored invitation - no longer assigned to account.
        assertThat(accountRepository.findOne(account.getId()).getInvitations()).isEmpty();
    }
}