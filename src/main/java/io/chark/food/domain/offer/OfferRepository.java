package io.chark.food.domain.offer;

import io.chark.food.domain.BaseRepository;
import io.chark.food.domain.restaurant.RestaurantDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends BaseRepository<Offer> {

    List<Offer> findByRestaurantId(long id);
}