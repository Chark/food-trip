package io.chark.food.app.thread.categories;

import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.domain.thread.Thread;
import io.chark.food.domain.thread.category.ThreadCategory;
import io.chark.food.domain.thread.category.ThreadCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class ThreadCategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadCategoryService.class);

    private final ThreadCategoryRepository threadCategoryRepository;
    private final AuditService auditService;

    @Autowired
    public ThreadCategoryService(ThreadCategoryRepository threadCategoryRepository, AuditService auditService) {
        this.threadCategoryRepository = threadCategoryRepository;
        this.auditService = auditService;
    }


    public Optional<ThreadCategory> register(String name, String description) {
        ThreadCategory threadCategory = new ThreadCategory(name,
                description
        );

        try {
            threadCategory = threadCategoryRepository.save(threadCategory);
            LOGGER.debug("Created new ThreadCategory{name='{}'}", name);
            auditService.info("Created a new Thread Category using name: %s", name);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Failed while creating new Thread{title='{}'}", name, e);

            auditService.error("Failed to create a Thread using title: %s", name);
            return Optional.empty();
        }
        return Optional.of(threadCategory);
    }

    public List<ThreadCategory> getThreadCategories (){
        return threadCategoryRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
    }

    public ThreadCategory getThreadCategory(long id) {
        return threadCategoryRepository.findOne(id);
    }

}
