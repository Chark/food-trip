package io.chark.food.app.restaurant;

import io.chark.food.domain.audit.RestaurantAuditMessage;
import io.chark.food.domain.audit.RestaurantAuditMessageRepository;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.authentication.account.AccountRepository;
import io.chark.food.domain.extras.Color;
import io.chark.food.util.authentication.AuthenticationUtils;
import io.chark.food.util.exception.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantAuditService {

    private final RestaurantAuditMessageRepository messageRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public RestaurantAuditService(RestaurantAuditMessageRepository messageRepository,
                                  AccountRepository accountRepository) {

        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Get all restaurant audit messages. Must be called by an authenticated user with a restaurant.
     *
     * @return list of restaurant audit messages.
     */
    public List<RestaurantAuditMessage> getRestaurantAuditMessages() {
        Account account = accountRepository.findOne(AuthenticationUtils.getIdOrThrow());
        if (!account.hasRestaurant()) {
            throw new UnauthorizedException("Account must have a restaurant to get restaurant audit messages");
        }
        return messageRepository
                .findByRestaurantId(account.getRestaurant().getId());
    }


    /**
     * Create a info audit message for a restaurant.
     *
     * @param message the message body.
     * @param args    message arguments.
     * @return created audit message.
     */
    public RestaurantAuditMessage info(String message, String action, Object... args) {
        return message(Color.BLUE, message, action, args);
    }

    /**
     * Create a warning type audit message for a restaurant.
     *
     * @param message the message body.
     * @param args    message arguments.
     * @return created audit message.
     */
    public RestaurantAuditMessage warn(String message, String action, Object... args) {
        return message(Color.YELLOW, message, action, args);
    }


    /**
     * Create a generic restaurant audit message.
     *
     * @param color   audit message color.
     * @param message the message body.
     * @param args    message arguments.
     * @param action  action which was made when creating this message.
     * @return created audit message.
     */
    private RestaurantAuditMessage message(Color color, String message, String action, Object... args) {
        Account account = accountRepository.findOne(AuthenticationUtils.getIdOrThrow());
        if (!account.hasRestaurant()) {
            throw new UnauthorizedException("Account must have a restaurant to log a restaurant message");
        }

        // Create a restaurant audit message.
        RestaurantAuditMessage auditMessage = new RestaurantAuditMessage(
                account,
                String.format(message, args),
                color,
                action,
                account.getRestaurant());

        return messageRepository.save(auditMessage);
    }
}
