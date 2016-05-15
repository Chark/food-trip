package io.chark.food.app.restaurant;

import io.chark.food.FoodTripIntegrationTest;
import io.chark.food.app.account.AccountService;
import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.authentication.account.AccountRepository;
import io.chark.food.domain.restaurant.Restaurant;
import io.chark.food.domain.restaurant.RestaurantRepository;
import io.chark.food.util.authentication.AuthenticationUtils;
import io.chark.food.util.exception.NotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

@FoodTripIntegrationTest
@RunWith(SpringJUnit4ClassRunner.class)
public class RestaurantServiceTest {

    private static final String TEST_USERNAME = "Username";
    private static final String TEST_PASSWORD = "password";
    private static final String TEST_EMAIL = "Test@test.com";

    @Resource
    private RestaurantRepository restaurantRepository;

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private AccountService accountService;

    @Resource
    private PasswordEncoder passwordEncoder;

    private RestaurantService service;
    private Account account;

    @Before
    public void setUp() {

        // Test account.
        account = accountRepository
                .save(new Account(TEST_USERNAME, TEST_EMAIL, passwordEncoder.encode(TEST_PASSWORD)));

        // Drop account into auth context.
        AuthenticationUtils.setAccount(account);

        service = new RestaurantService(
                restaurantRepository,
                accountService,
                Mockito.mock(AuditService.class));
    }

    @After
    public void tearDown() {
        accountRepository.deleteAll();
        restaurantRepository.deleteAll();
        SecurityContextHolder.clearContext();
    }

    @Test
    public void register() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("name");
        restaurant.setEmail("email@email.com");
        restaurant = service.register(restaurant).get();

        assertThat(AuthenticationUtils.getAccount().getRestaurant()).isNotNull();
        assertThat(restaurantRepository.findOne(restaurant.getId()).getAccounts()).hasSize(1);
    }

    @Test
    public void registerFailedNoAuth() {

        SecurityContextHolder.clearContext();

        // No auth - failed.
        Restaurant restaurant = new Restaurant();
        restaurant.setName("name");
        restaurant.setEmail("email@email.com");
        assertThat(service.register(restaurant)).isNotPresent();
    }


    @Test
    public void registerFailed() {

        // Account has one restaurant.
        Restaurant restaurant = new Restaurant();
        restaurant.setName("name");
        restaurant.setEmail("email@email.com");
        assertThat(service.register(restaurant)).isPresent();

        // Attempt to register a second one - failed.
        restaurant = new Restaurant();
        restaurant.setName("name2");
        restaurant.setEmail("email2@email.com");
        assertThat(service.register(restaurant)).isNotPresent();
    }

    @Test
    public void getRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("name");
        restaurant.setEmail("email@email.com");
        service.register(restaurant);

        assertThat(service.getRestaurant()).isNotNull();
    }

    @Test(expected = NotFoundException.class)
    public void getRestaurantFailed() {

        // No restaurant assigned to this user.
        service.getRestaurant();
    }

    @Test
    public void update() {

        // Register a new restaurant.
        Restaurant restaurant = new Restaurant();
        restaurant.setName("name");
        restaurant.setEmail("email@email.com");
        service.register(restaurant);

        String name = "newName";
        String email = "newEmail";
        String description = "newDescription";

        // Change vital properties.
        restaurant.setEmail(email);
        restaurant.setName(name);
        restaurant.setDescription(description);

        // Assert what was changed.
        restaurant = service.update(restaurant).get();
        assertThat(restaurant.getName()).isEqualTo(name);
        assertThat(restaurant.getEmail()).isEqualToIgnoringCase(email);
        assertThat(restaurant.getDescription()).isEqualTo(description);
    }
}