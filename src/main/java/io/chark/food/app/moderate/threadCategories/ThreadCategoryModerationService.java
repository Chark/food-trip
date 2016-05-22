package io.chark.food.app.moderate.threadCategories;

import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.app.thread.ThreadService;
import io.chark.food.app.thread.categories.ThreadCategoryService;
import io.chark.food.domain.thread.Thread;
import io.chark.food.domain.thread.category.ThreadCategory;
import io.chark.food.domain.thread.category.ThreadCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ThreadCategoryModerationService {
    private final ThreadCategoryRepository threadCategoryRepository;
    private final ThreadCategoryService threadCategoryService;
    private  final ThreadService threadService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadCategoryModerationService.class);
    private final AuditService auditService;


    @Autowired
    public ThreadCategoryModerationService(ThreadCategoryRepository threadCategoryRepository,
                                           ThreadService threadService,
                                           ThreadCategoryService threadCategoryService,
                                           AuditService auditService
                                           ) {
        this.threadCategoryRepository = threadCategoryRepository;
        this.threadService = threadService;
        this.threadCategoryService = threadCategoryService;
        this.auditService = auditService;
    }


    public List<ThreadCategory> getThreadCategories() {
        return threadCategoryRepository.findAll();
    }

    public void deleteCategory(long id){
        for(Thread t:threadCategoryRepository.findOne(id).getThreads()){
            threadService.delete(t.getId());
        }
        threadCategoryRepository.delete(id);
    }


    public ThreadCategory getThreadCategory(long id){
        return this.threadCategoryRepository.findOne(id);
    }


    public Optional<ThreadCategory> saveThreadCategory(long id, ThreadCategory threadCategoryDetails) {

        Optional<ThreadCategory> optional;
        if (id <= 0) {

            optional = threadCategoryService.register(threadCategoryDetails.getName(),
                    threadCategoryDetails.getDescription()
            );

        } else {

            optional = Optional.of(threadCategoryRepository.findOne(id));
        }

        if (!optional.isPresent()) {
            return Optional.empty();
        }

        optional = threadCategoryService.update(optional.get(), threadCategoryDetails);

        // Update other details editable only by admins.
        ThreadCategory threadCategory = optional.get();
        threadCategory.setDescription(threadCategoryDetails.getDescription());
        threadCategory.setName(threadCategory.getName());
        if(id > 0){
            threadCategory.setEditDate(new Date());
        }else{
            threadCategory.setEditDate(null);
        }

        try {
            threadCategory = threadCategoryRepository.save(threadCategory);
            LOGGER.debug("Saved ThreadCategory{id={}}", threadCategory.getId());

            auditService.debug("%s ThreadCategory with id: %d via admin panel",
                    id <= 0 ? "Created new" : "Updated", threadCategory.getId());

            return Optional.of(threadCategory);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not save thread category", e);
            auditService.error("Failed to save thread category");
            return Optional.empty();
        }
    }
}
