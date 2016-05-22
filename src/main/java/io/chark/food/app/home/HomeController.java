package io.chark.food.app.home;

import io.chark.food.app.account.AccountService;
import io.chark.food.app.article.ArticleService;
import io.chark.food.app.restaurant.RestaurantService;
import io.chark.food.domain.authentication.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/")
public class HomeController {

    private final RestaurantService restaurantService;
    private final ArticleService articleService;
    private final AccountService accountService;

    @Autowired
    public HomeController(RestaurantService restaurantService,
                          ArticleService articleService,
                          AccountService accountService) {

        this.restaurantService = restaurantService;
        this.articleService = articleService;
        this.accountService = accountService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("articles", articleService.getArticles());
        model.addAttribute("accounts", accountService.getTop10Accounts());
        model.addAttribute("restaurants", restaurantService.getRestaurants());
        return "home/home";
    }
}