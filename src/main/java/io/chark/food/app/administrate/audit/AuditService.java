package io.chark.food.app.administrate.audit;

import io.chark.food.domain.audit.AuditMessage;
import io.chark.food.domain.audit.AuditMessageRepository;
import io.chark.food.domain.extras.Color;
import io.chark.food.util.authentication.AuthenticationUtils;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    private final AuditMessageRepository messageRepository;

    @Autowired
    public AuditService(AuditMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
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
                AuthenticationUtils.getAccount(),
                String.format(message, args),
                color);

        return messageRepository.save(auditMessage);
    }
}