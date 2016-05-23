package io.chark.food.app.restaurant;

import io.chark.food.app.account.AccountService;
import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.app.restaurant.details.RestaurantDetailsService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.authentication.permission.Permission;
import io.chark.food.domain.offer.Offer;
import io.chark.food.domain.restaurant.*;
import io.chark.food.domain.restaurant.location.Location;
import io.chark.food.domain.restaurant.location.LocationRepository;
import io.chark.food.util.authentication.AuthenticationUtils;
import io.chark.food.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class RestaurantService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantService.class);
    private final RestaurantAuditService restaurantAuditService;
    private final InvitationRepository invitationRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantDetailsService restaurantDetailsService;
    private final AccountService accountService;
    private final AuditService auditService;
    private final LocationRepository locationRepository;

    @Autowired
    public RestaurantService(RestaurantAuditService restaurantAuditService,
                             InvitationRepository invitationRepository,
                             RestaurantRepository restaurantRepository,
                             AccountService accountService,
                             AuditService auditService,
                             RestaurantDetailsService restaurantDetailsService,
                             LocationRepository locationRepository) {

        this.restaurantAuditService = restaurantAuditService;
        this.invitationRepository = invitationRepository;
        this.restaurantRepository = restaurantRepository;
        this.accountService = accountService;
        this.auditService = auditService;
        this.restaurantDetailsService = restaurantDetailsService;
        this.locationRepository = locationRepository;
    }

    /**
     * Update currently authenticated users restaurant.
     *
     * @param updateDetails update details.
     * @return restaurant optional.
     */
    public Optional<Restaurant> update(Restaurant updateDetails) {
        Restaurant restaurant = getRestaurant();

        // Set required details.
        restaurant.setEmail(updateDetails.getEmail());
        restaurant.setName(updateDetails.getName());
        restaurant.setDescription(updateDetails.getDescription());

        RestaurantDetails details = restaurant.getRestaurantDetails();
        details.setRegistrationCode(updateDetails.getRestaurantDetails().getRegistrationCode());
        details.setFax(updateDetails.getRestaurantDetails().getFax());
        details.setManager(updateDetails.getRestaurantDetails().getManager());
        details.setMobileNumber(updateDetails.getRestaurantDetails().getMobileNumber());
        details.setVat(updateDetails.getRestaurantDetails().getVat());
        details.setWebsite(updateDetails.getRestaurantDetails().getWebsite());
        details.setPhoneNumber(updateDetails.getRestaurantDetails().getPhoneNumber());
        details.setBank(updateDetails.getRestaurantDetails().getBank());
        details.setBankAccountNumber(updateDetails.getRestaurantDetails().getBankAccountNumber());

        if (updateDetails.getLocation() != null) {
            Location location = locationRepository.save(updateDetails.getLocation());
            restaurant.setLocation(location);
        }

        try {
            restaurant = restaurantRepository.save(restaurant);
            restaurantAuditService.info("Updated restaurant details", "Update");

            return Optional.of(restaurant);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not update restaurant details", e);

            restaurantAuditService.warn("Failed to update restaurant details", "Update");
            return Optional.empty();
        }
    }

    /**
     * Register a new restaurant and set it to current user.
     *
     * @param restaurant restaurant to register.
     * @return restaurant optional.
     */
    public Optional<Restaurant> register(Restaurant restaurant) {

        // Whom to assign the restaurant to.
        Account account = accountService.getAccount();

        // Authentication is required and account must not have a restaurant assigned.
        if (account == null || account.hasRestaurant()) {
            LOGGER.error("Attempted to create a restaurant while not logged in or the user already has a " +
                    "restaurant assigned to him");

            auditService.error("Failed to create a restaurant");
            return Optional.empty();
        }

        Random random = new Random();

        try {

            Optional<RestaurantDetails> restaurantDetails = restaurantDetailsService.register(
                    "+37067715464",
                    "LT95569514646" + random.nextInt(9999999),
                    "LT8989898" + random.nextInt(9999999),
                    "989895526" + random.nextInt(9999999),
                    "Jonas Ketvirtis");

            restaurant.setRestaurantDetails(restaurantDetails.get());

            // Default restaurant rating and hygiene level.
            restaurant.setRating(1);
            restaurant.setHygieneLevel(5);

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
            return Optional.empty();
        }
    }

    /**
     * Get restaurant which is assigned to the currently authenticated user.
     *
     * @return restaurant.
     * @throws NotFoundException if user has no restaurant assigned to him.
     */
    public Restaurant getRestaurant() {

        Restaurant restaurant = restaurantRepository
                .findByAccountId(AuthenticationUtils.getIdOrThrow());

        if (restaurant == null) {
            throw new NotFoundException("No restaurant is found on this user");
        }
        return restaurant;
    }

    /**
     * Invite a user to your restaurant.
     *
     * @param username user to invite.
     */
    public Optional<Invitation> invite(String username) {
        if (username == null || username.isEmpty()) {
            LOGGER.error("Cannot create invitation for an empty username");
            return Optional.empty();
        }

        // Check if not inviting yourself.
        Account account = accountService.getAccount();
        if (account == null || account.getUsername().equals(username)) {
            LOGGER.error("Cannot invite yourself");
            return Optional.empty();
        }

        // User already has a restaurant or is null.
        account = accountService.getAccount(username);
        if (account == null || account.hasRestaurant()) {
            account = null;
        }

        // Create invitation.
        restaurantAuditService.info("Inviting %s to join the restaurant", "Invitation", username);
        return Optional.of(invitationRepository
                .save(new Invitation(username, account, getRestaurant())));
    }

    public void saveOffer(Restaurant restaurant){
        restaurantRepository.save(restaurant);
    }
    /**
     * Get all available restaurants.
     *
     * @return restaurant list.
     */
    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }

    /**
     * Get restaurant by id.
     *
     * @param id restaurant id.
     * @return restaurant.
     */
    public Restaurant getRestaurant(long id) {
        Restaurant restaurant = restaurantRepository.findOne(id);
        if (restaurant == null) {
            throw new NotFoundException("No restaurant found with id: %s", id);
        }
        return restaurant;
    }

    /**
     * Delete invitation by id and restaurant.
     *
     * @param id invitation id.
     */
    void deleteInvitation(long id) {
        restaurantAuditService.warn("Deleting invitation with id: %d", "Invitation", id);
        invitationRepository.deleteByRestaurantAndId(getRestaurant().getId(), id);
    }
}