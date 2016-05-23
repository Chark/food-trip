package io.chark.food.app.restaurant.newsletter;

import io.chark.food.domain.restaurant.newsletter.Newsletter;
import io.chark.food.domain.restaurant.newsletter.NewsletterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/newsletter")
public class NewsletterController {
    private final NewsletterRepository newsletterRepository;

    @Autowired
    public NewsletterController(NewsletterRepository newsletterRepository) {
        this.newsletterRepository = newsletterRepository;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getNewsletter(@PathVariable long id, Model model) {
        Newsletter newsletter = newsletterRepository.findOne(id);
        if (newsletter != null) {
            if (!newsletter.isPublished()) {
                model.addAttribute("published", false);
            } else {
                model.addAttribute("published", true);
                model.addAttribute("newsletter", newsletter);
            }
        } else {
            model.addAttribute("published", false);
        }


        return "restaurant/newsletter";

    }
}