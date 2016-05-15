package io.chark.food.app.restaurant;

import io.chark.food.app.account.AccountService;
import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.authentication.permission.Permission;
import io.chark.food.domain.restaurant.Restaurant;
import io.chark.food.domain.restaurant.RestaurantRepository;
import io.chark.food.util.authentication.AuthenticationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RestaurantService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantService.class);
    private final RestaurantRepository restaurantRepository;
    private final AccountService accountService;
    private final AuditService auditService;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository,
                             AccountService accountService,
                             AuditService auditService) {

        this.restaurantRepository = restaurantRepository;
        this.accountService = accountService;
        this.auditService = auditService;
    }

    /**
     * Register a new restaurant and set it to current user.
     *
     * @param restaurant restaurant to register.
     * @return restaurant optional.
     */
    public Optional<Restaurant> register(Restaurant restaurant) {

        // Whom to assign the restaurant to.
        Account account = AuthenticationUtils.getAccount();

        // Authentication is required and account must not have a restaurant assigned.
        if (account == null || account.hasRestaurant()) {
            LOGGER.error("Attempted to create a restaurant while not logged in or the user already has a " +
                    "restaurant assigned to him");

            auditService.error("Failed to create a restaurant");
            return Optional.empty();
        }

        try {
            restaurant = restaurantRepository.save(restaurant);
            LOGGER.debug("Created a new restaurant using Account{id={}}", account.getId());

            // Assign account and add worker permission.
            account.setRestaurant(restaurant);
            account.addPermission(accountService.getPermission(Permission.Authority.ROLE_WORKER));
            accountService.save(account);

            // Log more info and return.
            auditService.info("Created a new restaurant with name: %s", restaurant.getName());
            return Optional.of(restaurant);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Failed to create a new Restaurant", e);

            auditService.error("Could not create a new restaurant");
            return Optional.empty();
        }
    }
}