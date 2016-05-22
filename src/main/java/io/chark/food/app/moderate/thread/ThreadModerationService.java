package io.chark.food.app.moderate.thread;

import io.chark.food.app.account.AccountService;
import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.app.thread.ThreadService;
import io.chark.food.domain.thread.Thread;
import io.chark.food.domain.thread.ThreadRepository;
import io.chark.food.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ThreadModerationService {

    private final ThreadRepository threadRepository;
    private final ThreadService threadService;
    private final AuditService auditService;
    private final AccountService accountService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadModerationService.class);

    @Autowired
    public ThreadModerationService(ThreadRepository threadRepository, AuditService auditService, ThreadService threadService, AccountService accountService) {
        this.threadRepository = threadRepository;
        this.auditService = auditService;
        this.threadService = threadService;
        this.accountService = accountService;
    }

    public List<Thread> getThreads(){
        return threadRepository.findAll();
    }

    public Thread getThread(Long id){
        Thread thread = threadRepository.findOne(id);
        if (thread == null) {
            auditService.warn("Attempted to query non-existing Thread with id: %d", id);
            throw new NotFoundException(Thread.class, id);
        }
        return thread;
    }




    public Optional<Thread> saveThread(long id, Thread threadDetails) {

        Optional<Thread> optional;
        if (id <= 0) {

            optional = threadService.register(
                    threadDetails.getAccount(),
                    threadDetails.getTitle(),
                    threadDetails.getDescription(),
                    threadDetails.isRegistrationRequired(),
                    threadDetails.getThreadCategory()
                    );

        } else {

            optional = Optional.of(threadRepository.findOne(id));
        }

        if (!optional.isPresent()) {
            return Optional.empty();
        }

        optional = threadService.update(optional.get(), threadDetails);

        // Update other details editable only by admins.
        Thread thread = optional.get();
        thread.setAccount(accountService.getAccount());
        thread.setTitle(threadDetails.getTitle());
        thread.setDescription(threadDetails.getDescription());
        thread.setThreadLink(threadDetails.getThreadLink());
        thread.setEditDate(new Date());
        thread.setRegistrationRequired(threadDetails.isRegistrationRequired());

        try {
            thread = threadRepository.save(thread);
            LOGGER.debug("Saved Thread{id={}}", thread.getId());

            auditService.debug("%s Thread with id: %d via admin panel",
                    id <= 0 ? "Created new" : "Updated", thread.getId());

            return Optional.of(thread);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not save thread", e);
            auditService.error("Failed to save thread");
            return Optional.empty();
        }
    }

    public void delete(long id) {
        Thread thread = threadRepository.findOne(id);
        threadRepository.delete(thread);
        auditService.info("Deleted Thread with id: %d", id);
    }
}
