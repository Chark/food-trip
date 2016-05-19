package io.chark.food.app.comment;

import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.comment.Comment;
import io.chark.food.domain.comment.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import io.chark.food.domain.thread.Thread;

import java.util.Optional;

@Service
public class CommentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentService.class);
    private final CommentRepository commentRepository;
    private final AuditService auditService;

    @Autowired
    public CommentService(CommentRepository commentRepository, AuditService auditService) {
        this.commentRepository = commentRepository;
        this.auditService = auditService;
    }

    public Comment register(Account account, String text, boolean hidden) {
        Comment comment = new Comment(account,
                text,
                hidden);

        try {
            comment = commentRepository.save(comment);
            LOGGER.debug("Created new Comment {username='{}'}", account.getPrettyUsername());

            auditService.info("New Comment created by user : %s", account.getPrettyUsername());
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Failed while creating new Comment{username='{}'}", account.getPrettyUsername(), e);

            auditService.error("Failed to create a Comment by user: %s", account.getPrettyUsername());
            return null;
        }
        return comment;
    }
}