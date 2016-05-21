package io.chark.food.app.comment;

import io.chark.food.app.account.AccountService;
import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.app.thread.ThreadService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.comment.Comment;
import io.chark.food.domain.comment.CommentRepository;
import io.chark.food.domain.thread.ThreadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import io.chark.food.domain.thread.Thread;

import java.util.Date;
import java.util.Optional;

@Service
public class CommentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentService.class);
    private final CommentRepository commentRepository;
    private final ThreadRepository threadRepository;
    private final AuditService auditService;
    private final RatingService ratingService;
    private final AccountService accountService;

    @Autowired
    public CommentService(CommentRepository commentRepository, AuditService auditService, RatingService ratingService, AccountService accountService, ThreadRepository threadRepository) {
        this.commentRepository = commentRepository;
        this.auditService = auditService;
        this.ratingService = ratingService;
        this.accountService = accountService;
        this.threadRepository = threadRepository;
    }

    public Optional<Comment> register(Account account, String text, boolean isHidden) {
        Comment comment = new Comment(account, text, isHidden);

        try {

            comment = commentRepository.save(comment);


            LOGGER.debug("Created new Comment");

            auditService.info("Created a new comment");
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Failed while creating new comment");

            auditService.error("Failed to create a comment");
            return Optional.empty();
        }
        return Optional.of(comment);
    }

    public Optional<Comment> update(Comment comment, Comment commentDetails) {
        comment.setAccount(commentDetails.getAccount());
        comment.setThread(commentDetails.getThread());
        try {
            LOGGER.debug("Updating Comment{id={}} ", comment.getId());
            return Optional.ofNullable(commentRepository.save(comment));
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not update comment", e);

            auditService.error("Failed to update comment details");
            return Optional.empty();
        }
    }


    public Optional<Comment> saveComment(long catid, long comid, Comment commentDetails) {

        Optional<Comment> optional;
        Account currentAccount = accountService.getAccount();
        if (currentAccount == null) {
            LOGGER.error("Could not save thread. User not found");

            auditService.error("Failed to save Thread. User not found.");
            return Optional.empty();
        }
        if (comid <= 0) {

            Thread thread = threadRepository.getOne(catid);

            optional = register(currentAccount,
                    commentDetails.getText(),
                    commentDetails.isHidden()
            );
            thread.addComment(optional.get());

        } else {

            optional = Optional.of(commentRepository.findOne(comid));
        }

        if (!optional.isPresent()) {
            return Optional.empty();
        }

        optional = update(optional.get(), commentDetails);
        Thread thread = threadRepository.getOne(catid);

        Comment comment = optional.get();
        comment.setAccount(currentAccount);
        comment.setEditDate(new Date());
        comment.setText(commentDetails.getText());
        try {
            comment = commentRepository.save(comment);
            LOGGER.debug("Saved comment{id={}}", comment.getId());

            return Optional.of(comment);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not save thread", e);

            auditService.error("Failed to save Thread");
            return Optional.empty();
        }
    }

    public Comment upvoteComment(long id, boolean isPositive) {
        Comment comment = commentRepository.findOne(id);
        Account account = accountService.getAccount();
        System.out.println("YEP");
        ratingService.register(isPositive, account);
        return comment;
    }

    public Comment getComment(long id) {
        return commentRepository.findOne(id);
    }
}