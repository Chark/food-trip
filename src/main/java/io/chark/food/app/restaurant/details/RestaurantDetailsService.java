package io.chark.food.app.restaurant.details;


import io.chark.food.app.restaurant.RestaurantService;
import io.chark.food.domain.restaurant.Restaurant;
import io.chark.food.domain.restaurant.RestaurantDetails;
import io.chark.food.domain.restaurant.RestaurantDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class RestaurantDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestaurantDetailsService.class);
    private final RestaurantDetailsRepository restaurantDetailsRepository;

    @Autowired
    public RestaurantDetailsService(RestaurantDetailsRepository restaurantDetailsRepository) {
        this.restaurantDetailsRepository = restaurantDetailsRepository;
    }

    public Optional<RestaurantDetails> register(String mobileNumber, String bankAccountNumber, String vat, String registrationCode, String manager) {
        RestaurantDetails restaurantDetails = new RestaurantDetails(mobileNumber,
                bankAccountNumber,
                vat,
                registrationCode,
                manager
                );

//        restaurantDetails.setRestauant(restaurant);

        try {

            restaurantDetails = restaurantDetailsRepository.save(restaurantDetails);


//            LOGGER.debug("Created new Restaurant details {title='{}'}", title);
            LOGGER.debug("Created new Restaurant details ");

//            auditService.info("Created a new Thread using title: %s", title);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Failed while creating new Thread{title='{}'}", e);

//            auditService.error("Failed to create a Thread using title: %s", title);
            return Optional.empty();
        }
        return Optional.of(restaurantDetails);
    }


    public Optional<RestaurantDetails> update(RestaurantDetails updateDetails) {

        RestaurantDetails restaurantDetails = restaurantDetailsRepository.findOne(updateDetails.getId());



        restaurantDetails.setBankAccountNumber(updateDetails.getBankAccountNumber());
        restaurantDetails.setFax(updateDetails.getFax());
        restaurantDetails.setManager(updateDetails.getManager());
        restaurantDetails.setMobileNumber(updateDetails.getMobileNumber());
        restaurantDetails.setRegistrationCode(updateDetails.getRegistrationCode());
        restaurantDetails.setVat(updateDetails.getRegistrationCode());
        restaurantDetails.setWebsite(updateDetails.getWebsite());

        try {
            restaurantDetails = restaurantDetailsRepository.save(restaurantDetails);
            LOGGER.debug("Restaurant details updated");
            return Optional.of(restaurantDetails);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not update restaurant details", e);

            return Optional.empty();
        }
    }

}
