package io.chark.food.app.restaurant.details;

import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.comment.Comment;
//import io.chark.food.domain.restaurant.Restaurant;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class RestaurantDetailsService {
//
//
//
//    public RestaurantDetails register(Restaurant restaurant) {
//        Comment comment = new Comment(account,
//                text,
//                hidden);
//
//        try {
//            comment = commentRepository.save(comment);
//            LOGGER.debug("Created new Comment {username='{}'}", account.getPrettyUsername());
//
//            auditService.info("New Comment created by user : %s", account.getPrettyUsername());
//        } catch (DataIntegrityViolationException e) {
//            LOGGER.error("Failed while creating new Comment{username='{}'}", account.getPrettyUsername(), e);
//
//            auditService.error("Failed to create a Comment by user: %s", account.getPrettyUsername());
//            return null;
//        }
//        return comment;
//    }
//}
