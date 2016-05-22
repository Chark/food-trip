package io.chark.food.app.moderate.newsletter;

import io.chark.food.app.account.AccountService;
import io.chark.food.app.restaurant.offer.OfferService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.offer.Offer;
import io.chark.food.domain.restaurant.newsletter.Newsletter;
import io.chark.food.domain.restaurant.newsletter.NewsletterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NewsletterModerationService {
    private final NewsletterRepository newsletterRepository;
    private final AccountService accountService;
    private final OfferService offerService;

    @Autowired
    public NewsletterModerationService(NewsletterRepository newsletterRepository, AccountService accountService, OfferService offerService) {
        this.newsletterRepository = newsletterRepository;
        this.accountService = accountService;
        this.offerService = offerService;
    }


    public List<Newsletter> getNewsletters() {
        return newsletterRepository.findAll();
    }


    public Newsletter getNewsletter(long id) {
        return newsletterRepository.findOne(id);
    }


    Optional<Newsletter> saveNewsletter(long id,
                                        Newsletter newsletterDetails,
                                        long offers[]) {

        Optional<Newsletter> optional;
        if (id <= 0) {

            optional = register(newsletterDetails.getDescription(),
                    newsletterDetails.getTitle(),
                    newsletterDetails.getExpirationDate(),
                    newsletterDetails.isPublished(),
                    offers);

        } else {

            // Existing account.
            optional = Optional.of(newsletterRepository.findOne(id));
        }

        // No account found, error.
        if (!optional.isPresent()) {
            return Optional.empty();
        }


        // Update account details.
        optional = update(optional.get(), newsletterDetails);
        Newsletter newsletter = optional.get();
        newsletter.setDescription(newsletterDetails.getDescription());
        newsletter.setAccount(accountService.getAccount());
        newsletter.setCreationDate(new Date());

        newsletter.setPublished(newsletterDetails.isPublished());
        newsletter.setExpirationDate(newsletterDetails.getExpirationDate());
        if (offers != null)
            for (long o : offers) {
                newsletter.addOffer(offerService.getOffer(o));
            }
        if (id > 0) {
            newsletter.setEdited(true);
        } else {
            newsletter.setEdited(false);
        }


        try {

            newsletter = newsletterRepository.save(newsletter);

            return Optional.of(newsletter);
        } catch (DataIntegrityViolationException e) {
            return Optional.empty();
        }
    }

    public void delete(long id){
        newsletterRepository.delete(id);
    }

    public Optional<Newsletter> register(String description, String title, String expirationDate, boolean isPublished, long[] offers) {
        Account account = accountService.getAccount();
        Newsletter newsletter = new Newsletter(description, title, expirationDate, account,isPublished);
            if(offers != null)
            for (long l : offers) {
                if(!newsletter.getOffers().contains(offerService.getOffer(l)))
                newsletter.addOffer(offerService.getOffer(l));
            }

        try {
            newsletter = newsletterRepository.save(newsletter);

        } catch (DataIntegrityViolationException e) {
            return Optional.empty();
        }
        return Optional.of(newsletter);
    }

    public Optional<Newsletter> update(Newsletter newsletter, Newsletter newsletterDetails) {

        newsletter.setAccount(accountService.getAccount());
        newsletter.setTitle(newsletterDetails.getTitle());
        newsletter.setEdited(true);
        newsletter.setDescription(newsletterDetails.getDescription());
        newsletter.setExpirationDate(newsletter.getExpirationDate());
        newsletter.setPublished(newsletterDetails.isPublished());
        newsletter.setCreationDate(new Date());
        newsletter.setPublished(newsletterDetails.isPublished());

        newsletter.setTitle(newsletterDetails.getTitle());
        for (Offer o : newsletterDetails.getOffers())
            if(!newsletterDetails.getOffers().contains(o))
            newsletter.addOffer(o);

        try {
            return Optional.ofNullable(newsletterRepository.save(newsletter));
        } catch (DataIntegrityViolationException e) {

            return Optional.empty();
        }
    }



    public List<Newsletter> getPublishedNewsletter(){
        return newsletterRepository.findByPublished(true);
    }



}
