package io.chark.food.app.administrate.article.category;

import io.chark.food.app.administrate.account.AccountAdministrationService;
import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.app.article.category.ArticleCategoryService;
import io.chark.food.domain.article.category.ArticleCategory;
import io.chark.food.domain.article.category.ArticleCategoryRepository;
import io.chark.food.util.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleCategoryAdministrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleCategoryAdministrationService.class);

    private final ArticleCategoryRepository categoryRepository;
    private final ArticleCategoryService categoryService;
    private final AuditService auditService;

    @Autowired
    public ArticleCategoryAdministrationService(ArticleCategoryRepository categoryRepository,
                                                ArticleCategoryService categoryService,
                                                AuditService auditService) {

        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
        this.auditService = auditService;
    }

    /**
     * Create a new article category or update an existing one based on id.
     *
     * @param id              article category id.
     * @param categoryDetails details used in creation or updating.
     * @return article category optional.
     */
    public Optional<ArticleCategory> saveCategory(long id, ArticleCategory categoryDetails) {

        // Below or equals means this is a new account.
        Optional<ArticleCategory> optional;
        if (id <= 0) {

            // Reuse the register method to save a new account.
            optional = categoryService.register(
                    categoryDetails.getTitle(),
                    categoryDetails.getDescription()
            );

        } else {

            // Existing account.
            optional = Optional.of(categoryRepository.findOne(id));
        }

        // No account found, error.
        if (!optional.isPresent()) {
            return Optional.empty();
        }

        // Update account details.
        optional = categoryService.update(optional.get(), categoryDetails);

        // Update other details editable only by admins.
        ArticleCategory category = optional.get();
        category.setTitle(categoryDetails.getTitle());
        category.setDescription(categoryDetails.getDescription());

        try {
            category = categoryRepository.save(category);
            LOGGER.debug("Saved ArticleCategory{id={}}", category.getId());

            auditService.debug("%s ArticleCategory with id: %d via admin panel",
                    id <= 0 ? "Created new" : "Updated", category.getId());

            return Optional.of(category);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not save article category", e);

            auditService.error("Failed to save ArticleCategory");
            return Optional.empty();
        }
    }

    /**
     * Get an article category by id.
     *
     * @return article category.
     * @throws NotFoundException if the article category is not found.
     */
    public ArticleCategory getCategory(long id) {
        ArticleCategory category = categoryRepository.findOne(id);
        if (category == null) {
            auditService.warn("Attempted to query non-existing ArticleCategory with id: %d", id);
            throw new NotFoundException(ArticleCategory.class, id);
        }
        return category;
    }

    /**
     * Get all article categories.
     *
     * @return list of article categories.
     */
    List<ArticleCategory> getCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Delete specified article category.
     *
     * @param id user account id.
     */
    public void delete(long id) {
        ArticleCategory category = categoryRepository.findOne(id);
        categoryRepository.delete(category);
        auditService.info("Deleted Article Category with id: %d", id);
    }
}
