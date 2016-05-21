package io.chark.food.app.thread;

import io.chark.food.app.account.AccountService;
import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.app.comment.CommentService;
import io.chark.food.app.thread.categories.ThreadCategoryService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.comment.Comment;
import io.chark.food.domain.thread.Thread;
import io.chark.food.domain.thread.ThreadRepository;
import io.chark.food.domain.thread.category.ThreadCategory;
import io.chark.food.domain.thread.category.ThreadCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ThreadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadService.class);

    private final ThreadRepository threadRepository;
    private final ThreadCategoryService threadCategoryService;
    private final AccountService accountService;
    private final CommentService commentService;
    private final AuditService auditService;

    @Autowired
    public ThreadService(ThreadRepository threadRepository, AuditService auditService, CommentService commentService, ThreadCategoryService threadCategoryService, AccountService accountService) {
        this.threadRepository = threadRepository;
        this.auditService = auditService;
        this.commentService = commentService;
        this.threadCategoryService = threadCategoryService;
        this.accountService = accountService;
    }

    public Optional<Thread> update(Thread thread, Thread threadDetails) {

        // Update other stuff.
        thread.setTitle(threadDetails.getTitle());
        thread.setDescription(threadDetails.getDescription());
        thread.setAccount(threadDetails.getAccount());
        thread.setRegistrationRequired(threadDetails.isRegistrationRequired());
        thread.setThreadLink(threadDetails.getThreadLink());
        thread.setEditDate(new Date());
        try {
            LOGGER.debug("Updating Thread{id={}} details", thread.getId());
            return Optional.ofNullable(threadRepository.save(thread));
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not update thread", e);

            auditService.error("Failed to update thread details");
            return Optional.empty();
        }
    }

    public void incrementView(Thread t){
        t.setViewCount(t.incremenetViewCount());
        threadRepository.save(t);
    }

    public List<Comment> getComments(long id){
        return threadRepository.findOne(id).getComments();
    }

    public Optional<Thread> register(Account account, String title, String description, boolean registrationRequired, ThreadCategory threadCategory) {
        Thread thread = new Thread(account,
                title,
                description,
                registrationRequired,
                threadCategory);


        //TODO remove from live
        for (int i = 0; i < 10; i++) {
            thread.addComment(commentService.register(account, "Long text is really long not much lol " + i, false).get());
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



    public void addComment(long id, Comment comment){

        Thread t = threadRepository.findOne(id);
        t.addComment(comment);
    }
    public Thread getThread(long id) {
        return threadRepository.findOne(id);
    }

    public void delete(long id){
        Thread thread = threadRepository.findOne(id);
        threadRepository.delete(thread);
        auditService.info("Deleted Thread with id: %d", id);

    }

    public Optional<Thread> saveThread(long id, long cid, Thread threadDetails) {

        Optional<Thread> optional;
        Account currentAccount = accountService.getAccount();
        if(currentAccount == null){
            LOGGER.error("Could not save thread. User not found");

            auditService.error("Failed to save Thread. User not found.");
            return Optional.empty();
        }
        if (id <= 0) {

            ThreadCategory threadCategory = threadCategoryService.getThreadCategory(id);

            optional = register(currentAccount,
                    threadDetails.getTitle(),
                    threadDetails.getDescription(),
                    threadDetails.isRegistrationRequired(),
                    threadCategory
            );

        } else {

            optional = Optional.of(threadRepository.findOne(id));
        }

        if (!optional.isPresent()) {
            return Optional.empty();
        }

        optional = update(optional.get(), threadDetails);


        Thread thread = optional.get();
        thread.setTitle(threadDetails.getTitle());
        thread.setDescription(threadDetails.getDescription());
        thread.setAccount(currentAccount);
        thread.setThreadLink(threadDetails.getThreadLink());


        try {
            thread = threadRepository.save(thread);
            LOGGER.debug("Saved Thread{id={}}", thread.getId());

            auditService.debug("%s Thread with id: %d via front user",
                    id <= 0 ? "Created new" : "Updated", thread.getId());

            return Optional.of(thread);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not save thread", e);

            auditService.error("Failed to save Thread");
            return Optional.empty();
        }
    }

    public List<Thread> getThreads() {
        return threadRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
}