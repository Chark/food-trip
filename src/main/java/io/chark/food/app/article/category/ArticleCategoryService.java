package io.chark.food.app.article.category;

import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.domain.article.category.ArticleCategory;
import io.chark.food.domain.article.category.ArticleCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleCategoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleCategoryService.class);

    private final ArticleCategoryRepository categoryRepository;
    private final AuditService auditService;

    @Autowired
    public ArticleCategoryService(ArticleCategoryRepository categoryRepository,
                                  AuditService auditService) {

        this.categoryRepository = categoryRepository;
        this.auditService = auditService;
    }

    /**
     * Register a new Article Category.
     *
     * @param title         title of the article category.
     * @param description   description of the article category.
     * @return created article category or empty optional.
     */
    public Optional<ArticleCategory> register(String title, String description) {
        ArticleCategory category = new ArticleCategory(
                title,
                description);

        try {
            category = categoryRepository.save(category);
            LOGGER.debug("Created new user ArticleCategory{title='{}'}", title);

            auditService.info("Created a new ArticleCategory using title: %s", title);
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Failed while creating new user ArticleCategory{title='{}'}", title, e);

            auditService.error("Failed to create a new ArticleCategory using title: %s", title);
            return Optional.empty();
        }
        return Optional.of(category);
    }

    /**
     * Update specified article category details.
     *
     * @param category          article category which is to be updated.
     * @param categoryDetails   details which are used in updating.
     * @return                  updated article category optional.
     */
    public Optional<ArticleCategory> update(ArticleCategory category, ArticleCategory categoryDetails) {

        // Update other stuff.
        category.setTitle(categoryDetails.getTitle());
        category.setDescription(categoryDetails.getDescription());

        try {
            LOGGER.debug("Updating ArticleCategory{id={}} details", category.getId());
            return Optional.ofNullable(categoryRepository.save(category));
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Could not update article category", e);

            auditService.error("Failed to update article category details");
            return Optional.empty();
        }
    }

    /**
     * Get a list of article categories.
     * @return list of article categories.
     */
    public List<ArticleCategory> getCategories() {
        return categoryRepository.findAll();
    }
}
