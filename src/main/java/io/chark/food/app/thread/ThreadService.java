package io.chark.food.app.thread;

import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.app.comment.CommentService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.comment.Comment;
import io.chark.food.domain.thread.Thread;
import io.chark.food.domain.thread.ThreadRepository;
import io.chark.food.domain.thread.category.ThreadCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ThreadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadService.class);

    private final ThreadRepository threadRepository;
    private final CommentService commentService;
    private final AuditService auditService;

    @Autowired
    public ThreadService(ThreadRepository threadRepository, AuditService auditService, CommentService commentService) {
        this.threadRepository = threadRepository;
        this.auditService = auditService;
        this.commentService = commentService;
    }

    public Optional<Thread> update(Thread thread, Thread threadDetails) {

        // Update other stuff.
        thread.setTitle(threadDetails.getTitle());
        thread.setDescription(threadDetails.getDescription());
        thread.setComments(threadDetails.getComments());
        thread.setAccount(threadDetails.getAccount());
        thread.setEditDate(threadDetails.getEditDate());
        thread.setRegistrationRequired(threadDetails.isRegistrationRequired());
        thread.setThreadLink(threadDetails.getThreadLink());

        try {
            LOGGER.debug("Updating Thread{id={}} details", thread.getId());
            return Optional.ofNullable(threadRepository.save(thread));
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not update thread", e);

            auditService.error("Failed to update thread details");
            return Optional.empty();
        }
    }

    public Optional<Thread> register(Account account, String title, String description, boolean registrationRequired, ThreadCategory threadCategory) {
        Thread thread = new Thread(account,
                title,
                description,
                registrationRequired,
                threadCategory);


        //TODO remove from live
        for (int i = 0; i < 10; i++) {
            thread.addComment(commentService.register(account, "Long text is really long not much lol " + i, false));
        }
        try {

            thread = threadRepository.save(thread);


            LOGGER.debug("Created new Thread{title='{}'}", title);

            auditService.info("Created a new Thread using title: %s", title);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Failed while creating new Thread{title='{}'}", title, e);

            auditService.error("Failed to create a Thread using title: %s", title);
            return Optional.empty();
        }
        return Optional.of(thread);
    }


    public Optional<Thread> addComment(Optional<Thread> t, Comment comment) {
        try {
            t.get().addComment(comment);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Failed while creating new Comment");
            auditService.error("Failed to create a new Comment");
            return Optional.empty();
        }


        return t;
    }

    public Thread getThread(long id) {
        return threadRepository.findOne(id);
    }


    public List<Thread> getThreads() {
        return threadRepository.findAll();
    }
}