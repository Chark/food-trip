package io.chark.food.app.administrate.audit;

import io.chark.food.domain.audit.AuditMessage;
import io.chark.food.domain.audit.AuditMessageRepository;
import io.chark.food.domain.authentication.account.AccountRepository;
import io.chark.food.domain.extras.Color;
import io.chark.food.domain.restaurant.RestaurantRepository;
import io.chark.food.util.authentication.AuthenticationUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AuditService {

    private final RestaurantRepository restaurantRepository;
    private final AuditMessageRepository messageRepository;
    private final AccountRepository accountRepository;

    private static final int SEVEN_DAYS = 7;

    @Autowired
    public AuditService(RestaurantRepository restaurantRepository,
                        AuditMessageRepository messageRepository,
                        AccountRepository accountRepository) {

        this.restaurantRepository = restaurantRepository;
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Get a package of audit goodies.
     *
     * @return package of audit statistics.
     */
    public AuditPackage getAuditPackage() {
        return new AuditPackage(
                getAccountCountsPerDay(SEVEN_DAYS),
                restaurantRepository.count(),
                accountRepository.count());
    }

    /**
     * Get a list of audit messages by account id.
     *
     * @param id account id.
     * @return list of audit messages, never null.
     */
    public List<AuditMessage> getAuditMessages(long id) {
        return messageRepository.findByAccountId(id);
    }

    /**
     * Get all audit messages.
     *
     * @return list of audit messages, never null.
     */
    public List<AuditMessage> getAuditMessages() {
        return messageRepository.findAll();
    }

    /**
     * Get a single audit message by id.
     *
     * @param id audit message id.
     * @return audit message or null if it was not found.
     */
    public AuditMessage getAuditMessage(long id) {
        return messageRepository.findOne(id);
    }

    /**
     * Create a info audit message.
     *
     * @param message the message body.
     * @param args    message arguments.
     * @return created audit message.
     */
    public AuditMessage info(String message, Object... args) {
        return message(Color.BLUE, message, args);
    }

    /**
     * Create a debug audit message.
     *
     * @param message the message body.
     * @param args    message arguments.
     * @return created audit message.
     */
    public AuditMessage debug(String message, Object... args) {
        return message(Color.GREEN, message, args);
    }

    /**
     * Create a warning type audit message.
     *
     * @param message the message body.
     * @param args    message arguments.
     * @return created audit message.
     */
    public AuditMessage warn(String message, Object... args) {
        return message(Color.YELLOW, message, args);
    }

    /**
     * Create a error audit message.
     *
     * @param message the message body.
     * @param args    message arguments.
     * @return created audit message.
     */
    public AuditMessage error(String message, Object... args) {
        return message(Color.RED, message, args);
    }

    /**
     * Create a generic audit message.
     *
     * @param color   audit message color.
     * @param message the message body.
     * @param args    message arguments.
     * @return created audit message.
     */
    public AuditMessage message(Color color, String message, Object... args) {
        AuditMessage auditMessage = new AuditMessage(
                accountRepository.findOne(AuthenticationUtils.getId()),
                String.format(message, args),
                color);

        return messageRepository.save(auditMessage);
    }

    /**
     * Get a count of accounts per each day.
     *
     * @param days list of days to go back.
     * @return list of account counts for each day.
     */
    private List<Long> getAccountCountsPerDay(int days) {

        // Full counts.
        List<Long> counts = new ArrayList<>();

        // Start counting from some time back.
        Calendar startFrom = Calendar.getInstance();
        startFrom.add(Calendar.DAY_OF_YEAR, -days + 1);

        // Leave only year, month, day.
        startFrom = DateUtils.truncate(startFrom, Calendar.DAY_OF_MONTH);

        Date current = new Date();

        // One day ahead.
        Calendar ahead = (Calendar) startFrom.clone();
        ahead.add(Calendar.DAY_OF_YEAR, 1);

        while (startFrom.getTime().compareTo(current) <= 0) {

            // Find time in-between dates (really hackish).
            counts.add(accountRepository.countAccountsByDate(startFrom.getTime(), ahead.getTime()));

            // Shift time forward.
            startFrom.add(Calendar.DAY_OF_YEAR, 1);
            ahead.add(Calendar.DAY_OF_YEAR, 1);
        }
        return counts;
    }
}