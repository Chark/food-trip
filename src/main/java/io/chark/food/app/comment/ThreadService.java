package io.chark.food.app.comment;

import io.chark.food.app.account.AccountService;
import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.domain.authentication.account.Account;
import io.chark.food.domain.comment.Thread;
import io.chark.food.domain.comment.ThreadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class ThreadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadService.class);

    private final ThreadRepository threadRepository;
    private final AccountService accountService;
    private final AuditService auditService;

    @Autowired
    public ThreadService(ThreadRepository threadRepository, AccountService accountService, AuditService auditService) {
        this.threadRepository = threadRepository;
        this.accountService = accountService;
        this.auditService = auditService;
    }

    @PostConstruct
    public void init() {
        Account account = accountService.getAccount("admin");
        if (account == null) {
            return;
        }

        addThread(account, "test title1", "test description 1", false);
        addThread(account, "test title2", "test description 2", false);
        addThread(account, "test title3", "test description 3", false);
        addThread(account, "test title4", "test description 4", false);
        addThread(account, "test title5", "test description 5", false);
    }

    private Optional<Thread> addThread(String title, String description, boolean registrationRequired) {
        return addThread(null, title, description, registrationRequired);
    }

    private Optional<Thread> addThread(Account account, String title, String description, boolean registrationRequired) {
        LOGGER.debug("Creating new Thread{title='{}'}", title);

        // accountService.getAccount(); Do not use account from authentication!

        if (account == null) {
            LOGGER.error("Attempted to create a thread. But user is not logged in or not valid.");
            auditService.error("Failed to create a thread.");
            return Optional.empty();
        }

        try {
            Thread thread = new Thread(account, title, description, registrationRequired);
            Optional<Thread> optional = Optional.of(threadRepository.save(thread));
            auditService.info("Thread '%s' successfully created by '%s'", title, account.getUsername());
            LOGGER.debug("Thread created successfully{username='{}', title='{}'}", account.getUsername(), title);
            return optional;

        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Error in creating a thread", e);
            return Optional.empty();
        }
    }

    public List<Thread> getThreads() {
        return threadRepository.findAll();
    }
}