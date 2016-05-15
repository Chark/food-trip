package io.chark.food.app.restaurant;

import io.chark.food.domain.restaurant.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping(value = "/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    /**
     * Get the restaurant template to which the account belongs to.
     *
     * @return restaurant profile template.
     */
    @RequestMapping(method = RequestMethod.GET)
    public String restaurant(Model model) {
        model.addAttribute("restaurant", restaurantService.getRestaurant());
        return "restaurant/profile";
    }

    /**
     * Edit restaurant profile.
     *
     * @return restaurant profile template.
     */
    @RequestMapping(method = RequestMethod.POST)
    public String restaurant(Restaurant details, Model model) {

        if (restaurantService.update(details).isPresent()) {
            // Success, redirect to profile page.
            return "redirect:/restaurant";
        }

        // Error, failed to update.
        model.addAttribute("error", "Could not update restaurant details, " +
                "please double check what you've entered");

        model.addAttribute("restaurant", details);
        return "restaurant/profile";
    }

    /**
     * Get the restaurant register template.
     *
     * @return restaurant register template.
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        model.addAttribute("restaurant", new Restaurant());
        return "restaurant/register";
    }

    /**
     * Register a new restaurant.
     *
     * @return same restaurant register template on error or newly created restaurant.
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@Valid Restaurant restaurant,
                           RedirectAttributes attributes,
                           Model model) {

        Optional<Restaurant> optional = restaurantService.register(restaurant);

        // Register was a success.
        if (optional.isPresent()) {
            attributes.addFlashAttribute("success", "Your new restaurant profile is created");
            return "redirect:/restaurant";
        }

        // Failed, try again.
        model.addAttribute("restaurant", restaurant);
        model.addAttribute("error", "Failed to register restaurant, please check if the name" +
                " and email is not taken");

        return "restaurant/register";
    }
}