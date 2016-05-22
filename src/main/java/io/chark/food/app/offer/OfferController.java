package io.chark.food.app.offer;

import io.chark.food.app.account.AccountService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.offer.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping(value = "/offers")
public class OfferController {

    private final AccountService accountService;
    private final OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService,AccountService accountService)
    {
        this.offerService = offerService;
        this.accountService = accountService;
    }

    /**
     * View article categories.
     */
    @RequestMapping(value = "")
    public String list(Model model) {
        int n = 5;
        try{
            Account account = accountService.getAccount();
            n = 10;
        }
        catch(Exception e){
            System.out.println(e);
        }
        List<Offer> offers = offerService.getnOffers(n);

        model.addAttribute("offers", offers);
        return "offers/offers";
    }

}
