package io.chark.food.app.comment;

import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.comment.Comment;
import io.chark.food.domain.comment.Rating;
import io.chark.food.domain.comment.RatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RatingService.class);
    private final RatingRepository ratingRepository;
    private final AuditService auditService;

    @Autowired
    public RatingService(RatingRepository ratingRepository, AuditService auditService) {
        this.ratingRepository = ratingRepository;
        this.auditService = auditService;
    }

    public Rating register(boolean isPositive, Account account) {
        Rating rating = new Rating(isPositive, account);

        try {
            rating = ratingRepository.save(rating);
            LOGGER.debug("Voted for comment {isPositive='{}'}", isPositive);

            auditService.info("Voted for comment: %b", isPositive);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Failed to vote for comment", e);

            auditService.error("Failed to vote for comment");
            return null;
        }
        return rating;
    }

    public void register(Rating rating){
        try {
            rating = ratingRepository.save(rating);
            LOGGER.debug("Voted for comment {isPositive='{}'}", rating.isPositive());

            auditService.info("Voted for comment: %b", rating.isPositive());
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Failed to vote for comment", e);

            auditService.error("Failed to vote for comment");
        }
    }

    public int getScore(long id) {
        int score = 0;

        List<Rating> ratings = ratingRepository.findAll();

        for (Rating r : ratings) {
            if (r.isPositive()) {
                score++;
            } else {
                score--;
            }
        }

        return score;
    }


}
