package io.chark.food.app.restaurant;

import io.chark.food.domain.restaurant.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        model.addAttribute("restaurant", new Restaurant());
        return "restaurant/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Restaurant restaurant, Model model) {
        model.addAttribute("restaurant", restaurant);
        return "restaurant/register";
    }
}