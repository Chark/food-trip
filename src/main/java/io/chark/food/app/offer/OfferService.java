package io.chark.food.app.offer;

import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.app.restaurant.RestaurantService;
import io.chark.food.domain.offer.Offer;
import io.chark.food.domain.offer.OfferRepository;
import io.chark.food.domain.restaurant.Restaurant;
import io.chark.food.domain.restaurant.RestaurantDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class OfferService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OfferService.class);
    private final OfferRepository offerRepository;
    private final RestaurantService restaurantService;
    @Autowired
    public OfferService(OfferRepository offerRepository,AuditService auditService,RestaurantService restaurantService )
    {
        this.restaurantService = restaurantService;
        this.offerRepository = offerRepository;
    }

    public Optional<Offer> register(String validThrough, String description, String headline) {
        Offer offer = new Offer(validThrough,description,headline);

            offer.setRestaurant(restaurantService.getRestaurant());
        try {

            offer = offerRepository.save(offer);

            Restaurant restaurant = restaurantService.getRestaurant();
            restaurant.addOffer(offer);
            restaurantService.saveOffer(restaurant);

//            LOGGER.debug("Created new Offer {title='{}'}", title);
            LOGGER.debug("Created new Offer ");

        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Failed while creating new Offer{title='{}'}", e);

//            auditService.error("Failed to create a Thread using title: %s", title);
            return Optional.empty();
        }
        return Optional.of(offer);
    }

    public Optional<Offer> register(Offer offer){
        return register(offer.getValidThrough(), offer.getDescription(), offer.getHeadline());
    }




}
