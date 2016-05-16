package io.chark.food.app.administrate.article;

import io.chark.food.app.administrate.audit.AuditService;
import io.chark.food.domain.article.ArticleCategory;
import io.chark.food.domain.article.ArticleCategoryRepository;
import io.chark.food.util.authentication.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleCategoryAdministrationService {

    private final ArticleCategoryRepository categoryRepository;
    private final AuditService auditService;

    @Autowired
    public ArticleCategoryAdministrationService(ArticleCategoryRepository categoryRepository, AuditService auditService) {
        this.categoryRepository = categoryRepository;
        this.auditService = auditService;
    }

    public Optional<ArticleCategory> getCategory(long id) {
        return Optional.of(categoryRepository.findOne(id));
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
