package io.chark.food.app.restaurant.offer;

import io.chark.food.app.restaurant.RestaurantService;
import io.chark.food.domain.offer.Offer;
import io.chark.food.domain.offer.OfferRepository;
import io.chark.food.domain.restaurant.Restaurant;
import io.chark.food.domain.restaurant.RestaurantRepository;
import io.chark.food.domain.restaurant.newsletter.Newsletter;
import io.chark.food.domain.restaurant.newsletter.NewsletterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class OfferService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OfferService.class);

    private final NewsletterRepository newsletterRepository;
    private final RestaurantRepository restaurantRepository;
    private final OfferRepository offerRepository;
    private final RestaurantService restaurantService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public OfferService(OfferRepository offerRepository,
                        NewsletterRepository newsletterRepository,
                        RestaurantRepository restaurantRepository,
                        RestaurantService restaurantService) {

        this.newsletterRepository = newsletterRepository;
        this.restaurantRepository = restaurantRepository;
        this.restaurantService = restaurantService;
        this.offerRepository = offerRepository;
    }

    public Optional<Offer> register(String validThrough, String description, String headline) {
        Offer offer = new Offer(validThrough, description, headline);
        if (offer.getValidThroughDate().getTime() > new Date().getTime()) {
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
        }
        return Optional.of(offer);
    }

    public Optional<Offer> register(Offer offer) {
        return register(offer.getValidThrough(), offer.getDescription(), offer.getHeadline());
    }


    public Offer getOffer(long id) {
        return offerRepository.findOne(id);
    }

    public List<Offer> getOffers() {
        Restaurant restaurant = restaurantService.getRestaurant();
        return offerRepository.findByRestaurantId(restaurant.getId());
    }

    public List<Offer> getAllOffers()
    {
        return offerRepository.findAll();
    }


    public Optional<Offer> update(long id, Offer updateDetails) {
        Offer offer = offerRepository.findOne(id);

        offer.setDescription(updateDetails.getDescription());
        offer.setHeadline(updateDetails.getHeadline());
        offer.setPublicationDate(new Date());
        offer.setValidThrough(updateDetails.getValidThrough());

        try {
            offer = offerRepository.save(offer);

            return Optional.of(offer);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not update restaurant details", e);
            return Optional.empty();
        }
    }

    public void deleteOffer(long id) {
        Offer offer = offerRepository.findOne(id);

        Restaurant restaurant = offer.getRestaurant();
        if (restaurant != null) {
            restaurant.removeOffer(offer);
        }
        restaurantRepository.save(restaurant);

        List<Newsletter> newsletters = newsletterRepository.findByOfferId(offer.getId());
        for (Newsletter newsletter : newsletters) {
            newsletter.removeOffer(offer);
        }
        newsletterRepository.save(newsletters);

        offerRepository.delete(id);
    }

    public List<Offer> getnOffers(int max){
        return entityManager.createQuery("SELECT a FROM Offer a ORDER BY a.validThrough asc", Offer.class)
                .setMaxResults(max)
                .getResultList();
    }

}