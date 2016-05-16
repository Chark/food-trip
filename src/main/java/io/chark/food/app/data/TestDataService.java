package io.chark.food.app.data;

import io.chark.food.app.account.AccountService;
import io.chark.food.app.restaurant.RestaurantService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.restaurant.Restaurant;
import io.chark.food.util.authentication.AuthenticationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Future;

/**
 * Component to seed database with test data.
 */
@Service
public class TestDataService {

    /**
     * Default password used for username's and such.
     */
    private static final String DEFAULT_PASSWORD = "password";

    /**
     * Default description for all the stuff.
     */
    private static final String DEFAULT_DESCRIPTION = "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
            " Curabitur tincidunt purus urna, non gravida leo porttitor vel. Morbi vel dui quis elit aliquet" +
            " molestie. Morbi ut blandit purus. Donec in tortor mauris. Proin tincidunt aliquam auctor. ";

    private static final Logger LOGGER = LoggerFactory.getLogger(TestDataService.class);
    private final RestaurantService restaurantService;
    private final AccountService accountService;

    // Username's to initialize.
    private final List<String> usernameList;

    // Restaurants to initialize.
    private final List<String> restaurantNameList;

    // Add some randomness to test data.
    private final Random random;

    @Autowired
    public TestDataService(RestaurantService restaurantService,
                           AccountService accountService) {

        this.restaurantService = restaurantService;
        this.accountService = accountService;
        this.random = new Random();

        // Test username's.
        this.usernameList = new ArrayList<>();
        this.usernameList.addAll(Arrays.asList(
                "CoolGuy",
                "FoodLover69",
                "Mr.Meeseeks",
                "JohnDoe",
                "AAaaeeeoouuu",
                "ThatOtherGuy",
                "SuperMega",
                "Padla",
                "Baunyzas",
                "Ugnius",
                "Arvydas",
                "Erlandas",
                "Edvinas"
        ));

        // Test restaurant names.
        this.restaurantNameList = new ArrayList<>();
        this.restaurantNameList.addAll(Arrays.asList(
                "Kebabs",
                "Super Duper pizza place",
                "Not so cool pizza place",
                "Pizzas",
                "Just awesome"
        ));
    }

    /**
     * Fill database with test data.
     */
    @Async
    public Future<Void> initTestData() {

        LOGGER.info("Initializing test data");

        // Initialize main test accounts.
        List<Account> accounts = initTestAccounts();

        // Initialize main restaurants and their test accounts.
        initTestRestaurants(accounts);

        LOGGER.info("Finished initializing test data");
        return new AsyncResult<>(null);
    }

    /**
     * Register a list of tests accounts and return their instances.
     *
     * @return list of test accounts.
     */
    private List<Account> initTestAccounts() {
        List<Account> accounts = new ArrayList<>();
        for (String username : this.usernameList) {
            Optional<Account> account = accountService
                    .register(username, String.format("%s@food.com", username), DEFAULT_PASSWORD);

            // Add account to account list if present.
            account.ifPresent(accounts::add);
        }
        return accounts;
    }

    /**
     * Create a list of restaurants.
     *
     * @return list of test restaurants.
     */
    private List<Restaurant> initTestRestaurants(List<Account> accounts) {

        // There must be more accounts than restaurants.
        List<Restaurant> restaurants = new ArrayList<>();
        for (String name : this.restaurantNameList) {

            String email = String.format("%s@food.com", name)
                    .replaceAll(" ", "");

            // Restaurant owner.
            Optional<Account> account = accountService.register(name.replaceAll(" ", ""), email, DEFAULT_PASSWORD);
            if (account.isPresent()) {

                // Need an account in authentication for new restaurant registering.
                AuthenticationUtils.setAccount(account.get());

                Optional<Restaurant> restaurant = restaurantService
                        .register(new Restaurant(email, name, DEFAULT_DESCRIPTION));

                // Add restaurant to restaurant list if present.
                if (restaurant.isPresent()) {

                    // Invite some users to join this restaurant.
                    for (Account invite : accounts) {

                        // Should we invite this account?
                        if (random.nextBoolean()) {
                            restaurantService.invite(invite.getUsername());
                        }
                    }
                    restaurants.add(restaurant.get());
                }
            }

            // Cleanup authentication.
            SecurityContextHolder.clearContext();
        }
        return restaurants;
    }
}