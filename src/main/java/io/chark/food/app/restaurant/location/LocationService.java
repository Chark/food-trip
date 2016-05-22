package io.chark.food.app.restaurant.location;


import io.chark.food.app.restaurant.RestaurantService;
import io.chark.food.domain.restaurant.location.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private final RestaurantService restaurantService;

    @Autowired
    public LocationService(LocationRepository locationRepository, RestaurantService restaurantService) {
        this.locationRepository = locationRepository;
        this.restaurantService = restaurantService;
    }



}
