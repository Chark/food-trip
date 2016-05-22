package io.chark.food.app.restaurant.location;


import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.restaurant.location.City;
import io.chark.food.domain.restaurant.location.CityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    private final CityRepository cityRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(CityService.class);

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public Optional<City> register(String name, String country) {
        City city = new City(name, country);

        try {

            city = cityRepository.save(city);


            LOGGER.debug("Created a new city");

        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Failed while creating new city");

            return Optional.empty();
        }
        return Optional.of(city);
    }

    public List<City> getCities(){
        return cityRepository.findAll();
    }
}
