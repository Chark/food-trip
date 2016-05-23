package io.chark.food.app.moderate.newsletter;

import io.chark.food.app.restaurant.offer.OfferService;
import io.chark.food.domain.restaurant.newsletter.Newsletter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/moderate")
public class NewsletterModerationController {

    private final NewsletterModerationService newsletterModerationService;
    private final OfferService offerService;


    @Autowired
    public NewsletterModerationController(NewsletterModerationService newsletterModerationService,
                                          OfferService offerService) {
        this.newsletterModerationService = newsletterModerationService;
        this.offerService = offerService;
    }

    @RequestMapping(value = "/newsletters", method = RequestMethod.GET)
    public String getData() {
        return "moderate/newsletters";
    }


    @RequestMapping(value = "/newsletters/edit/{id}", method = RequestMethod.GET)
    public String getData(@PathVariable long id, Model model) {
        Newsletter newsletter;

        if (id <= 0) {
            // Id below or equals to zero means t
            // his is a new thread.
            newsletter = new Newsletter();
        } else {
            // Id is above zero, existing account.
            newsletter = newsletterModerationService.getNewsletter(id);
        }

        model.addAttribute("newsletter", newsletter);
        model.addAttribute("offers", offerService.getAllOffers());
        return "moderate/newsletter";
    }




    @RequestMapping(value = "/newsletters/edit/{id}", method = RequestMethod.POST)
    public String saveNewsletter(@PathVariable long id,
                                 Newsletter newsletter,
                                 long[] offers,
                                 Model model) {


        if (!newsletterModerationService.saveNewsletter(id, newsletter, offers).isPresent()) {

            model.addAttribute("offers", offerService.getAllOffers());
            return "moderate/newsletter";
        }

        return "redirect:/moderate/newsletters";
    }


    @ResponseBody
    @RequestMapping(value = "/api/newsletters", method = RequestMethod.GET)
    public List<Newsletter> getComments() {
        return newsletterModerationService.getNewsletters();
    }




    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/api/newsletters/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable long id) {
        newsletterModerationService.delete(id);
    }
}
