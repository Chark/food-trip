package io.chark.food.app.moderate.comment;

import io.chark.food.app.account.AccountService;
import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.app.article.ArticleService;
import io.chark.food.app.comment.CommentService;
import io.chark.food.app.thread.ThreadService;
import io.chark.food.domain.article.Article;
import io.chark.food.domain.comment.Comment;
import io.chark.food.domain.comment.CommentRepository;
import io.chark.food.domain.thread.Thread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentModerationService {

    private final CommentRepository commentRepository;
    private final AuditService auditService;
    private final CommentService commentService;
    private final AccountService accountService;
    private final ArticleService articleService;
    private final ThreadService threadService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentModerationService.class);

    @Autowired
    public CommentModerationService(CommentRepository commentRepository, AuditService auditService, AccountService accountService, CommentService commentService, ArticleService articleService, ThreadService threadService) {
        this.commentRepository = commentRepository;
        this.auditService = auditService;
        this.accountService = accountService;
        this.commentService = commentService;
        this.articleService = articleService;
        this.threadService = threadService;
    }


    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    public Comment getComment(long id) {
        return commentRepository.findOne(id);
    }


    public Optional<Comment> saveComment(long id, Comment commentDetails) {

        Optional<Comment> optional;
        if (id <= 0) {

            optional = commentService.register(
                    commentDetails.getAccount(),
                    commentDetails.getText(),
                    commentDetails.isHidden()
            );

        } else {

            optional = Optional.of(commentRepository.findOne(id));
        }

        if (!optional.isPresent()) {
            return Optional.empty();
        }

        optional = commentService.update(optional.get(), commentDetails);

        // Update other details editable only by admins.
        Comment comment = optional.get();
        comment.setAccount(accountService.getAccount());
        comment.setText(commentDetails.getText());
        comment.setEditDate(new Date());
        comment.setHidden(commentDetails.isHidden());

        try {
            comment = commentRepository.save(comment);
            LOGGER.debug("Saved Comment{id={}}", comment.getId());

            auditService.debug("%s Comment with id: %d via admin panel",
                    id <= 0 ? "Created new" : "Updated", comment.getId());

            return Optional.of(comment);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not save comment", e);
            auditService.error("Failed to save comment");
            return Optional.empty();
        }
    }

    public void delete(long id) {
        Comment comment = commentRepository.findOne(id);
        for (Article a : articleService.getArticles()) {
            a.removeComment(comment);
        }
        for (Thread t : threadService.getThreads()) {
            t.removeComment(comment);
        }
        commentRepository.delete(comment);
        auditService.info("Deleted comment with id: %d", id);
    }
}
